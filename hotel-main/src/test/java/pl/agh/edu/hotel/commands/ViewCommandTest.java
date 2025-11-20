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
 * Test suite for ViewCommand
 */
class ViewCommandTest {

    private Hotel hotel;
    private ViewCommand command;
    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errorStream;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        hotel = new Hotel("Test Hotel", 2);
        Room room = new Room(101, "Single", 1, 150.0);
        hotel.addRoom(room);

        Guest guest = new Guest("Kacper", "Lipiec", LocalDate.now(), 3);
        room.checkIn(guest, new ArrayList<>());

        command = new ViewCommand();
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
    @DisplayName("Should display occupied room information")
    void testViewOccupiedRoom() {
        String input = "101\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("101"), "Should show room number");
        assertTrue(output.contains("Single"), "Should show room description");
        assertTrue(output.contains("OCCUPIED"), "Should show occupied status");
        assertTrue(output.contains("Kacper Lipiec") ||
                        (output.contains("Kacper") && output.contains("Lipiec")),
                "Should show guest name");
        assertTrue(output.contains("150"), "Should show price");
    }

    @Test
    @DisplayName("Should display available room information")
    void testViewAvailableRoom() {
        Room room = new Room(102, "Double", 2, 250.0);
        hotel.addRoom(room);

        String input = "102\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("102"), "Should show room number");
        assertTrue(output.contains("Double"), "Should show room description");
        assertTrue(output.contains("AVAILABLE"), "Should show available status");
        assertTrue(output.contains("250"), "Should show price");
    }

    @Test
    @DisplayName("Should handle non-existent room")
    void testViewNonExistentRoom() {
        String input = "999\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String errorOutput = errorStream.toString();
        assertTrue(errorOutput.contains("Error") || errorOutput.contains("does not exist"),
                "Should show error for non-existent room");
    }

    @Test
    @DisplayName("Should handle invalid room number format")
    void testViewInvalidRoomNumber() {
        String input = "abc\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String errorOutput = errorStream.toString();
        assertTrue(errorOutput.contains("Error") || errorOutput.contains("Invalid"),
                "Should show error for invalid format");
    }

    @Test
    @DisplayName("Should display room capacity")
    void testDisplayCapacity() {
        String input = "101\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Capacity") && output.contains("1"),
                "Should show room capacity");
    }

    @Test
    @DisplayName("Should display check-in and checkout dates")
    void testDisplayDates() {
        String input = "101\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Check-in"), "Should show check-in date");
        assertTrue(output.contains("Checkout") || output.contains("Check-out"),
                "Should show checkout date");
    }

    @Test
    @DisplayName("Should display stay duration")
    void testDisplayStayDuration() {
        String input = "101\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Stay Duration") || output.contains("Duration"),
                "Should show stay duration");
        assertTrue(output.contains("3"), "Should show 3 nights");
    }

    @Test
    @DisplayName("Should display additional info if present")
    void testDisplayAdditionalInfo() {
        Room room = hotel.getRoom(101);
        room.getMainGuest().setAdditionalInfo("VIP Guest");

        String input = "101\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("VIP Guest") || output.contains("Additional Info"),
                "Should show additional information");
    }

    @Test
    @DisplayName("Should display additional guests if present")
    void testDisplayAdditionalGuests() {
        Room room = new Room(103, "Suite", 3, 400.0);
        hotel.addRoom(room);

        Guest mainGuest = new Guest("Kacper", "Lipiec", LocalDate.now(), 2);
        ArrayList<Guest> additionalGuests = new ArrayList<>();
        additionalGuests.add(new Guest("Federico", "Gatti", LocalDate.now(), 2));
        additionalGuests.add(new Guest("Khephren", "Thuram", LocalDate.now(), 2));
        room.checkIn(mainGuest, additionalGuests);

        String input = "103\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Additional Guests") ||
                        (output.contains("Federico") && output.contains("Khephren")),
                "Should show additional guests");
    }

    @Test
    @DisplayName("Should return correct command name")
    void testGetCommandName() {
        assertEquals("view", command.getCommandName());
    }

    @Test
    @DisplayName("Should not show guest info for available rooms")
    void testNoGuestInfoForAvailableRoom() {
        Room room = new Room(102, "Double", 2, 250.0);
        hotel.addRoom(room);

        String input = "102\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String output = outputStream.toString();
        assertFalse(output.contains("Guest:") || output.contains("Check-in Date:"),
                "Should not show guest information for available rooms");
    }
}