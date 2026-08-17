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
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.println(separator);
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(separator);
                break;
            }

            System.out.println(separator);
            if (command.equals("list")) {
                taskList.displayTasks();
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring("mark ".length()).trim());
                Task task = taskList.markTaskAsDone(taskNumber);
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + task);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring("unmark ".length()).trim());
                Task task = taskList.markTaskAsNotDone(taskNumber);
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + task);
            } else if (command.startsWith("todo ")) {
                String description = command.substring("todo ".length()).trim();
                addTask(taskList, new Todo(description));
            } else if (command.startsWith("deadline ")) {
                String taskDetails = command.substring("deadline ".length()).trim();
                int byIndex = taskDetails.indexOf(" /by ");
                String description = taskDetails.substring(0, byIndex);
                String by = taskDetails.substring(byIndex + " /by ".length());
                addTask(taskList, new Deadline(description, by));
            } else if (command.startsWith("event ")) {
                String taskDetails = command.substring("event ".length()).trim();
                int fromIndex = taskDetails.indexOf(" /from ");
                int toIndex = taskDetails.indexOf(" /to ", fromIndex + " /from ".length());
                String description = taskDetails.substring(0, fromIndex);
                String from = taskDetails.substring(fromIndex + " /from ".length(), toIndex);
                String to = taskDetails.substring(toIndex + " /to ".length());
                addTask(taskList, new Event(description, from, to));
            }
            System.out.println(separator);
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
        int taskCount = taskList.getTaskCount();
        String taskWord = taskCount == 1 ? "task" : "tasks";
        System.out.println("Now you have " + taskCount + " " + taskWord + " in the list.");
    }
}
