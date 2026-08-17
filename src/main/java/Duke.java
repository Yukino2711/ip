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
            } else {
                taskList.addTask(new Task(command));
                System.out.println("added: " + command);
            }
            System.out.println(separator);
        }
    }
}
