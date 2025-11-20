package pl.agh.edu.hotel.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Hotel class.
 */
@DisplayName("Hotel Tests")
class HotelTest {

    private Hotel hotel;

    @BeforeEach
    void setUp() {
        hotel = new Hotel("Test Hotel", 2);
    }

    @Test
    @DisplayName("Should add room to hotel")
    void testAddRoom() {
        Room room = new Room(101, "Single", 1, 150.0);
        hotel.addRoom(room);

        assertEquals(1, hotel.getRoomCount());
        assertNotNull(hotel.getRoom(101));
    }

    @Test
    @DisplayName("Should retrieve room by number")
    void testGetRoom() {
        Room room = new Room(201, "Double", 2, 250.0);
        hotel.addRoom(room);

        Room retrieved = hotel.getRoom(201);

        assertNotNull(retrieved);
        assertEquals(201, retrieved.getRoomNumber());
        assertEquals("Double", retrieved.getDescription());
    }

    @Test
    @DisplayName("Should return null for non-existent room")
    void testGetNonExistentRoom() {
        Room room = hotel.getRoom(999);

        assertNull(room);
    }

    @Test
    @DisplayName("Should remove room from hotel")
    void testRemoveRoom() {
        Room room = new Room(101, "Single", 1, 150.0);
        hotel.addRoom(room);

        hotel.removeRoom(101);

        assertEquals(0, hotel.getRoomCount());
        assertNull(hotel.getRoom(101));
    }

    @Test
    @DisplayName("Should return all room numbers")
    void testGetAllRoomNumbers() {
        hotel.addRoom(new Room(101, "Single", 1, 150.0));
        hotel.addRoom(new Room(102, "Double", 2, 250.0));
        hotel.addRoom(new Room(201, "Suite", 4, 500.0));

        var roomNumbers = hotel.getAllRoomNumbers();

        assertEquals(3, roomNumbers.size());
        assertTrue(roomNumbers.contains(101));
        assertTrue(roomNumbers.contains(102));
        assertTrue(roomNumbers.contains(201));
    }

    @Test
    @DisplayName("Should count occupied rooms correctly")
    void testGetOccupiedRoomCount() {
        Room room1 = new Room(101, "Single", 1, 150.0);
        Room room2 = new Room(102, "Double", 2, 250.0);
        Room room3 = new Room(103, "Triple", 3, 300.0);

        hotel.addRoom(room1);
        hotel.addRoom(room2);
        hotel.addRoom(room3);

        Guest guest1 = new Guest("Kacper", "Lipiec", LocalDate.now(), 2);
        Guest guest2 = new Guest("Pavel", "Nedved", LocalDate.now(), 3);

        room1.checkIn(guest1, new ArrayList<>());
        room2.checkIn(guest2, new ArrayList<>());

        int occupiedCount = hotel.getOccupiedRoomCount();

        assertEquals(2, occupiedCount);
    }

    @Test
    @DisplayName("Should return hotel properties (name and floors)")
    void testGetHotelProperties() {
        assertEquals("Test Hotel", hotel.getName());
        assertEquals(2, hotel.getFloors());
    }

    @Test
    @DisplayName("Should handle empty hotel correctly")
    void testEmptyHotel() {
        assertEquals(0, hotel.getRoomCount());
        assertEquals(0, hotel.getOccupiedRoomCount());
        assertTrue(hotel.getAllRoomNumbers().isEmpty());
    }

    @Test
    @DisplayName("Should handle multiple room additions")
    void testMultipleRoomAdditions() {
        Room room1 = new Room(101, "Single", 1, 150.0);
        Room room2 = new Room(102, "Double", 2, 250.0);
        Room room3 = new Room(103, "Suite", 4, 500.0);

        hotel.addRoom(room1);
        hotel.addRoom(room2);
        hotel.addRoom(room3);

        assertEquals(3, hotel.getRoomCount());
        assertNotNull(hotel.getRoom(101));
        assertNotNull(hotel.getRoom(102));
        assertNotNull(hotel.getRoom(103));
    }

    @Test
    @DisplayName("Should replace room with same number")
    void testReplaceRoom() {
        Room room1 = new Room(101, "Single", 1, 150.0);
        Room room2 = new Room(101, "Double", 2, 250.0);

        hotel.addRoom(room1);
        hotel.addRoom(room2);

        assertEquals(1, hotel.getRoomCount());
        Room retrieved = hotel.getRoom(101);
        assertEquals("Double", retrieved.getDescription());
        assertEquals(2, retrieved.getCapacity());
    }

    @Test
    @DisplayName("Should handle removing non-existent room")
    void testRemoveNonExistentRoom() {
        hotel.removeRoom(999);

        assertEquals(0, hotel.getRoomCount());
    }

    @Test
    @DisplayName("Should maintain correct count after add and remove operations")
    void testRoomCountAfterOperations() {
        Room room1 = new Room(101, "Single", 1, 150.0);
        Room room2 = new Room(102, "Double", 2, 250.0);
        Room room3 = new Room(103, "Triple", 3, 300.0);

        hotel.addRoom(room1);
        hotel.addRoom(room2);
        hotel.addRoom(room3);
        assertEquals(3, hotel.getRoomCount());

        hotel.removeRoom(102);
        assertEquals(2, hotel.getRoomCount());

        hotel.removeRoom(101);
        assertEquals(1, hotel.getRoomCount());

        hotel.removeRoom(103);

        assertEquals(0, hotel.getRoomCount());
        assertTrue(hotel.getAllRoomNumbers().isEmpty());
    }

    @Test
    @DisplayName("Should track occupancy changes correctly")
    void testOccupancyTracking() {
        Room room1 = new Room(101, "Single", 1, 150.0);
        Room room2 = new Room(102, "Double", 2, 250.0);
        hotel.addRoom(room1);
        hotel.addRoom(room2);

        Guest guest1 = new Guest("Ben", "Shelton", LocalDate.now(), 2);
        Guest guest2 = new Guest("Jannik", "Sinner", LocalDate.now(), 3);

        assertEquals(0, hotel.getOccupiedRoomCount());

        room1.checkIn(guest1, new ArrayList<>());
        assertEquals(1, hotel.getOccupiedRoomCount());

        room2.checkIn(guest2, new ArrayList<>());
        assertEquals(2, hotel.getOccupiedRoomCount());

        room1.checkOut();
        assertEquals(1, hotel.getOccupiedRoomCount());

        room2.checkOut();

        assertEquals(0, hotel.getOccupiedRoomCount());
    }
}