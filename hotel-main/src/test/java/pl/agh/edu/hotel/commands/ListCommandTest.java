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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for ListCommand
 */
class ListCommandTest {

    private ListCommand command;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        Hotel hotel = new Hotel("Test Hotel", 2);

        Room room1 = new Room(101, "Single", 1, 150.0);
        Room room2 = new Room(102, "Double", 2, 250.0);
        hotel.addRoom(room1);
        hotel.addRoom(room2);

        Guest guest = new Guest("Kacper", "Lipiec", LocalDate.now(), 3);
        room1.checkIn(guest, new ArrayList<>());

        command = new ListCommand();
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
    @DisplayName("Should list all rooms with status")
    void testListAllRooms() {
        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("101"), "Output should contain room 101");
        assertTrue(output.contains("102"), "Output should contain room 102");
        assertTrue(output.contains("OCCUPIED"), "Output should show occupied status");
        assertTrue(output.contains("AVAILABLE"), "Output should show available status");
        assertTrue(output.contains("Kacper Lipiec") || output.contains("Kacper") && output.contains("Lipiec"),
                "Output should contain guest name");
    }

    @Test
    @DisplayName("Should show correct totals")
    void testShowTotals() {
        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Total Rooms: 2") || output.contains("Total: 2"),
                "Should show 2 total rooms");
        assertTrue(output.contains("Occupied: 1"), "Should show 1 occupied room");
        assertTrue(output.contains("Available: 1"), "Should show 1 available room");
    }

    @Test
    @DisplayName("Should display room descriptions")
    void testDisplayRoomDescriptions() {
        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Single"), "Should show Single room description");
        assertTrue(output.contains("Double"), "Should show Double room description");
    }

    @Test
    @DisplayName("Should display check-in and checkout dates for occupied rooms")
    void testDisplayDatesForOccupiedRooms() {
        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Check-in"), "Should show check-in information");
        assertTrue(output.contains("Checkout") || output.contains("Check-out"),
                "Should show checkout information");
    }

    @Test
    @DisplayName("Should return correct command name")
    void testGetCommandName() {
        assertEquals("list", command.getCommandName());
    }

    @Test
    @DisplayName("Should handle hotel with no rooms")
    void testListEmptyHotel() {
        Hotel emptyHotel = new Hotel("Empty Hotel", 0);
        command.setHotel(emptyHotel);

        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Total Rooms: 0") || output.contains("Total: 0"),
                "Should show 0 total rooms");
    }

    @Test
    @DisplayName("Should display additional guests count if present")
    void testDisplayAdditionalGuestsCount() {
        Hotel hotel = new Hotel("Test Hotel", 1);
        Room room = new Room(103, "Suite", 3, 400.0);
        hotel.addRoom(room);

        Guest mainGuest = new Guest("Kacper", "Lipiec", LocalDate.now(), 2);
        ArrayList<Guest> additionalGuests = new ArrayList<>();
        additionalGuests.add(new Guest("Daniil", "Medvedev", LocalDate.now(), 2));
        room.checkIn(mainGuest, additionalGuests);

        command.setHotel(hotel);
        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Additional Guests") || output.contains("1"),
                "Should show additional guests information");
    }
}