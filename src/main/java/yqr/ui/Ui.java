package yqr.ui;

import java.util.List;
import java.util.Scanner;

import yqr.task.Task;

/**
 * Handles input from and output to the user.
 */
public class Ui {
    private static final String SEPARATOR =
            "____________________________________________________________";
    private static final String BANNER = "+-------+\n"
            + "|  yqr  |\n"
            + "+-------+";

    private final Scanner scanner = new Scanner(System.in);

    /** Displays the chatbot greeting. */
    public void showWelcome() {
        showLine();
        System.out.println(BANNER);
        System.out.println("Hello! I'm yqr.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Returns whether another command can be read from standard input.
     *
     * @return {@code true} if another command is available
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and trims the next command entered by the user.
     *
     * @return next user command
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Displays the chatbot farewell. */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Displays an error encountered while loading saved tasks.
     *
     * @param message explanation of the loading error
     */
    public void showLoadingError(String message) {
        System.out.println(message);
        System.out.println("Starting with an empty task list instead.");
        showLine();
    }

    /**
     * Displays an error caused by a command or storage operation.
     *
     * @param message explanation of the error
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays all tasks in numbered order.
     *
     * @param tasks tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays confirmation that a task was marked or unmarked.
     *
     * @param task task whose status changed
     * @param isMarked whether the task was marked as done
     */
    public void showTaskStatusChange(Task task, boolean isMarked) {
        if (isMarked) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
    }

    /**
     * Displays confirmation that a task was deleted and the new task count.
     *
     * @param task deleted task
     * @param taskCount number of tasks remaining
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    /**
     * Displays confirmation that a task was added and the new task count.
     *
     * @param task added task
     * @param taskCount number of tasks now stored
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    /** Displays the line separating command responses. */
    public void showLine() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays a task count with the correct singular or plural noun.
     *
     * @param taskCount number of tasks to display
     */
    private void showTaskCount(int taskCount) {
        String taskWord = taskCount == 1 ? "task" : "tasks";
        System.out.println("Now you have " + taskCount + " " + taskWord + " in the list.");
    }
}
