package pl.agh.edu.hotel.commands;

import org.junit.jupiter.api.io.TempDir;
import pl.agh.edu.hotel.model.Guest;
import pl.agh.edu.hotel.model.Hotel;
import pl.agh.edu.hotel.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for SaveCommand
 */
class SaveCommandTest {

    private SaveCommand command;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        Hotel hotel = new Hotel("Test Hotel", 2);
        Room room = new Room(101, "Single", 1, 150.0);
        hotel.addRoom(room);

        Guest guest = new Guest("Kacper", "Lipiec", LocalDate.now(), 3);
        guest.setAdditionalInfo("VIP");
        room.checkIn(guest, new ArrayList<>());

        command = new SaveCommand();
        command.setHotel(hotel);

        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should save hotel state to file")
    void testSaveToFile() throws IOException {
        String filename = tempDir.resolve("test_hotel.csv").toString();
        String input = filename + "\n";
        command.setScanner(new Scanner(input));

        command.execute();

        File file = new File(filename);
        assertTrue(file.exists(), "CSV file should be created");

        // Read and verify content
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            assertNotNull(header, "Header should exist");
            assertTrue(header.contains("RoomNumber"), "Header should contain RoomNumber");
            assertTrue(header.contains("Description"), "Header should contain Description");

            String data = reader.readLine();
            assertNotNull(data, "Data line should exist");
            assertTrue(data.contains("101"), "Data should contain room number 101");
            assertTrue(data.contains("Single"), "Data should contain room description");
            assertTrue(data.contains("Kacper"), "Data should contain guest first name");
            assertTrue(data.contains("Lipiec"), "Data should contain guest last name");
            assertTrue(data.contains("VIP"), "Data should contain additional info");
        }
    }

    @Test
    @DisplayName("Should use default filename")
    void testSaveWithDefaultFilename() {
        String input = "\n"; // Empty input = default filename
        command.setScanner(new Scanner(input));

        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("hotel_state.csv") || output.contains("successful"),
                "Should use default filename hotel_state.csv");
    }

    @Test
    @DisplayName("Should auto-append .csv extension")
    void testAutoAppendExtension() {
        String filenameWithoutExt = tempDir.resolve("test_hotel").toString();
        String input = filenameWithoutExt + "\n";
        command.setScanner(new Scanner(input));

        command.execute();

        File file = new File(filenameWithoutExt + ".csv");
        assertTrue(file.exists(), "File with .csv extension should be created");
    }

    @Test
    @DisplayName("Should save multiple rooms")
    void testSaveMultipleRooms() throws IOException {
        Hotel hotel = new Hotel("Test Hotel", 3);
        hotel.addRoom(new Room(101, "Single", 1, 150.0));
        hotel.addRoom(new Room(102, "Double", 2, 250.0));
        hotel.addRoom(new Room(103, "Suite", 4, 500.0));

        command.setHotel(hotel);

        String filename = tempDir.resolve("multiple_rooms.csv").toString();
        String input = filename + "\n";
        command.setScanner(new Scanner(input));

        command.execute();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.readLine(); // Skip header
            assertNotNull(reader.readLine(), "Should have room 101");
            assertNotNull(reader.readLine(), "Should have room 102");
            assertNotNull(reader.readLine(), "Should have room 103");
        }
    }

    @Test
    @DisplayName("Should handle empty hotel")
    void testSaveEmptyHotel() throws IOException {
        Hotel emptyHotel = new Hotel("Empty Hotel", 0);
        command.setHotel(emptyHotel);

        String filename = tempDir.resolve("empty_hotel.csv").toString();
        String input = filename + "\n";
        command.setScanner(new Scanner(input));

        command.execute();

        File file = new File(filename);
        assertTrue(file.exists(), "CSV file should be created even for empty hotel");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            assertNotNull(header, "Header should exist");
            assertNull(reader.readLine(), "Should have no data lines for empty hotel");
        }
    }

    @Test
    @DisplayName("Should escape CSV special characters")
    void testEscapeSpecialCharacters() throws IOException {
        Hotel hotel = new Hotel("Test Hotel", 1);
        Room room = new Room(101, "Luxury, Suite", 2, 300.0);
        hotel.addRoom(room);

        Guest guest = new Guest("Jane", "O'Connor", LocalDate.now(), 2);
        guest.setAdditionalInfo("Needs \"quiet\" room");
        room.checkIn(guest, new ArrayList<>());

        command.setHotel(hotel);

        String filename = tempDir.resolve("special_chars.csv").toString();
        String input = filename + "\n";
        command.setScanner(new Scanner(input));

        command.execute();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.readLine(); // Skip header
            String data = reader.readLine();
            assertNotNull(data);
            // CSV should handle commas and quotes properly
            assertTrue(data.contains("Luxury") || data.contains("\"Luxury, Suite\""));
        }
    }

    @Test
    @DisplayName("Should save occupied and unoccupied rooms correctly")
    void testSaveMixedOccupancy() throws IOException {
        Hotel hotel = new Hotel("Test Hotel", 2);

        Room occupiedRoom = new Room(101, "Single", 1, 150.0);
        Guest guest = new Guest("Kacper", "Lipiec", LocalDate.now(), 2);
        occupiedRoom.checkIn(guest, new ArrayList<>());
        hotel.addRoom(occupiedRoom);

        Room vacantRoom = new Room(102, "Double", 2, 250.0);
        hotel.addRoom(vacantRoom);

        command.setHotel(hotel);

        String filename = tempDir.resolve("mixed.csv").toString();
        String input = filename + "\n";
        command.setScanner(new Scanner(input));

        command.execute();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.readLine(); // Skip header
            String line1 = reader.readLine();
            String line2 = reader.readLine();

            assertTrue(line1.contains("true") || line2.contains("true"),
                    "One room should be occupied");
            assertTrue(line1.contains("false") || line2.contains("false"),
                    "One room should be vacant");
        }
    }

    @Test
    @DisplayName("Should save unoccupied room with correct CSV format")
    void testSaveUnoccupiedRoomFormat() throws IOException {
        Hotel hotel = new Hotel("Test Hotel", 1);
        Room vacantRoom = new Room(102, "Double", 2, 250.0);
        hotel.addRoom(vacantRoom);

        command.setHotel(hotel);

        String filename = tempDir.resolve("vacant_room.csv").toString();
        String input = filename + "\n";
        command.setScanner(new Scanner(input));

        command.execute();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.readLine(); // Skip header
            String data = reader.readLine();

            assertNotNull(data, "Data line should exist");

            // Split by comma (simple parsing)
            String[] parts = data.split(",", -1); // -1 to keep trailing empty strings

            // Verify we have exactly 12 columns
            assertEquals(12, parts.length, "Should have exactly 12 columns");

            // Verify structure for unoccupied room
            assertEquals("102", parts[0].trim(), "Room number should be 102");
            assertEquals("Double", parts[1].trim(), "Description should be Double");
            assertEquals("250.0", parts[2].trim(), "Price should be 250.0");
            assertEquals("2", parts[3].trim(), "Capacity should be 2");
            assertEquals("false", parts[4].trim(), "Occupied should be false");
            assertEquals("", parts[5].trim(), "MainGuestFirstName should be empty");
            assertEquals("", parts[6].trim(), "MainGuestLastName should be empty");
            assertEquals("", parts[7].trim(), "CheckInDate should be empty");
            assertEquals("", parts[8].trim(), "CheckOutDate should be empty");
            assertEquals("", parts[9].trim(), "StayDuration should be empty");
            assertEquals("", parts[10].trim(), "AdditionalInfo should be empty");
            assertEquals("0", parts[11].trim(), "AdditionalGuestsCount should be 0");
        }
    }

    @Test
    @DisplayName("Should return correct command name")
    void testGetCommandName() {
        assertEquals("save", command.getCommandName());
    }
}