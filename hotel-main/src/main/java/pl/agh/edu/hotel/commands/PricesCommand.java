package pl.agh.edu.hotel.commands;

import pl.agh.edu.hotel.model.Room;

/**
 * Command to list all rooms with their prices.
 */
public class PricesCommand extends Command {

    @Override
    public void execute() {
        System.out.println("\n=== Room Prices ===");
        System.out.println("Room No. | Description | Capacity | Price/Night");
        System.out.println("------------------------------------------------");

        for (Integer roomNumber : hotel.getAllRoomNumbers()) {
            Room room = hotel.getRoom(roomNumber);
            if (room != null) {
                System.out.printf("%8d | %-11s | %8d | %10.2f PLN%n",
                        room.getRoomNumber(),
                        room.getDescription(),
                        room.getCapacity(),
                        room.getPricePerNight());
            }
        }
        System.out.println();
    }

    @Override
    public String getCommandName() {
        return "prices";
    }
}
