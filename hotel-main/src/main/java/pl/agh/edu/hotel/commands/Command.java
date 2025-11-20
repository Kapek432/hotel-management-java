package pl.agh.edu.hotel.commands;

import lombok.Setter;
import pl.agh.edu.hotel.model.Hotel;

import java.util.Scanner;

/**
 * Abstract base class for all hotel management commands.
 * Implements the Command pattern for extensibility.
 */
@Setter
public abstract class Command {
    protected Hotel hotel;
    protected Scanner scanner;

    /**
     * Executes the command logic.
     * This method must be implemented by all concrete command classes.
     */
    public abstract void execute();

    /**
     * Returns the command name for registration.
     * @return command name
     */
    public abstract String getCommandName();
}
