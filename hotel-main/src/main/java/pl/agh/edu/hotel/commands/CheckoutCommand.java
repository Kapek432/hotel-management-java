package pl.agh.edu.hotel.commands;

import pl.agh.edu.hotel.model.Guest;
import pl.agh.edu.hotel.model.Room;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Command to check out a guest from a room.
 */
public class CheckoutCommand extends Command {

    @Override
    public void execute() {
        System.out.print("Enter room number: ");
        String input = scanner.nextLine().trim();

        try {
            int roomNumber = Integer.parseInt(input);
            Room room = hotel.getRoom(roomNumber);

            if (room == null) {
                System.err.println("Error: Room " + roomNumber + " does not exist.");
                return;
            }

            if (!room.isOccupied()) {
                System.err.println("Error: Room " + roomNumber + " is not occupied.");
                return;
            }

            performCheckout(room);

        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid room number format.");
        }
    }

    private void performCheckout(Room room) {
        Guest mainGuest = room.getMainGuest();
        LocalDate checkInDate = mainGuest.getCheckInDate();
        LocalDate checkOutDate = LocalDate.now();

        long actualNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (actualNights < 1) {
            actualNights = 1; // Minimum 1 night charge
        }

        double totalAmount = actualNights * room.getPricePerNight();

        System.out.println("\n=== Checkout Summary ===");
        System.out.println("Room Number: " + room.getRoomNumber());
        System.out.println("Guest: " + mainGuest.getFullName());
        System.out.println("Check-in Date: " + checkInDate);
        System.out.println("Check-out Date: " + checkOutDate);
        System.out.println("Actual Stay: " + actualNights + " night(s)");
        System.out.println("Price per Night: " + room.getPricePerNight() + " PLN");
        System.out.println("----------------------------");
        System.out.println("TOTAL AMOUNT DUE: " + totalAmount + " PLN");
        System.out.println();

        room.checkOut();
        System.out.println("Checkout completed successfully. Room is now available.");
        System.out.println();
    }

    @Override
    public String getCommandName() {
        return "checkout";
    }
}
