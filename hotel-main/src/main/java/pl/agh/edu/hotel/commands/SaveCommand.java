package pl.agh.edu.hotel.commands;

import pl.agh.edu.hotel.model.Guest;
import pl.agh.edu.hotel.model.Room;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Command to save the current hotel state to a CSV file.
 */
public class SaveCommand extends Command {

    private static final String DEFAULT_FILENAME = "hotel_state.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void execute() {
        System.out.print("Enter filename (press Enter for default 'hotel_state.csv'): ");
        String filename = scanner.nextLine().trim();

        if (filename.isEmpty()) {
            filename = DEFAULT_FILENAME;
        }

        if (!filename.endsWith(".csv")) {
            filename += ".csv";
        }

        try {
            saveToCSV(filename);
            System.out.println("Hotel state saved successfully to: " + filename);
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
            System.out.println();
        }
    }

    private void saveToCSV(String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.append("RoomNumber,Description,Price,Capacity,Occupied,");
            writer.append("MainGuestFirstName,MainGuestLastName,CheckInDate,CheckOutDate,");
            writer.append("StayDuration,AdditionalInfo,AdditionalGuestsCount\n");

            for (Integer roomNumber : hotel.getAllRoomNumbers()) {
                Room room = hotel.getRoom(roomNumber);
                if (room != null) {
                    writeRoomToCSV(writer, room);
                }
            }
        }
    }

    private void writeRoomToCSV(FileWriter writer, Room room) throws IOException {
        writer.append(String.valueOf(room.getRoomNumber())).append(',');
        writer.append(escapeCsvValue(room.getDescription())).append(',');
        writer.append(String.valueOf(room.getPricePerNight())).append(',');
        writer.append(String.valueOf(room.getCapacity())).append(',');
        writer.append(String.valueOf(room.isOccupied())).append(',');

        if (room.isOccupied() && room.getMainGuest() != null) {
            Guest mainGuest = room.getMainGuest();
            writer.append(escapeCsvValue(mainGuest.getFirstName())).append(',');
            writer.append(escapeCsvValue(mainGuest.getLastName())).append(',');
            writer.append(mainGuest.getCheckInDate().format(DATE_FORMATTER)).append(',');
            writer.append(mainGuest.getCheckOutDate().format(DATE_FORMATTER)).append(',');
            writer.append(String.valueOf(mainGuest.getStayDuration())).append(',');
            writer.append(escapeCsvValue(mainGuest.getAdditionalInfo())).append(',');

            int additionalGuestsCount = room.getAdditionalGuests() != null ?
                    room.getAdditionalGuests().size() : 0;
            writer.append(String.valueOf(additionalGuestsCount));
        } else {
            // Empty fields for unoccupied rooms
            // Format: MainGuestFirstName,MainGuestLastName,CheckInDate,CheckOutDate,StayDuration,AdditionalInfo,AdditionalGuestsCount
            // That's 7 fields, so we need: ,,,,,,0
            writer.append(",,,,,,0");
        }

        writer.append('\n');
    }

    private String escapeCsvValue(String value) {
        if (value == null) {
            return "";
        }
        // Escape quotes and wrap in quotes if contains comma or quote
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    @Override
    public String getCommandName() {
        return "save";
    }
}