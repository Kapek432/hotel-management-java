package pl.agh.edu.hotel.commands;
/**
 * Command to exit the application.
 */
public class ExitCommand extends Command {

    @Override
    public void execute() {
        System.out.println("Thank you for using Hotel Management System. Goodbye!");
        System.exit(0);
    }

    @Override
    public String getCommandName() {
        return "exit";
    }
}