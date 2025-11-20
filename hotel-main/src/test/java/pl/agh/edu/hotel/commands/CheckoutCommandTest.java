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
 * Test suite for CheckoutCommand
 */
class CheckoutCommandTest {

    private Hotel hotel;
    private CheckoutCommand command;
    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errorStream;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        hotel = new Hotel("Test Hotel", 2);
        Room room = new Room(101, "Single", 1, 150.0);
        hotel.addRoom(room);

        Guest guest = new Guest("Kacper", "Lipiec", LocalDate.now().minusDays(2), 3);
        room.checkIn(guest, new ArrayList<>());

        command = new CheckoutCommand();
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
    @DisplayName("Should check out guest successfully")
    void testCheckOutSuccess() {
        String input = "101\n";
        command.setScanner(new Scanner(input));

        command.execute();

        Room room = hotel.getRoom(101);
        assertFalse(room.isOccupied(), "Room should not be occupied after checkout");
        assertNull(room.getMainGuest(), "Main guest should be null after checkout");

        String output = outputStream.toString();
        assertTrue(output.contains("Checkout") || output.contains("successful") ||
                output.contains("completed"), "Output should contain checkout confirmation");
        assertTrue(output.contains("TOTAL AMOUNT"), "Output should show total amount");
    }

    @Test
    @DisplayName("Should calculate correct amount for 2 nights")
    void testCalculateAmount() {
        String input = "101\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("300"),
                "Output should contain total amount of 300 PLN (2 nights * 150 PLN)");
        assertTrue(output.contains("2 night"), "Output should show 2 nights");
    }

    @Test
    @DisplayName("Should handle unoccupied room")
    void testCheckOutUnoccupiedRoom() {
        Room room = new Room(102, "Double", 2, 250.0);
        hotel.addRoom(room);

        String input = "102\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String errorOutput = errorStream.toString();
        assertTrue(errorOutput.contains("Error") || errorOutput.contains("not occupied"),
                "Error should indicate room is not occupied");
    }

    @Test
    @DisplayName("Should handle non-existent room")
    void testCheckOutNonExistentRoom() {
        String input = "999\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String errorOutput = errorStream.toString();
        assertTrue(errorOutput.contains("Error") || errorOutput.contains("does not exist"),
                "Error should indicate room doesn't exist");
    }

    @Test
    @DisplayName("Should handle invalid room number format")
    void testCheckOutInvalidRoomNumber() {
        String input = "abc\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String errorOutput = errorStream.toString();
        assertTrue(errorOutput.contains("Error") || errorOutput.contains("Invalid"),
                "Error should indicate invalid format");
    }

    @Test
    @DisplayName("Should return correct command name")
    void testGetCommandName() {
        assertEquals("checkout", command.getCommandName());
    }

    @Test
    @DisplayName("Should charge minimum 1 night for same-day checkout")
    void testMinimumOneNightCharge() {
        Room room = new Room(103, "Single", 1, 100.0);
        hotel.addRoom(room);

        Guest guest = new Guest("Max", "Allegri", LocalDate.now(), 1);
        room.checkIn(guest, new ArrayList<>());

        String input = "103\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("100") || output.contains("1 night"),
                "Should charge for at least 1 night");
    }

    @Test
    @DisplayName("Should display checkout summary with dates")
    void testCheckoutSummary() {
        String input = "101\n";
        command.setScanner(new Scanner(input));

        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Checkout Summary") || output.contains("==="));
        assertTrue(output.contains("Room Number"));
        assertTrue(output.contains("Guest: Kacper Lipiec"));
        assertTrue(output.contains("Check-in Date"));
        assertTrue(output.contains("Check-out Date"));
        assertTrue(output.contains("Price per Night"));
    }
}