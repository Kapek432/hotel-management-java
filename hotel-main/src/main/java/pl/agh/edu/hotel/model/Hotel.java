package pl.agh.edu.hotel.model;

import lombok.Getter;
import pl.agh.edu.hotel.utils.MyMap;

import java.util.List;

/**
 * Represents a hotel.
 */
@Getter
public class Hotel {
    private final MyMap<Integer, Room> rooms;
    private final String name;
    private final int floors;

    /**
     * Creates a new hotel.
     * @param name the hotel name
     * @param floors number of floors
     */
    public Hotel(String name, int floors) {
        this.name = name;
        this.floors = floors;
        this.rooms = new MyMap<>();
    }

    /**
     * Adds a room to the hotel.
     * @param room the room to add
     */
    public void addRoom(Room room) {
        rooms.put(room.getRoomNumber(), room);
    }

    /**
     * Gets a room by its number.
     * @param roomNumber the room number
     * @return the room, or null if not found
     */
    public Room getRoom(int roomNumber) {
        return rooms.get(roomNumber);
    }

    /**
     * Returns all room numbers in the hotel.
     * @return list of room numbers
     */
    public List<Integer> getAllRoomNumbers() {
        return rooms.keys();
    }

    /**
     * Removes a room from the hotel.
     * @param roomNumber the room number to remove
     */
    public void removeRoom(int roomNumber) {
        rooms.remove(roomNumber);
    }

    /**
     * Gets the total number of rooms in the hotel.
     * @return number of rooms
     */
    public int getRoomCount() {
        return rooms.keys().size();
    }

    /**
     * Counts occupied rooms.
     * @return number of occupied rooms
     */
    public int getOccupiedRoomCount() {
        int count = 0;
        for (Integer roomNumber : rooms.keys()) {
            Room room = rooms.get(roomNumber);
            if (room != null && room.isOccupied()) {
                count++;
            }
        }
        return count;
    }
}