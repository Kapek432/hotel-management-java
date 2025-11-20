package pl.agh.edu.hotel.commands;

import pl.agh.edu.hotel.model.Guest;
import pl.agh.edu.hotel.model.Room;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Command to check in a guest to a room.
 */
public class CheckinCommand extends Command {

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

            if (room.isOccupied()) {
                System.err.println("Error: Room " + roomNumber + " is already occupied.");
                return;
            }

            performCheckin(room);

        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid room number format.");
        }
    }

    private void performCheckin(Room room) {
        System.out.print("Enter main guest first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter main guest last name: ");
        String lastName = scanner.nextLine().trim();

        LocalDate checkInDate = getCheckInDate();

        System.out.print("Enter stay duration (number of nights): ");
        int stayDuration;
        try {
            stayDuration = Integer.parseInt(scanner.nextLine().trim());
            if (stayDuration <= 0) {
                System.err.println("Error: Stay duration must be positive. Using 1 night.");
                stayDuration = 1;
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid duration. Using 1 night.");
            stayDuration = 1;
        }

        System.out.print("Enter additional information (optional, press Enter to skip): ");
        String additionalInfo = scanner.nextLine().trim();

        Guest mainGuest = new Guest(firstName, lastName, checkInDate, stayDuration);
        if (!additionalInfo.isEmpty()) {
            mainGuest.setAdditionalInfo(additionalInfo);
        }

        List<Guest> additionalGuests = new ArrayList<>();
        if (room.getCapacity() > 1) {
            System.out.print("Number of additional guests (0-" + (room.getCapacity() - 1) + "): ");
            try {
                int additionalCount = Integer.parseInt(scanner.nextLine().trim());
                if (additionalCount > room.getCapacity() - 1) {
                    System.err.println("Error: Too many additional guests. Limiting to " + (room.getCapacity() - 1) + ".");
                }
                additionalCount = Math.min(additionalCount, room.getCapacity() - 1);

                for (int i = 0; i < additionalCount; i++) {
                    System.out.print("Additional guest " + (i + 1) + " first name: ");
                    String addFirstName = scanner.nextLine().trim();
                    System.out.print("Additional guest " + (i + 1) + " last name: ");
                    String addLastName = scanner.nextLine().trim();
                    additionalGuests.add(new Guest(addFirstName, addLastName, checkInDate, stayDuration));
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid number. No additional guests added.");
            }
        }

        room.checkIn(mainGuest, additionalGuests);

        System.out.println("\nCheck-in successful!");
        System.out.println("Room: " + room.getRoomNumber());
        System.out.println("Guest: " + mainGuest.getFullName());
        System.out.println("Check-in: " + checkInDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println("Check-out: " + mainGuest.getCheckOutDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println("Total guests: " + (1 + additionalGuests.size()) + "/" + room.getCapacity());
        System.out.println();
    }

    private LocalDate getCheckInDate() {
        System.out.print("Enter check-in date (YYYY-MM-DD, press Enter for today): ");
        String dateInput = scanner.nextLine().trim();

        if (dateInput.isEmpty()) {
            return LocalDate.now();
        }

        try {
            return LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format. Using today's date.");
            return LocalDate.now();
        }
    }

    @Override
    public String getCommandName() {
        return "checkin";
    }
}
