package pl.agh.edu.hotel.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a hotel room.
 */
@Getter
public class Room {
    private final int roomNumber;
    private final String description;
    private final int capacity;
    private final double pricePerNight;
    private boolean occupied;
    private Guest mainGuest;
    private List<Guest> additionalGuests;

    /**
     * Creates a new room.
     * @param roomNumber unique room number
     * @param description room description (e.g., "Single", "Double")
     * @param capacity maximum number of guests
     * @param pricePerNight price per night in PLN
     */
    public Room(int roomNumber, String description, int capacity, double pricePerNight) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if (pricePerNight < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        this.roomNumber = roomNumber;
        this.description = description;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.occupied = false;
        this.additionalGuests = new ArrayList<>();
    }

    /**
     * Checks in a guest to this room.
     * @param mainGuest the main guest
     * @param additionalGuests list of additional guests
     * @throws IllegalStateException if room is already occupied
     * @throws IllegalArgumentException if too many guests
     */
    public void checkIn(Guest mainGuest, List<Guest> additionalGuests) {
        if (occupied) {
            throw new IllegalStateException("Room is already occupied");
        }
        if (mainGuest == null) {
            throw new IllegalArgumentException("Main guest cannot be null");
        }

        int totalGuests = 1 + (additionalGuests != null ? additionalGuests.size() : 0);
        if (totalGuests > capacity) {
            throw new IllegalArgumentException(
                    "Too many guests. Capacity: " + capacity + ", Requested: " + totalGuests);
        }

        this.mainGuest = mainGuest;
        this.additionalGuests = additionalGuests != null ?
                new ArrayList<>(additionalGuests) : new ArrayList<>();
        this.occupied = true;
    }

    /**
     * Checks out the current guest.
     * @throws IllegalStateException if room is not occupied
     */
    public void checkOut() {
        if (!occupied) {
            throw new IllegalStateException("Room is not occupied");
        }

        this.mainGuest = null;
        this.additionalGuests.clear();
        this.occupied = false;
    }

    /**
     * Returns an unmodifiable list of additional guests.
     * @return list of additional guests
     */
    public List<Guest> getAdditionalGuests() {
        return Collections.unmodifiableList(additionalGuests);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return roomNumber == room.roomNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", description='" + description + '\'' +
                ", capacity=" + capacity +
                ", pricePerNight=" + pricePerNight +
                ", occupied=" + occupied +
                '}';
    }
}