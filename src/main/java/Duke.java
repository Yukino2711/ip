import java.util.Scanner;

/**
 * Runs the yqr chatbot and handles commands entered by the user.
 */
public class Duke {
    /**
     * Starts the chatbot.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String separator = "____________________________________________________________";
        String banner = "+-------+\n"
                + "|  yqr  |\n"
                + "+-------+";

        System.out.println(separator);
        System.out.println(banner);
        System.out.println("Hello! I'm yqr.");
        System.out.println("What can I do for you?");
        System.out.println(separator);

        Scanner scanner = new Scanner(System.in);
        TaskList taskList = new TaskList();

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            if (command.equals("bye")) {
                System.out.println(separator);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(separator);
                break;
            }

            System.out.println(separator);
            try {
                handleCommand(command, taskList);
            } catch (YqrException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(separator);
        }
    }

    /**
     * Executes a command or reports invalid input using a {@link YqrException}.
     *
     * @param command command entered by the user
     * @param taskList list on which the command operates
     * @throws YqrException if the command or its arguments are invalid
     */
    private static void handleCommand(String command, TaskList taskList) throws YqrException {
        String commandWord = command.split("\\s+", 2)[0];

        switch (commandWord) {
        case "list":
            if (command.equals("list")) {
                taskList.displayTasks();
                return;
            }
            break;
        case "mark":
            markTask(command, taskList, true);
            return;
        case "unmark":
            markTask(command, taskList, false);
            return;
        case "delete":
            deleteTask(command, taskList);
            return;
        case "todo":
            addTask(taskList, parseTodo(command));
            return;
        case "deadline":
            addTask(taskList, parseDeadline(command));
            return;
        case "event":
            addTask(taskList, parseEvent(command));
            return;
        default:
            break;
        }

        throw new YqrException("Please input valid commands");
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
     * @throws YqrException if the description or deadline is missing
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
        return new Deadline(description, by);
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
     * Marks or unmarks the task identified in a command.
     *
     * @param command mark or unmark command entered by the user
     * @param taskList list containing the task
     * @param isMark {@code true} to mark the task, or {@code false} to unmark it
     * @throws YqrException if the task number is missing, invalid, or out of range
     */
    private static void markTask(String command, TaskList taskList, boolean isMark)
            throws YqrException {
        String commandWord = isMark ? "mark" : "unmark";
        int taskNumber = parseTaskNumber(command, commandWord);

        Task task;
        if (isMark) {
            task = taskList.markTaskAsDone(taskNumber);
            System.out.println("Nice! I've marked this task as done:");
        } else {
            task = taskList.markTaskAsNotDone(taskNumber);
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
    }

    /**
     * Deletes the task identified in a command and displays the updated task count.
     *
     * @param command delete command entered by the user
     * @param taskList list containing the task
     * @throws YqrException if the task number is missing, invalid, or out of range
     */
    private static void deleteTask(String command, TaskList taskList) throws YqrException {
        int taskNumber = parseTaskNumber(command, "delete");
        Task deletedTask = taskList.deleteTask(taskNumber);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + deletedTask);
        displayTaskCount(taskList);
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

    /**
     * Adds a task and displays confirmation and the updated task count.
     *
     * @param taskList list to which the task is added
     * @param task task to add
     */
    private static void addTask(TaskList taskList, Task task) {
        taskList.addTask(task);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        displayTaskCount(taskList);
    }

    /**
     * Displays the number of tasks currently in a list.
     *
     * @param taskList task list whose size is displayed
     */
    private static void displayTaskCount(TaskList taskList) {
        int taskCount = taskList.getTaskCount();
        String taskWord = taskCount == 1 ? "task" : "tasks";
        System.out.println("Now you have " + taskCount + " " + taskWord + " in the list.");
    }
}
