package yqr.task;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import yqr.exception.YqrException;

/**
 * Tests the public behaviors of {@link TaskList}.
 */
class TaskListTest {
    private static final String INVALID_TASK_NUMBER_MESSAGE = "Please input a valid task number";

    @Test
    void addTask_emptyList_taskAdded() {
        Task task = new Todo("read book");
        TaskList taskList = new TaskList();

        taskList.addTask(task);

        assertAll(
                () -> assertEquals(1, taskList.getTaskCount()),
                () -> assertIterableEquals(List.of(task), taskList.getTasks()));
    }

    @Test
    void addTask_nonEmptyList_taskAppendedAtEnd() {
        Task firstTask = new Todo("first");
        Task secondTask = new Todo("second");
        TaskList taskList = new TaskList(List.of(firstTask));

        taskList.addTask(secondTask);

        assertIterableEquals(List.of(firstTask, secondTask), taskList.getTasks());
    }

    @Test
    void getTaskCount_emptyAndPopulatedLists_correctCountsReturned() {
        TaskList emptyTaskList = new TaskList();
        TaskList populatedTaskList = new TaskList(List.of(
                new Todo("first"), new Todo("second"), new Todo("third")));

        assertAll(
                () -> assertEquals(0, emptyTaskList.getTaskCount()),
                () -> assertEquals(3, populatedTaskList.getTaskCount()));
    }

    @Test
    void getTasks_listChangesAfterCall_returnedSnapshotIsUnchangedAndUnmodifiable() {
        Task firstTask = new Todo("first");
        Task secondTask = new Todo("second");
        TaskList taskList = new TaskList(List.of(firstTask));
        List<Task> snapshot = taskList.getTasks();

        taskList.addTask(secondTask);

        assertAll(
                () -> assertIterableEquals(List.of(firstTask), snapshot),
                () -> assertThrows(UnsupportedOperationException.class,
                        () -> snapshot.add(secondTask)),
                () -> assertIterableEquals(List.of(firstTask, secondTask), taskList.getTasks()));
    }

    @Test
    void markTaskAsDone_validTask_taskMarkedAndReturned() throws YqrException {
        Task firstTask = new Todo("first");
        Task targetTask = new Todo("target");
        Task thirdTask = new Todo("third");
        TaskList taskList = new TaskList(List.of(firstTask, targetTask, thirdTask));

        Task markedTask = taskList.markTaskAsDone(2);

        assertAll(
                () -> assertSame(targetTask, markedTask),
                () -> assertTrue(targetTask.isDone()),
                () -> assertFalse(firstTask.isDone()),
                () -> assertFalse(thirdTask.isDone()),
                () -> assertIterableEquals(List.of(firstTask, targetTask, thirdTask),
                        taskList.getTasks()));
    }

    @Test
    void markTaskAsDone_alreadyDoneTask_taskRemainsDone() throws YqrException {
        Task task = new Todo("task");
        task.markAsDone();
        TaskList taskList = new TaskList(List.of(task));

        Task markedTask = taskList.markTaskAsDone(1);

        assertAll(
                () -> assertSame(task, markedTask),
                () -> assertTrue(task.isDone()));
    }

    @Test
    void markTaskAsDone_invalidTaskNumbers_exceptionsThrown() {
        TaskList emptyTaskList = new TaskList();
        TaskList taskList = new TaskList(List.of(new Todo("task")));

        assertAll(
                () -> assertInvalidTaskNumber(() -> emptyTaskList.markTaskAsDone(1)),
                () -> assertInvalidTaskNumber(() -> taskList.markTaskAsDone(-1)),
                () -> assertInvalidTaskNumber(() -> taskList.markTaskAsDone(0)),
                () -> assertInvalidTaskNumber(() -> taskList.markTaskAsDone(2)));
    }

    @Test
    void markTaskAsNotDone_doneTask_taskUnmarkedAndReturned() throws YqrException {
        Task firstTask = new Todo("first");
        Task targetTask = new Todo("target");
        Task thirdTask = new Todo("third");
        targetTask.markAsDone();
        TaskList taskList = new TaskList(List.of(firstTask, targetTask, thirdTask));

        Task unmarkedTask = taskList.markTaskAsNotDone(2);

        assertAll(
                () -> assertSame(targetTask, unmarkedTask),
                () -> assertFalse(targetTask.isDone()),
                () -> assertFalse(firstTask.isDone()),
                () -> assertFalse(thirdTask.isDone()),
                () -> assertIterableEquals(List.of(firstTask, targetTask, thirdTask),
                        taskList.getTasks()));
    }

    @Test
    void markTaskAsNotDone_alreadyNotDoneTask_taskRemainsNotDone() throws YqrException {
        Task task = new Todo("task");
        TaskList taskList = new TaskList(List.of(task));

        Task unmarkedTask = taskList.markTaskAsNotDone(1);

        assertAll(
                () -> assertSame(task, unmarkedTask),
                () -> assertFalse(task.isDone()));
    }

    @Test
    void markTaskAsNotDone_invalidTaskNumbers_exceptionsThrown() {
        TaskList emptyTaskList = new TaskList();
        TaskList taskList = new TaskList(List.of(new Todo("task")));

        assertAll(
                () -> assertInvalidTaskNumber(() -> emptyTaskList.markTaskAsNotDone(1)),
                () -> assertInvalidTaskNumber(() -> taskList.markTaskAsNotDone(-1)),
                () -> assertInvalidTaskNumber(() -> taskList.markTaskAsNotDone(0)),
                () -> assertInvalidTaskNumber(() -> taskList.markTaskAsNotDone(2)));
    }

    @Test
    void deleteTask_onlyTask_taskRemovedAndReturned() throws YqrException {
        Task onlyTask = new Todo("read book");
        TaskList taskList = new TaskList(List.of(onlyTask));

        Task deletedTask = taskList.deleteTask(1);

        assertAll(
                () -> assertSame(onlyTask, deletedTask),
                () -> assertEquals(0, taskList.getTaskCount()),
                () -> assertIterableEquals(List.of(), taskList.getTasks()));
    }

    @Test
    void deleteTask_firstTask_firstTaskRemoved() throws YqrException {
        Task firstTask = new Todo("first");
        Task secondTask = new Todo("second");
        Task thirdTask = new Todo("third");
        TaskList taskList = new TaskList(List.of(firstTask, secondTask, thirdTask));

        Task deletedTask = taskList.deleteTask(1);

        assertAll(
                () -> assertSame(firstTask, deletedTask),
                () -> assertIterableEquals(List.of(secondTask, thirdTask), taskList.getTasks()));
    }

    @Test
    void deleteTask_middleTask_middleTaskRemoved() throws YqrException {
        Task firstTask = new Todo("first");
        Task secondTask = new Todo("second");
        Task thirdTask = new Todo("third");
        TaskList taskList = new TaskList(List.of(firstTask, secondTask, thirdTask));

        Task deletedTask = taskList.deleteTask(2);

        assertAll(
                () -> assertSame(secondTask, deletedTask),
                () -> assertIterableEquals(List.of(firstTask, thirdTask), taskList.getTasks()));
    }

    @Test
    void deleteTask_lastTask_lastTaskRemoved() throws YqrException {
        Task firstTask = new Todo("first");
        Task secondTask = new Todo("second");
        Task thirdTask = new Todo("third");
        TaskList taskList = new TaskList(List.of(firstTask, secondTask, thirdTask));

        Task deletedTask = taskList.deleteTask(3);

        assertAll(
                () -> assertSame(thirdTask, deletedTask),
                () -> assertIterableEquals(List.of(firstTask, secondTask), taskList.getTasks()));
    }

    @Test
    void deleteTask_emptyList_exceptionThrown() {
        TaskList taskList = new TaskList();

        YqrException exception = assertThrows(YqrException.class, () -> taskList.deleteTask(1));

        assertEquals(INVALID_TASK_NUMBER_MESSAGE, exception.getMessage());
    }

    @Test
    void deleteTask_zeroTaskNumber_exceptionThrown() {
        TaskList taskList = new TaskList(List.of(new Todo("task")));

        YqrException exception = assertThrows(YqrException.class, () -> taskList.deleteTask(0));

        assertEquals(INVALID_TASK_NUMBER_MESSAGE, exception.getMessage());
    }

    @Test
    void deleteTask_negativeTaskNumber_exceptionThrown() {
        TaskList taskList = new TaskList(List.of(new Todo("task")));

        YqrException exception = assertThrows(YqrException.class, () -> taskList.deleteTask(-1));

        assertEquals(INVALID_TASK_NUMBER_MESSAGE, exception.getMessage());
    }

    @Test
    void deleteTask_taskNumberAboveRange_exceptionThrown() {
        TaskList taskList = new TaskList(List.of(new Todo("task")));

        YqrException exception = assertThrows(YqrException.class, () -> taskList.deleteTask(2));

        assertEquals(INVALID_TASK_NUMBER_MESSAGE, exception.getMessage());
    }

    /**
     * Asserts that an operation fails because its task number is outside the list.
     *
     * @param operation operation expected to reject its task number
     */
    private static void assertInvalidTaskNumber(TaskListOperation operation) {
        YqrException exception = assertThrows(YqrException.class, operation::execute);
        assertEquals(INVALID_TASK_NUMBER_MESSAGE, exception.getMessage());
    }

    /**
     * Represents a task-list operation that may reject an invalid task number.
     */
    @FunctionalInterface
    private interface TaskListOperation {
        void execute() throws YqrException;
    }
}
