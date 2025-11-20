package pl.agh.edu.hotel.commands;

import org.junit.jupiter.api.io.TempDir;
import pl.agh.edu.hotel.model.Hotel;
import pl.agh.edu.hotel.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for LoadCommand
 */
class LoadCommandTest {

    private Hotel hotel;
    private LoadCommand command;
    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errorStream;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        hotel = new Hotel("Test Hotel", 2);
        command = new LoadCommand();
        command.setHotel(hotel);

        originalOut = System.out;
        originalErr = System.err;
        outputStream = new ByteArrayOutputStream();
        errorStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(errorStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    @DisplayName("Should load hotel state from CSV file")
    void testLoadFromFile() throws IOException {
        // Create test CSV file with 12 columns
        Path csvFile = tempDir.resolve("test_load.csv");
        String csvContent = """
                RoomNumber,Description,Price,Capacity,Occupied,MainGuestFirstName,MainGuestLastName,CheckInDate,CheckOutDate,StayDuration,AdditionalInfo,AdditionalGuestsCount
                101,Single,150.0,1,true,Kacper,Lipiec,2025-10-26,2025-10-29,3,VIP,0
                102,Double,250.0,2,false,,,,,,,0
                """;
        Files.writeString(csvFile, csvContent);

        String input = csvFile + "\n";
        command.setScanner(new Scanner(input));

        command.execute();

        // Verify rooms loaded
        Room room101 = hotel.getRoom(101);
        assertNotNull(room101, "Room 101 should be loaded");
        assertEquals("Single", room101.getDescription());
        assertEquals(150.0, room101.getPricePerNight());
        assertTrue(room101.isOccupied(), "Room 101 should be occupied");
        assertNotNull(room101.getMainGuest(), "Room 101 should have a guest");
        assertEquals("Kacper", room101.getMainGuest().getFirstName());
        assertEquals("Lipiec", room101.getMainGuest().getLastName());

        Room room102 = hotel.getRoom(102);
        assertNotNull(room102, "Room 102 should be loaded");
        assertFalse(room102.isOccupied(), "Room 102 should not be occupied");
    }

    @Test
    @DisplayName("Should handle non-existent file")
    void testLoadNonExistentFile() {
        String input = "nonexistent_file_xyz.csv\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String output = outputStream.toString() + errorStream.toString();
        assertTrue(output.contains("Error") || output.contains("loading") ||
                        output.contains("not found") || output.contains("No such file"),
                "Should show error for non-existent file");
    }

    @Test
    @DisplayName("Should handle empty filename")
    void testLoadEmptyFilename() {
        String input = "\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String errorOutput = errorStream.toString();
        assertTrue(errorOutput.contains("Error") || errorOutput.contains("cannot be empty") ||
                        errorOutput.contains("empty"),
                "Should show error for empty filename");
    }

    @Test
    @DisplayName("Should auto-append .csv extension")
    void testAutoAppendExtension() throws IOException {
        Path csvFile = tempDir.resolve("test_load.csv");
        String csvContent = """
                RoomNumber,Description,Price,Capacity,Occupied,MainGuestFirstName,MainGuestLastName,CheckInDate,CheckOutDate,StayDuration,AdditionalInfo,AdditionalGuestsCount
                101,Single,150.0,1,false,,,,,,,0
                """;
        Files.writeString(csvFile, csvContent);

        String filenameWithoutExt = tempDir.resolve("test_load").toString();
        String input = filenameWithoutExt + "\n";
        command.setScanner(new Scanner(input));

        command.execute();

        Room room = hotel.getRoom(101);
        assertNotNull(room, "Room should be loaded even without .csv extension");
        assertEquals("Single", room.getDescription());
    }

    @Test
    @DisplayName("Should skip empty lines in CSV")
    void testSkipEmptyLines() throws IOException {
        Path csvFile = tempDir.resolve("test_empty_lines.csv");
        String csvContent = """
                RoomNumber,Description,Price,Capacity,Occupied,MainGuestFirstName,MainGuestLastName,CheckInDate,CheckOutDate,StayDuration,AdditionalInfo,AdditionalGuestsCount
                101,Single,150.0,1,false,,,,,,,0
                
                102,Double,250.0,2,false,,,,,,,0
                """;
        Files.writeString(csvFile, csvContent);

        String input = csvFile + "\n";
        command.setScanner(new Scanner(input));

        command.execute();

        assertNotNull(hotel.getRoom(101));
        assertNotNull(hotel.getRoom(102));
    }

    @Test
    @DisplayName("Should handle CSV with quoted values")
    void testLoadCSVWithQuotes() throws IOException {
        Path csvFile = tempDir.resolve("test_quotes.csv");
        String csvContent = """
                RoomNumber,Description,Price,Capacity,Occupied,MainGuestFirstName,MainGuestLastName,CheckInDate,CheckOutDate,StayDuration,AdditionalInfo,AdditionalGuestsCount
                101,"Luxury Suite",350.0,2,true,Kacper,Lipiec,2025-10-26,2025-10-29,3,"VIP, Special Request",0
                """;
        Files.writeString(csvFile, csvContent);

        String input = csvFile + "\n";
        command.setScanner(new Scanner(input));

        command.execute();

        Room room = hotel.getRoom(101);
        assertNotNull(room);
        assertEquals("Luxury Suite", room.getDescription());
        assertEquals("VIP, Special Request", room.getMainGuest().getAdditionalInfo());
    }

    @Test
    @DisplayName("Should update existing room instead of creating duplicate")
    void testUpdateExistingRoom() throws IOException {
        // Add existing room
        hotel.addRoom(new Room(101, "Old Description", 1, 100.0));

        Path csvFile = tempDir.resolve("test_update.csv");
        String csvContent = """
                RoomNumber,Description,Price,Capacity,Occupied,MainGuestFirstName,MainGuestLastName,CheckInDate,CheckOutDate,StayDuration,AdditionalInfo,AdditionalGuestsCount
                101,Single,150.0,1,false,,,,,,,0
                """;
        Files.writeString(csvFile, csvContent);

        String input = csvFile + "\n";
        command.setScanner(new Scanner(input));

        command.execute();

        Room room = hotel.getRoom(101);
        assertNotNull(room);
        assertFalse(room.isOccupied());
    }

    @Test
    @DisplayName("Should return correct command name")
    void testGetCommandName() {
        assertEquals("load", command.getCommandName());
    }
}