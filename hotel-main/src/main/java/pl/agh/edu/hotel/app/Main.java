package pl.agh.edu.hotel.app;

import pl.agh.edu.hotel.commands.*;
import pl.agh.edu.hotel.model.Hotel;
import pl.agh.edu.hotel.model.Room;

import java.util.Scanner;

/**
 * Main application class for the Hotel Management System.
 */
public class Main {

    public static void main(String[] args) {
        // Initialize hotel
        Hotel hotel = initializeHotel();

        // Initialize command registry
        CommandRegistry commandRegistry = new CommandRegistry();
        registerCommands(commandRegistry);

        // Main application loop
        runApplication(hotel, commandRegistry);
    }

    /**
     * Initializes the hotel with default rooms.
     * @return initialized hotel
     */
    private static Hotel initializeHotel() {
        Hotel hotel = new Hotel("My Hotel", 3);

        // Floor 1
        hotel.addRoom(new Room(101, "Single", 1, 150.0));
        hotel.addRoom(new Room(102, "Single", 1, 150.0));
        hotel.addRoom(new Room(103, "Double", 2, 250.0));
        hotel.addRoom(new Room(104, "Deluxe", 4, 700.0));

        // Floor 2
        hotel.addRoom(new Room(201, "Double", 2, 300.0));
        hotel.addRoom(new Room(202, "Triple", 3, 400.0));
        hotel.addRoom(new Room(203, "Triple", 3, 400.0));

        // Floor 3
        hotel.addRoom(new Room(301, "Suite", 4, 600.0));
        hotel.addRoom(new Room(302, "Deluxe", 2, 500.0));
        hotel.addRoom(new Room(303, "Suite", 4, 600.0));

        return hotel;
    }

    /**
     * Registers all available commands in the registry.
     * @param registry the command registry
     */
    private static void registerCommands(CommandRegistry registry) {
        registry.registerCommand("load", LoadCommand.class);
        registry.registerCommand("save", SaveCommand.class);
        registry.registerCommand("prices", PricesCommand.class);
        registry.registerCommand("view", ViewCommand.class);
        registry.registerCommand("checkin", CheckinCommand.class);
        registry.registerCommand("checkout", CheckoutCommand.class);
        registry.registerCommand("list", ListCommand.class);
        registry.registerCommand("exit", ExitCommand.class);
    }

    /**
     * Runs the main application loop.
     * @param hotel the hotel instance
     * @param commandRegistry the command registry
     */
    private static void runApplication(Hotel hotel, CommandRegistry commandRegistry) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("  Welcome to " + hotel.getName());
        System.out.println("========================================");
        System.out.println();

        boolean running = true;
        while (running) {
            displayMenu(commandRegistry);
            System.out.print("Enter command: ");
            String cmd = scanner.nextLine().trim();
            System.out.println();

            if (cmd.isEmpty()) {
                continue;
            }

            try {
                Command command = commandRegistry.createCommand(cmd);
                command.setHotel(hotel);
                command.setScanner(scanner);

                if (command instanceof ExitCommand) {
                    command.execute();
                    running = false;
                } else {
                    command.execute();
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Unknown command: " + cmd);
                System.err.println("Please use one of the available commands listed above.");
                System.out.println();
            } catch (Exception e) {
                System.err.println("Error executing command: " + e.getMessage());
                System.out.println();
            }
        }
    }

    /**
     * Displays the menu with available commands.
     * @param registry the command registry
     */
    private static void displayMenu(CommandRegistry registry) {
        System.out.println("Available commands:");
        for (String command : registry.getRegisteredCommands()) {
            System.out.println("  - " + command);
        }
        System.out.println();
    }
}
