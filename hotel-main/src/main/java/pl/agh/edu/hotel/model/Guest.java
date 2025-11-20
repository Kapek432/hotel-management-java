package pl.agh.edu.hotel.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a guest staying at the hotel.
 */
@Getter
@Setter
public class Guest {
    private final String firstName;
    private final String lastName;
    private final LocalDate checkInDate;
    private final int stayDuration;
    private String additionalInfo;

    /**
     * Creates a new guest.
     * @param firstName guest's first name
     * @param lastName guest's last name
     * @param checkInDate check-in date
     * @param stayDuration duration of stay in nights
     */
    public Guest(String firstName, String lastName, LocalDate checkInDate, int stayDuration) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (checkInDate == null) {
            throw new IllegalArgumentException("Check-in date cannot be null");
        }
        if (stayDuration <= 0) {
            throw new IllegalArgumentException("Stay duration must be positive");
        }

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.checkInDate = checkInDate;
        this.stayDuration = stayDuration;
    }

    /**
     * Returns the full name of the guest.
     * @return full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Calculates the check-out date based on check-in date and stay duration.
     * @return check-out date
     */
    public LocalDate getCheckOutDate() {
        return checkInDate.plusDays(stayDuration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return stayDuration == guest.stayDuration &&
                firstName.equals(guest.firstName) &&
                lastName.equals(guest.lastName) &&
                checkInDate.equals(guest.checkInDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, checkInDate, stayDuration);
    }

    @Override
    public String toString() {
        return "Guest{" +
                "name='" + getFullName() + '\'' +
                ", checkInDate=" + checkInDate +
                ", stayDuration=" + stayDuration +
                '}';
    }
}