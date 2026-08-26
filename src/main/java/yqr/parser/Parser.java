package yqr.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import yqr.command.AddCommand;
import yqr.command.Command;
import yqr.command.DeleteCommand;
import yqr.command.ExitCommand;
import yqr.command.FindCommand;
import yqr.command.ListCommand;
import yqr.command.MarkCommand;
import yqr.command.UnmarkCommand;
import yqr.exception.YqrException;
import yqr.task.Deadline;
import yqr.task.Event;
import yqr.task.Todo;

/**
 * Parses user commands and converts their arguments into values used by the application.
 */
public class Parser {
    /** Prevents creation of this utility class. */
    private Parser() {
    }

    /**
     * Parses a full user command into a command that can be executed.
     *
     * @param fullCommand command entered by the user
     * @return command corresponding to the user input
     * @throws YqrException if the command or its arguments are invalid
     */
    public static Command parse(String fullCommand) throws YqrException {
        String command = fullCommand.trim();
        String commandWord = getCommandWord(command);

        switch (commandWord) {
        case "list":
            if (command.equals("list")) {
                return new ListCommand();
            }
            break;
        case "mark":
            return new MarkCommand(parseTaskNumber(command, "mark"));
        case "unmark":
            return new UnmarkCommand(parseTaskNumber(command, "unmark"));
        case "delete":
            return new DeleteCommand(parseTaskNumber(command, "delete"));
        case "todo":
            return new AddCommand(parseTodo(command));
        case "deadline":
            return new AddCommand(parseDeadline(command));
        case "event":
            return new AddCommand(parseEvent(command));
        case "find":
            return new FindCommand(parseFindKeyword(command));
        case "bye":
            if (command.equals("bye")) {
                return new ExitCommand();
            }
            break;
        default:
            break;
        }

        throw new YqrException("Please input valid commands");
    }

    /**
     * Returns the first word of a command, which identifies the requested operation.
     *
     * @param command command entered by the user
     * @return command word, or an empty string when the command is empty
     */
    private static String getCommandWord(String command) {
        return command.isEmpty() ? "" : command.split("\\s+", 2)[0];
    }

    /**
     * Creates a todo from a command after validating its description.
     *
     * @param command todo command entered by the user
     * @return parsed todo
     * @throws YqrException if the description is missing
     */
    private static Todo parseTodo(String command) throws YqrException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new YqrException("Please input task description");
        }
        return new Todo(description);
    }

    /**
     * Creates a deadline from a command after validating its description and deadline.
     *
     * @param command deadline command entered by the user
     * @return parsed deadline
     * @throws YqrException if the description or deadline is missing or invalid
     */
    private static Deadline parseDeadline(String command) throws YqrException {
        String taskDetails = command.substring("deadline".length()).trim();
        if (taskDetails.isEmpty() || taskDetails.startsWith("/by")) {
            throw new YqrException("Please input task description");
        }

        int byIndex = taskDetails.indexOf(" /by");
        if (byIndex < 0) {
            throw new YqrException("Please input the deadline");
        }

        String description = taskDetails.substring(0, byIndex).trim();
        String by = taskDetails.substring(byIndex + " /by".length()).trim();
        if (description.isEmpty()) {
            throw new YqrException("Please input task description");
        }
        if (by.isEmpty()) {
            throw new YqrException("Please input the deadline");
        }
        try {
            return new Deadline(description, LocalDate.parse(by));
        } catch (DateTimeParseException e) {
            throw new YqrException("Please input the deadline in yyyy-MM-dd format");
        }
    }

    /**
     * Creates an event from a command after validating its description and time details.
     *
     * @param command event command entered by the user
     * @return parsed event
     * @throws YqrException if the description or event times are missing
     */
    private static Event parseEvent(String command) throws YqrException {
        String taskDetails = command.substring("event".length()).trim();
        if (taskDetails.isEmpty()
                || taskDetails.startsWith("/from")
                || taskDetails.startsWith("/to")) {
            throw new YqrException("Please input task description");
        }

        int fromIndex = taskDetails.indexOf(" /from");
        if (fromIndex < 0) {
            throw new YqrException("Please input the starting and ending details");
        }

        String description = taskDetails.substring(0, fromIndex).trim();
        String timeDetails = taskDetails.substring(fromIndex + " /from".length()).trim();
        int toIndex = timeDetails.indexOf(" /to");
        if (toIndex < 0) {
            throw new YqrException("Please input the starting and ending details");
        }

        String from = timeDetails.substring(0, toIndex).trim();
        String to = timeDetails.substring(toIndex + " /to".length()).trim();
        if (description.isEmpty()) {
            throw new YqrException("Please input task description");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new YqrException("Please input the starting and ending details");
        }
        return new Event(description, from, to);
    }

    /**
     * Extracts and validates the keyword from a find command.
     *
     * @param command find command entered by the user.
     * @return keyword to search for.
     * @throws YqrException if the keyword is missing.
     */
    private static String parseFindKeyword(String command) throws YqrException {
        String keyword = command.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new YqrException("Please input a keyword to search for");
        }
        return keyword;
    }

    /**
     * Extracts a task number from a command.
     *
     * @param command command containing a task number
     * @param commandWord command word to remove before parsing
     * @return parsed task number
     * @throws YqrException if the task number is missing or not an integer
     */
    private static int parseTaskNumber(String command, String commandWord) throws YqrException {
        String numberText = command.substring(commandWord.length()).trim();
        try {
            return Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            throw new YqrException("Please input a valid task number");
        }
    }
}
