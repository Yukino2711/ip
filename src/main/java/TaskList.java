import java.util.ArrayList;
import java.util.List;

/**
 * Stores and displays the tasks entered by the user.
 */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Creates an empty task list.
     */
    public TaskList() {
    }

    /**
     * Creates a task list containing tasks loaded from storage.
     *
     * @param tasks tasks with which to initialise the list
     */
    public TaskList(List<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    /**
     * Adds a task to the list.
     *
     * @param task task to store
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return number of stored tasks
     */
    public int getTaskCount() {
        return tasks.size();
    }

    /**
     * Returns an unmodifiable snapshot of the stored tasks.
     *
     * @return snapshot of the tasks in this list
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Displays all stored tasks in numbered order.
     */
    public void displayTasks() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Deletes a task from the list.
     *
     * @param taskNumber one-based number of the task to delete
     * @return the deleted task
     * @throws YqrException if the task number is outside the list
     */
    public Task deleteTask(int taskNumber) throws YqrException {
        Task task = getTask(taskNumber);
        tasks.remove(taskNumber - 1);
        return task;
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
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new YqrException("Please input a valid task number");
        }
        return tasks.get(taskNumber - 1);
    }
}
