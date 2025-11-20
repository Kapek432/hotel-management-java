package pl.agh.edu.hotel.commands;

import pl.agh.edu.hotel.model.Guest;
import pl.agh.edu.hotel.model.Hotel;
import pl.agh.edu.hotel.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for CheckinCommand
 */
class CheckinCommandTest {

    private Hotel hotel;
    private CheckinCommand command;
    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errorStream;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        hotel = new Hotel("Test Hotel", 2);
        hotel.addRoom(new Room(101, "Single", 1, 150.0));
        hotel.addRoom(new Room(102, "Double", 2, 250.0));

        command = new CheckinCommand();
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
    @DisplayName("Should check in guest successfully")
    void testCheckInSuccess() {
        String input = "101\nKacper\nLipiec\n\n3\n\n0\n";
        command.setScanner(new Scanner(input));

        command.execute();

        Room room = hotel.getRoom(101);
        assertTrue(room.isOccupied(), "Room should be occupied after check-in");
        assertNotNull(room.getMainGuest(), "Room should have a main guest");
        assertEquals("Kacper", room.getMainGuest().getFirstName());
        assertEquals("Lipiec", room.getMainGuest().getLastName());
        assertEquals(3, room.getMainGuest().getStayDuration());

        String output = outputStream.toString();
        assertTrue(output.contains("successful") || output.contains("Check-in"),
                "Output should contain success message");
    }

    @Test
    @DisplayName("Should check in with additional guests")
    void testCheckInWithAdditionalGuests() {
        String input = "102\nKacper\nLipiec\n\n3\n\n1\nPierre\nKalulu\n";
        command.setScanner(new Scanner(input));

        command.execute();

        Room room = hotel.getRoom(102);
        assertTrue(room.isOccupied(), "Room should be occupied");
        assertNotNull(room.getAdditionalGuests(), "Additional guests list should not be null");
        assertEquals(1, room.getAdditionalGuests().size(), "Should have 1 additional guest");
        assertEquals("Pierre", room.getAdditionalGuests().getFirst().getFirstName());
        assertEquals("Kalulu", room.getAdditionalGuests().getFirst().getLastName());
    }

    @Test
    @DisplayName("Should handle occupied room")
    void testCheckInOccupiedRoom() {
        Room room = hotel.getRoom(101);
        Guest guest = new Guest("Cristiano", "Ronaldo", LocalDate.now(), 2);
        room.checkIn(guest, new ArrayList<>());

        String input = "101\nKacper\nLipiec\n\n3\n\n0\n";
        command.setScanner(new Scanner(input));

        command.execute();

        assertEquals("Cristiano", room.getMainGuest().getFirstName(),
                "Original guest should remain after failed check-in");

        String errorOutput = errorStream.toString();
        assertTrue(errorOutput.contains("Error") || errorOutput.contains("occupied"),
                "Error message should indicate room is occupied");
    }

    @Test
    @DisplayName("Should handle non-existent room")
    void testCheckInNonExistentRoom() {
        String input = "999\nKacper\nLipiec\n\n3\n\n0\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String errorOutput = errorStream.toString();
        assertTrue(errorOutput.contains("Error") || errorOutput.contains("does not exist"),
                "Error message should indicate room doesn't exist");
    }

    @Test
    @DisplayName("Should handle invalid room number format")
    void testCheckInInvalidRoomNumber() {
        String input = "abc\nKacper\nLipiec\n\n3\n\n0\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String errorOutput = errorStream.toString();
        assertTrue(errorOutput.contains("Error") || errorOutput.contains("Invalid"),
                "Error message should indicate invalid format");
    }

    @Test
    @DisplayName("Should return correct command name")
    void testGetCommandName() {
        assertEquals("checkin", command.getCommandName());
    }

    @Test
    @DisplayName("Should handle custom check-in date")
    void testCheckInWithCustomDate() {
        String input = "101\nKacper\nLipiec\n2025-11-01\n3\n\n0\n";
        command.setScanner(new Scanner(input));

        command.execute();

        Room room = hotel.getRoom(101);
        assertTrue(room.isOccupied());
        assertEquals(LocalDate.of(2025, 11, 1), room.getMainGuest().getCheckInDate());
    }

    @Test
    @DisplayName("Should use today's date when empty input")
    void testCheckInWithTodayDate() {
        String input = "101\nKacper\nLipiec\n\n3\n\n0\n";
        command.setScanner(new Scanner(input));

        command.execute();

        Room room = hotel.getRoom(101);
        assertTrue(room.isOccupied());
        assertEquals(LocalDate.now(), room.getMainGuest().getCheckInDate());
    }
}