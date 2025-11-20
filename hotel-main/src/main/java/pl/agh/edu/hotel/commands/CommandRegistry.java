package pl.agh.edu.hotel.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Factory class for managing and creating command instances.
 * Implements the Factory pattern for command creation.
 */
public class CommandRegistry {
    private final Map<String, Class<? extends Command>> commandMap = new HashMap<>();

    /**
     * Registers a command class with a given name.
     *
     * @param name         the command name (case-insensitive)
     * @param commandClass the command class to register
     */
    public void registerCommand(String name, Class<? extends Command> commandClass) {
        commandMap.put(name.toLowerCase(), commandClass);
    }

    /**
     * Creates a command instance based on the command name.
     *
     * @param commandName the name of the command
     * @return a new instance of the requested command
     * @throws IllegalArgumentException if the command is not registered
     */
    public Command createCommand(String commandName) {
        Class<? extends Command> commandClass = commandMap.get(commandName.toLowerCase());
        if (commandClass == null) {
            throw new IllegalArgumentException("Unknown command: " + commandName);
        }
        try {
            return commandClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create command instance", e);
        }
    }
    /**
     * Returns all registered command names.
     * @return set of command names
     */
    public Set<String> getRegisteredCommands() {
        return commandMap.keySet();
    }
}

