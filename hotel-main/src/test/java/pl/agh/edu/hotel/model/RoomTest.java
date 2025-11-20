package pl.agh.edu.hotel.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Room class.
 */
@DisplayName("Room Tests")
class RoomTest {

    private Room room;
    private Guest guest;

    @BeforeEach
    void setUp() {
        room = new Room(101, "Single", 1, 150.0);
        guest = new Guest("Kacper", "Lipiec", LocalDate.now(), 2);
    }

    @Test
    @DisplayName("Should create room with valid parameters")
    void testCreateRoom() {
        assertEquals(101, room.getRoomNumber());
        assertEquals("Single", room.getDescription());
        assertEquals(1, room.getCapacity());
        assertEquals(150.0, room.getPricePerNight());
        assertFalse(room.isOccupied());
    }

    @Test
    @DisplayName("Should throw exception for invalid capacity")
    void testInvalidCapacity() {
        assertThrows(IllegalArgumentException.class,
                () -> new Room(101, "Single", 0, 150.0));
        assertThrows(IllegalArgumentException.class,
                () -> new Room(101, "Single", -1, 150.0));
    }

    @Test
    @DisplayName("Should throw exception for negative price")
    void testNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Room(101, "Single", 1, -50.0));
    }

    @Test
    @DisplayName("Should check in guest successfully")
    void testCheckIn() {
        room.checkIn(guest, new ArrayList<>());

        assertTrue(room.isOccupied());
        assertEquals(guest, room.getMainGuest());
        assertTrue(room.getAdditionalGuests().isEmpty());
    }

    @Test
    @DisplayName("Should check in with additional guests")
    void testCheckInWithAdditionalGuests() {
        Room doubleRoom = new Room(201, "Double", 2, 250.0);
        Guest additionalGuest = new Guest("Jane", "Smith", LocalDate.now(), 2);
        List<Guest> additionalGuests = List.of(additionalGuest);

        doubleRoom.checkIn(guest, additionalGuests);

        assertTrue(doubleRoom.isOccupied());
        assertEquals(guest, doubleRoom.getMainGuest());
        assertEquals(1, doubleRoom.getAdditionalGuests().size());
        assertEquals(additionalGuest, doubleRoom.getAdditionalGuests().getFirst());
    }

    @Test
    @DisplayName("Should throw exception when checking in to occupied room")
    void testCheckInOccupiedRoom() {
        room.checkIn(guest, new ArrayList<>());

        Guest anotherGuest = new Guest("Kacper", "Lipiec", LocalDate.now(), 1);
        List<Guest> emptyList = new ArrayList<>();
        assertThrows(IllegalStateException.class,
                () -> room.checkIn(anotherGuest, emptyList));
    }

    @Test
    @DisplayName("Should throw exception for null main guest")
    void testCheckInNullGuest() {
        List<Guest> emptyList = new ArrayList<>();
        assertThrows(IllegalArgumentException.class,
                () -> room.checkIn(null, emptyList));
    }

    @Test
    @DisplayName("Should throw exception for too many guests")
    void testCheckInTooManyGuests() {
        Guest additional1 = new Guest("Kacper", "Lipiec", LocalDate.now(), 2);
        Guest additional2 = new Guest("Kenan", "Yildiz", LocalDate.now(), 2);
        List<Guest> additionalGuests = List.of(additional1, additional2);

        // Room capacity is 1, trying to add 3 guests total
        assertThrows(IllegalArgumentException.class,
                () -> room.checkIn(guest, additionalGuests));
    }

    @Test
    @DisplayName("Should handle null additional guests list")
    void testCheckInNullAdditionalGuests() {
        assertDoesNotThrow(() -> room.checkIn(guest, null));
        assertTrue(room.isOccupied());
        assertTrue(room.getAdditionalGuests().isEmpty());
    }

    @Test
    @DisplayName("Should check out successfully")
    void testCheckOut() {
        room.checkIn(guest, new ArrayList<>());
        room.checkOut();

        assertFalse(room.isOccupied());
        assertNull(room.getMainGuest());
        assertTrue(room.getAdditionalGuests().isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when checking out from unoccupied room")
    void testCheckOutUnoccupiedRoom() {
        assertThrows(IllegalStateException.class, () -> room.checkOut());
    }

    @Test
    @DisplayName("Should clear all guests on checkout")
    void testCheckOutClearsAllGuests() {
        Room doubleRoom = new Room(201, "Double", 2, 250.0);
        Guest additionalGuest = new Guest("Cristiano", "Ronaldo", LocalDate.now(), 2);
        List<Guest> additionalGuests = List.of(additionalGuest);

        doubleRoom.checkIn(guest, additionalGuests);
        doubleRoom.checkOut();

        assertFalse(doubleRoom.isOccupied());
        assertNull(doubleRoom.getMainGuest());
        assertTrue(doubleRoom.getAdditionalGuests().isEmpty());
    }

    @Test
    @DisplayName("Should return unmodifiable list of additional guests")
    void testGetAdditionalGuestsUnmodifiable() {
        Room doubleRoom = new Room(201, "Double", 2, 250.0);
        Guest additionalGuest = new Guest("Manuel", "Locatelli", LocalDate.now(), 2);
        List<Guest> additionalGuests = new ArrayList<>();
        additionalGuests.add(additionalGuest);

        doubleRoom.checkIn(guest, additionalGuests);
        List<Guest> retrievedGuests = doubleRoom.getAdditionalGuests();

        Guest newGuest = new Guest("Alexander", "Zverev", LocalDate.now(), 2);
        assertThrows(UnsupportedOperationException.class,
                () -> retrievedGuests.add(newGuest));
    }

    @Test
    @DisplayName("Should be equal if room numbers are equal")
    void testEquals() {
        Room room1 = new Room(101, "Single", 1, 150.0);
        Room room2 = new Room(101, "Double", 2, 250.0);

        assertEquals(room1, room2);
        assertEquals(room1.hashCode(), room2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal if room numbers differ")
    void testNotEquals() {
        Room room1 = new Room(101, "Single", 1, 150.0);
        Room room2 = new Room(102, "Single", 1, 150.0);

        assertNotEquals(room1, room2);
    }

    @Test
    @DisplayName("Should handle multiple check-in and check-out cycles")
    void testMultipleCycles() {
        // First cycle
        room.checkIn(guest, new ArrayList<>());
        assertTrue(room.isOccupied());
        room.checkOut();
        assertFalse(room.isOccupied());

        // Second cycle
        Guest newGuest = new Guest("Carlos", "Alcaraz", LocalDate.now(), 3);
        room.checkIn(newGuest, new ArrayList<>());
        assertTrue(room.isOccupied());
        assertEquals(newGuest, room.getMainGuest());
    }
}
