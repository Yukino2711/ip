/**
 * Stores and displays the tasks entered by the user.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;

    private final String[] tasks = new String[MAX_TASKS];
    private int taskCount;

    /**
     * Adds a task to the list.
     *
     * @param task task description to store
     */
    public void addTask(String task) {
        tasks[taskCount] = task;
        taskCount++;
    }

    /**
     * Displays all stored tasks in numbered order.
     */
    public void displayTasks() {
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
        }
    }
}
