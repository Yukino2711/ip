package yqr.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import yqr.command.Command;
import yqr.command.FindCommand;
import yqr.exception.YqrException;

/**
 * Tests parsing of find commands.
 */
class ParserTest {
    private static final String MISSING_KEYWORD_MESSAGE = "Please input a keyword to search for";

    @Test
    void parse_validFindCommand_findCommandReturned() throws YqrException {
        Command command = Parser.parse("find book");

        assertInstanceOf(FindCommand.class, command);
    }

    @Test
    void parse_findWithoutKeyword_exceptionThrown() {
        YqrException exception = assertThrows(YqrException.class, () -> Parser.parse("find"));

        assertEquals(MISSING_KEYWORD_MESSAGE, exception.getMessage());
    }

    @Test
    void parse_findWithWhitespaceOnlyKeyword_exceptionThrown() {
        YqrException exception = assertThrows(YqrException.class, () -> Parser.parse("find     "));

        assertEquals(MISSING_KEYWORD_MESSAGE, exception.getMessage());
    }
}
