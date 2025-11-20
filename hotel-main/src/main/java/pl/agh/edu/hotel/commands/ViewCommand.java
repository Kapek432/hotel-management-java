package pl.agh.edu.hotel.commands;

import pl.agh.edu.hotel.model.Guest;
import pl.agh.edu.hotel.model.Room;

import java.time.format.DateTimeFormatter;

/**
 * Command to view detailed information about a specific room.
 */
public class ViewCommand extends Command {

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

            displayRoomInfo(room);

        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid room number format.");
        }
    }

    private void displayRoomInfo(Room room) {
        System.out.println("\n=== Room Information ===");
        System.out.println("Room Number: " + room.getRoomNumber());
        System.out.println("Description: " + room.getDescription());
        System.out.println("Capacity: " + room.getCapacity() + " person(s)");
        System.out.println("Price per Night: " + room.getPricePerNight() + " PLN");
        System.out.println("Status: " + (room.isOccupied() ? "OCCUPIED" : "AVAILABLE"));

        if (room.isOccupied() && room.getMainGuest() != null) {
            Guest mainGuest = room.getMainGuest();
            System.out.println("\nGuest Information:");
            System.out.println("Main Guest: " + mainGuest.getFullName());
            System.out.println("Check-in Date: " +
                    mainGuest.getCheckInDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            System.out.println("Planned Checkout: " +
                    mainGuest.getCheckOutDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            System.out.println("Stay Duration: " + mainGuest.getStayDuration() + " night(s)");

            if (mainGuest.getAdditionalInfo() != null && !mainGuest.getAdditionalInfo().isEmpty()) {
                System.out.println("Additional Info: " + mainGuest.getAdditionalInfo());
            }

            if (room.getAdditionalGuests() != null && !room.getAdditionalGuests().isEmpty()) {
                System.out.println("\nAdditional Guests:");
                for (Guest guest : room.getAdditionalGuests()) {
                    System.out.println("  - " + guest.getFullName());
                }
            }
        }
        System.out.println();
    }

    @Override
    public String getCommandName() {
        return "view";
    }
}
