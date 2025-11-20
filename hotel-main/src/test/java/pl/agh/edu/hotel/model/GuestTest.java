package pl.agh.edu.hotel.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for Guest class.
 */
@DisplayName("Guest Tests")
class GuestTest {

    private LocalDate checkInDate;

    @BeforeEach
    void setUp() {
        checkInDate = LocalDate.of(2025, 10, 26);
    }

    @Test
    @DisplayName("Should create guest with valid parameters")
    void testCreateGuest() {
        Guest guest = new Guest("Kacper", "Lipiec", checkInDate, 3);

        assertEquals("Kacper", guest.getFirstName());
        assertEquals("Lipiec", guest.getLastName());
        assertEquals("Kacper Lipiec", guest.getFullName());
        assertEquals(checkInDate, guest.getCheckInDate());
        assertEquals(3, guest.getStayDuration());
        assertEquals(checkInDate.plusDays(3), guest.getCheckOutDate());
    }

    @Test
    @DisplayName("Should trim whitespace from names")
    void testTrimNames() {
        Guest guest = new Guest("  Kacper  ", "  Lipiec  ", checkInDate, 2);

        assertEquals("Kacper", guest.getFirstName());
        assertEquals("Lipiec", guest.getLastName());
    }

    @Test
    @DisplayName("Should throw exception for empty first name")
    void testEmptyFirstName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Guest("", "Lipiec", checkInDate, 2));
        assertThrows(IllegalArgumentException.class,
                () -> new Guest("   ", "Lipiec", checkInDate, 2));
        assertThrows(IllegalArgumentException.class,
                () -> new Guest(null, "Lipiec", checkInDate, 2));
    }

    @Test
    @DisplayName("Should throw exception for empty last name")
    void testEmptyLastName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Guest("Kacper", "", checkInDate, 2));
        assertThrows(IllegalArgumentException.class,
                () -> new Guest("Kacper", "   ", checkInDate, 2));
        assertThrows(IllegalArgumentException.class,
                () -> new Guest("Kacper", null, checkInDate, 2));
    }

    @Test
    @DisplayName("Should throw exception for null check-in date")
    void testNullCheckInDate() {
        assertThrows(IllegalArgumentException.class,
                () -> new Guest("Kacper", "Lipiec", null, 2));
    }

    @Test
    @DisplayName("Should throw exception for invalid stay duration")
    void testInvalidStayDuration() {
        assertThrows(IllegalArgumentException.class,
                () -> new Guest("Kacper", "Lipiec", checkInDate, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Guest("Kacper", "Lipiec", checkInDate, -1));
    }

    @Test
    @DisplayName("Should calculate checkout date correctly")
    void testCheckOutDateCalculation() {
        Guest guest = new Guest("Kacper", "Lipiec", checkInDate, 5);

        LocalDate expectedCheckOut = checkInDate.plusDays(5);
        assertEquals(expectedCheckOut, guest.getCheckOutDate());
    }

    @Test
    @DisplayName("Should handle single night stay")
    void testSingleNightStay() {
        Guest guest = new Guest("Kacper", "Lipiec", checkInDate, 1);

        assertEquals(checkInDate.plusDays(1), guest.getCheckOutDate());
    }

    @Test
    @DisplayName("Should set and get additional info")
    void testAdditionalInfo() {
        Guest guest = new Guest("Kacper", "Lipiec", checkInDate, 2);

        assertNull(guest.getAdditionalInfo());

        guest.setAdditionalInfo("VIP Guest");
        assertEquals("VIP Guest", guest.getAdditionalInfo());
    }

    @Test
    @DisplayName("Should handle null additional info")
    void testNullAdditionalInfo() {
        Guest guest = new Guest("Kacper", "Lipiec", checkInDate, 2);

        assertDoesNotThrow(() -> guest.setAdditionalInfo(null));
        assertNull(guest.getAdditionalInfo());
    }

    @Test
    @DisplayName("Should be equal if all fields match")
    void testEquals() {
        Guest guest1 = new Guest("Kacper", "Lipiec", checkInDate, 3);
        Guest guest2 = new Guest("Kacper", "Lipiec", checkInDate, 3);

        assertEquals(guest1, guest2);
        assertEquals(guest1.hashCode(), guest2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal if names differ")
    void testNotEqualsDifferentNames() {
        Guest guest1 = new Guest("Kacper", "Lipiec", checkInDate, 3);
        Guest guest2 = new Guest("Jane", "Lipiec", checkInDate, 3);

        assertNotEquals(guest1, guest2);
    }

    @Test
    @DisplayName("Should not be equal if dates differ")
    void testNotEqualsDifferentDates() {
        Guest guest1 = new Guest("Kacper", "Lipiec", checkInDate, 3);
        Guest guest2 = new Guest("Kacper", "Lipiec", checkInDate.plusDays(1), 3);

        assertNotEquals(guest1, guest2);
    }

    @Test
    @DisplayName("Should not be equal if duration differs")
    void testNotEqualsDifferentDuration() {
        Guest guest1 = new Guest("Kacper", "Lipiec", checkInDate, 3);
        Guest guest2 = new Guest("Kacper", "Lipiec", checkInDate, 5);

        assertNotEquals(guest1, guest2);
    }

    @Test
    @DisplayName("Should concatenate first and last name")
    void testFullName() {
        Guest guest = new Guest("Kacper", "Lipiec", checkInDate, 2);

        assertEquals("Kacper Lipiec", guest.getFullName());
    }

    @Test
    @DisplayName("Should handle names with special characters")
    void testFullNameSpecialCharacters() {
        Guest guest = new Guest("Pierre-Emerick", "Aubameyang", checkInDate, 2);

        assertEquals("Pierre-Emerick Aubameyang", guest.getFullName());
    }

    @Test
    @DisplayName("Should create guest for long-term stay")
    void testLongTermStay() {
        Guest guest = new Guest("Kacper", "Lipiec", checkInDate, 30);

        assertEquals(30, guest.getStayDuration());
        assertEquals(checkInDate.plusDays(30), guest.getCheckOutDate());
    }

    @Test
    @DisplayName("Should handle guest with all features")
    void testCompleteGuest() {
        Guest guest = new Guest("Kacper", "Lipiec", checkInDate, 5);
        guest.setAdditionalInfo("Business trip");

        assertEquals("Kacper", guest.getFirstName());
        assertEquals("Lipiec", guest.getLastName());
        assertEquals("Kacper Lipiec", guest.getFullName());
        assertEquals(checkInDate, guest.getCheckInDate());
        assertEquals(5, guest.getStayDuration());
        assertEquals(checkInDate.plusDays(5), guest.getCheckOutDate());
        assertEquals("Business trip", guest.getAdditionalInfo());
    }
}
