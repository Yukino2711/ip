package yqr.task;

import java.util.ArrayList;
import java.util.List;

import yqr.exception.YqrException;

/**
 * Stores the tasks entered by the user and provides operations on them.
 */
public class TaskList {
    private final List<Task> tasks = new ArrayList<>();
    /** Reverses the most recent task change, or is {@code null} when none is available. */
    private Runnable lastUndoAction;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
    }

    /**
     * Creates a task list containing tasks loaded from storage.
     *
     * @param tasks tasks with which to initialize the list.
     */
    public TaskList(List<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    /**
     * Adds a task to the list.
     *
     * @param task task to store.
     */
    public void addTask(Task task) {
        int taskIndex = tasks.size();
        tasks.add(task);
        lastUndoAction = () -> tasks.remove(taskIndex);
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return number of stored tasks.
     */
    public int getTaskCount() {
        return tasks.size();
    }

    /**
     * Returns an unmodifiable snapshot of the stored tasks.
     *
     * @return snapshot of the tasks in this list.
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Returns tasks whose descriptions contain the given keyword.
     *
     * @param keyword keyword to find in task descriptions.
     * @return matching tasks in their original order.
     */
    public List<Task> findTasks(String keyword) {
        return tasks.stream()
                .filter(task -> task.getDescription().contains(keyword))
                .toList();
    }

    /**
     * Deletes a task from the list.
     *
     * @param taskNumber one-based number of the task to delete.
     * @return the deleted task.
     * @throws YqrException if the task number is outside the list.
     */
    public Task deleteTask(int taskNumber) throws YqrException {
        Task task = getTask(taskNumber);
        int taskIndex = taskNumber - 1;
        tasks.remove(taskIndex);
        lastUndoAction = () -> tasks.add(taskIndex, task);
        return task;
    }

    /**
     * Marks a task as done.
     *
     * @param taskNumber one-based number of the task to mark.
     * @return the task whose status was changed.
     * @throws YqrException if the task number is outside the list.
     */
    public Task markTaskAsDone(int taskNumber) throws YqrException {
        Task task = getTask(taskNumber);
        boolean wasDone = task.isDone();
        task.markAsDone();
        lastUndoAction = () -> setTaskStatus(task, wasDone);
        return task;
    }

    /**
     * Marks a task as not done.
     *
     * @param taskNumber one-based number of the task to unmark.
     * @return the task whose status was changed.
     * @throws YqrException if the task number is outside the list.
     */
    public Task markTaskAsNotDone(int taskNumber) throws YqrException {
        Task task = getTask(taskNumber);
        boolean wasDone = task.isDone();
        task.markAsNotDone();
        lastUndoAction = () -> setTaskStatus(task, wasDone);
        return task;
    }

    /**
     * Restores the task list to its state before the most recent change.
     *
     * @throws YqrException if no task-changing command can be undone.
     */
    public void undo() throws YqrException {
        if (lastUndoAction == null) {
            throw new YqrException("There is no command to undo");
        }
        lastUndoAction.run();
        lastUndoAction = null;
    }

    /** Restores a task's previous completion status. */
    private static void setTaskStatus(Task task, boolean isDone) {
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
    }

    /**
     * Returns the task with the given one-based task number.
     *
     * @param taskNumber one-based task number.
     * @return corresponding task.
     * @throws YqrException if the task number is outside the list.
     */
    private Task getTask(int taskNumber) throws YqrException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new YqrException("Please input a valid task number");
        }
        return tasks.get(taskNumber - 1);
    }
}
