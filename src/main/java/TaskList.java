/**
 * Stores and displays the tasks entered by the user.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;

    private final Task[] tasks = new Task[MAX_TASKS];
    private int taskCount;

    /**
     * Adds a task to the list.
     *
     * @param task task to store
     */
    public void addTask(Task task) {
        tasks[taskCount] = task;
        taskCount++;
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return number of stored tasks
     */
    public int getTaskCount() {
        return taskCount;
    }

    /**
     * Displays all stored tasks in numbered order.
     */
    public void displayTasks() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
    }

    /**
     * Marks a task as done.
     *
     * @param taskNumber one-based number of the task to mark
     * @return the task whose status was changed
     */
    public Task markTaskAsDone(int taskNumber) {
        Task task = tasks[taskNumber - 1];
        task.markAsDone();
        return task;
    }

    /**
     * Marks a task as not done.
     *
     * @param taskNumber one-based number of the task to unmark
     * @return the task whose status was changed
     */
    public Task markTaskAsNotDone(int taskNumber) {
        Task task = tasks[taskNumber - 1];
        task.markAsNotDone();
        return task;
    }
}
