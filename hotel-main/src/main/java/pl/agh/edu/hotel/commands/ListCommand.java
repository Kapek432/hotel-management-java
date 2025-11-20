package pl.agh.edu.hotel.commands;

import pl.agh.edu.hotel.model.Guest;
import pl.agh.edu.hotel.model.Room;

import java.time.format.DateTimeFormatter;

/**
 * Command to list all rooms with their occupancy status.
 */
public class ListCommand extends Command {

    @Override
    public void execute() {
        System.out.println("\n=== Hotel Room Status ===");
        System.out.println();

        int occupiedCount = 0;
        int totalRooms = 0;

        for (Integer roomNumber : hotel.getAllRoomNumbers()) {
            Room room = hotel.getRoom(roomNumber);
            if (room != null) {
                totalRooms++;
                displayRoomStatus(room);
                if (room.isOccupied()) {
                    occupiedCount++;
                }
            }
        }

        System.out.println("=========================");
        System.out.println("Total Rooms: " + totalRooms);
        System.out.println("Occupied: " + occupiedCount);
        System.out.println("Available: " + (totalRooms - occupiedCount));
        System.out.println();
    }

    private void displayRoomStatus(Room room) {
        System.out.printf("Room %d (%s) - %s%n",
                room.getRoomNumber(),
                room.getDescription(),
                room.isOccupied() ? "OCCUPIED" : "AVAILABLE");

        if (room.isOccupied() && room.getMainGuest() != null) {
            Guest mainGuest = room.getMainGuest();
            System.out.printf("  Guest: %s%n", mainGuest.getFullName());
            System.out.printf("  Check-in: %s%n",
                    mainGuest.getCheckInDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            System.out.printf("  Planned Checkout: %s%n",
                    mainGuest.getCheckOutDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

            if (room.getAdditionalGuests() != null && !room.getAdditionalGuests().isEmpty()) {
                System.out.printf("  Additional Guests: %d%n", room.getAdditionalGuests().size());
            }
        }
        System.out.println();
    }

    @Override
    public String getCommandName() {
        return "list";
    }
}
