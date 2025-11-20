package pl.agh.edu.hotel.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for ExitCommand
 */
class ExitCommandTest {

    private ExitCommand command;

    @BeforeEach
    void setUp() {
        command = new ExitCommand();
    }

    @Test
    @DisplayName("Should return correct command name")
    void testGetCommandName() {
        assertEquals("exit", command.getCommandName());
    }

    // There is no possibility to test execute() method as it calls System.exit()
}
