package pl.agh.edu.hotel.commands;

import pl.agh.edu.hotel.model.Guest;
import pl.agh.edu.hotel.model.Room;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Command to load hotel configuration from a CSV file.
 */
public class LoadCommand extends Command {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void execute() {
        System.out.print("Enter filename to load: ");
        String filename = scanner.nextLine().trim();

        if (filename.isEmpty()) {
            System.err.println("Error: Filename cannot be empty.");
            return;
        }

        if (!filename.endsWith(".csv")) {
            filename += ".csv";
        }

        try {
            loadFromCSV(filename);
            System.out.println("Hotel state loaded successfully from: " + filename);
            System.out.println();
        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
            System.out.println();
        } catch (Exception e) {
            System.err.println("Error parsing file: " + e.getMessage());
            System.out.println();
        }
    }

    private void loadFromCSV(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine(); // Skip header

            int loadedRooms = 0;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    loadRoomFromCSV(line);
                    loadedRooms++;
                } catch (Exception e) {
                    System.err.println("Error parsing line: " + line);
                    System.err.println("Error: " + e.getMessage());
                }
            }

            System.out.println("Loaded " + loadedRooms + " rooms from file.");
        }
    }

    private void loadRoomFromCSV(String line) {
        String[] parts = parseCsvLine(line);

        // CSV Format: RoomNumber,Description,Price,Capacity,Occupied,MainGuestFirstName,MainGuestLastName,
        //             CheckInDate,CheckOutDate,StayDuration,AdditionalInfo,AdditionalGuestsCount (12 columns)

        if (parts.length < 12) {
            throw new IllegalArgumentException("Invalid CSV format: expected 12 columns, got " + parts.length);
        }

        int roomNumber = Integer.parseInt(parts[0].trim());
        String description = parts[1].trim();
        double price = Double.parseDouble(parts[2].trim());
        int capacity = Integer.parseInt(parts[3].trim());
        boolean occupied = Boolean.parseBoolean(parts[4].trim());

        Room room = hotel.getRoom(roomNumber);

        if (room == null) {
            room = new Room(roomNumber, description, capacity, price);
            hotel.addRoom(room);
        } else {
            if (room.isOccupied()) {
                room.checkOut();
            }
        }

        if (occupied) {
            String firstName = parts[5].trim();
            String lastName = parts[6].trim();

            if (!firstName.isEmpty() && !lastName.isEmpty()) {
                String checkInDateStr = parts[7].trim();
                String stayDurationStr = parts[9].trim();
                String additionalInfo = parts[10].trim();
                String additionalGuestsCountStr = parts[11].trim();

                LocalDate checkInDate = LocalDate.parse(checkInDateStr, DATE_FORMATTER);
                int stayDuration = Integer.parseInt(stayDurationStr);

                Guest mainGuest = new Guest(firstName, lastName, checkInDate, stayDuration);
                if (!additionalInfo.isEmpty()) {
                    mainGuest.setAdditionalInfo(additionalInfo);
                }

                List<Guest> additionalGuests = new ArrayList<>();
                int additionalGuestsCount = Integer.parseInt(additionalGuestsCountStr);

                // Create placeholder additional guests (since we don't have their details in CSV)
                for (int i = 0; i < additionalGuestsCount; i++) {
                    Guest additionalGuest = new Guest(
                            "Guest" + (i + 1),
                            lastName,
                            checkInDate,
                            stayDuration
                    );
                    additionalGuests.add(additionalGuest);
                }

                room.checkIn(mainGuest, additionalGuests);
            }
        }
    }

    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        int pos = 0;
        while (pos < line.length()) {
            char c = line.charAt(pos);

            if (c == '"') {
                if (inQuotes && pos + 1 < line.length() && line.charAt(pos + 1) == '"') {
                    current.append('"');
                    pos += 2;
                    continue;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
            pos++;
        }
        result.add(current.toString());

        return result.toArray(new String[0]);
    }

    @Override
    public String getCommandName() {
        return "load";
    }
}