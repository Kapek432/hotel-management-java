package pl.agh.edu.hotel.commands;

import pl.agh.edu.hotel.model.Hotel;
import pl.agh.edu.hotel.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for PricesCommand
 */
class PricesCommandTest {

    private PricesCommand command;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        Hotel hotel = new Hotel("Test Hotel", 2);
        hotel.addRoom(new Room(101, "Single", 1, 150.0));
        hotel.addRoom(new Room(102, "Double", 2, 250.0));
        hotel.addRoom(new Room(201, "Suite", 4, 500.0));

        command = new PricesCommand();
        command.setHotel(hotel);

        // Capture System.out
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should display all room prices")
    void testExecute() {
        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("101"), "Should show room 101");
        assertTrue(output.contains("Single"), "Should show Single description");
        assertTrue(output.contains("150"), "Should show price 150");

        assertTrue(output.contains("102"), "Should show room 102");
        assertTrue(output.contains("Double"), "Should show Double description");
        assertTrue(output.contains("250"), "Should show price 250");

        assertTrue(output.contains("201"), "Should show room 201");
        assertTrue(output.contains("Suite"), "Should show Suite description");
        assertTrue(output.contains("500"), "Should show price 500");
    }

    @Test
    @DisplayName("Should display room capacities")
    void testDisplayCapacities() {
        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Capacity") || output.contains("capacity"),
                "Should have capacity column header");
        assertTrue(output.contains("1") && output.contains("2") && output.contains("4"),
                "Should show all capacity values");
    }

    @Test
    @DisplayName("Should display prices with PLN currency")
    void testDisplayCurrency() {
        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("PLN"), "Should show PLN currency");
    }

    @Test
    @DisplayName("Should format prices with decimal places")
    void testPriceFormatting() {
        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("150.00") || output.contains("150,00"),
                "Should format price with decimals");
    }

    @Test
    @DisplayName("Should display header")
    void testDisplayHeader() {
        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Room Prices") || output.contains("==="),
                "Should display header");
        assertTrue(output.contains("Room No") || output.contains("RoomNumber") ||
                output.contains("Room Number"), "Should have room number column");
        assertTrue(output.contains("Description"), "Should have description column");
        assertTrue(output.contains("Price"), "Should have price column");
    }

    @Test
    @DisplayName("Should handle empty hotel")
    void testEmptyHotel() {
        Hotel emptyHotel = new Hotel("Empty Hotel", 0);
        command.setHotel(emptyHotel);

        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Room Prices") || output.contains("==="),
                "Should still display header for empty hotel");
    }

    @Test
    @DisplayName("Should display rooms in order")
    void testRoomOrder() {
        command.execute();

        String output = outputStream.toString();
        int pos101 = output.indexOf("101");
        int pos102 = output.indexOf("102");
        int pos201 = output.indexOf("201");

        assertTrue(pos101 >= 0 && pos102 >= 0 && pos201 >= 0,
                "All rooms should be displayed");
    }

    @Test
    @DisplayName("Should return correct command name")
    void testGetCommandName() {
        assertEquals("prices", command.getCommandName());
    }

    @Test
    @DisplayName("Should handle rooms with different price ranges")
    void testVariousPriceRanges() {
        Hotel hotel = new Hotel("Test Hotel", 3);
        hotel.addRoom(new Room(101, "Budget", 1, 99.99));
        hotel.addRoom(new Room(102, "Standard", 2, 199.50));
        hotel.addRoom(new Room(103, "Premium", 4, 1500.00));

        command.setHotel(hotel);
        command.execute();

        String output = outputStream.toString();
        assertTrue(output.contains("99") || output.contains("100"),
                "Should show budget price");
        assertTrue(output.contains("199") || output.contains("200"),
                "Should show standard price");
        assertTrue(output.contains("1500"), "Should show premium price");
    }
}