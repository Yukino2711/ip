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
     * @throws YqrException if the list already contains 100 tasks
     */
    public void addTask(Task task) throws YqrException {
        if (taskCount >= MAX_TASKS) {
            throw new YqrException("The task list is full");
        }
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
     * @throws YqrException if the task number is outside the list
     */
    public Task markTaskAsDone(int taskNumber) throws YqrException {
        Task task = getTask(taskNumber);
        task.markAsDone();
        return task;
    }

    /**
     * Marks a task as not done.
     *
     * @param taskNumber one-based number of the task to unmark
     * @return the task whose status was changed
     * @throws YqrException if the task number is outside the list
     */
    public Task markTaskAsNotDone(int taskNumber) throws YqrException {
        Task task = getTask(taskNumber);
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns the task with the given one-based task number.
     *
     * @param taskNumber one-based task number
     * @return corresponding task
     * @throws YqrException if the task number is outside the list
     */
    private Task getTask(int taskNumber) throws YqrException {
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new YqrException("Please input a valid task number");
        }
        return tasks[taskNumber - 1];
    }
}
