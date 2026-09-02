package yqr.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import yqr.exception.YqrException;
import yqr.task.Deadline;
import yqr.task.Event;
import yqr.task.Task;
import yqr.task.TaskList;
import yqr.task.Todo;

/**
 * Loads tasks from and saves tasks to a local text file.
 */
public class Storage {
    private static final String FIELD_SEPARATOR = " | ";
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";
    private static final String DONE_STATUS = "1";
    private static final String NOT_DONE_STATUS = "0";

    private final Path filePath;

    /**
     * Creates storage that reads from and writes to the given path.
     *
     * @param filePath relative path of the task data file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all tasks from the data file. A missing file represents an empty list.
     *
     * @return task list reconstructed from the data file.
     * @throws YqrException if the file cannot be read or contains invalid data.
     */
    public TaskList loadTasks() throws YqrException {
        if (Files.notExists(filePath)) {
            return new TaskList();
        }

        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            List<Task> tasks = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (!line.isBlank()) {
                    tasks.add(parseTask(line, i + 1));
                }
            }
            return new TaskList(tasks);
        } catch (IOException e) {
            throw new YqrException("Unable to load saved tasks: " + e.getMessage());
        }
    }

    /**
     * Saves the current task list, creating its parent directory when necessary.
     *
     * @param taskList task list to save.
     * @throws YqrException if the data file cannot be written.
     */
    public void saveTasks(TaskList taskList) throws YqrException {
        try {
            Path parentDirectory = filePath.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            List<String> lines = new ArrayList<>();
            for (Task task : taskList.getTasks()) {
                lines.add(formatTask(task));
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new YqrException("Unable to save tasks: " + e.getMessage());
        }
    }

    /**
     * Converts a task into one line of the storage format.
     *
     * @param task task to convert.
     * @return serialized task.
     */
    private String formatTask(Task task) {
        String status = task.isDone() ? DONE_STATUS : NOT_DONE_STATUS;
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.join(FIELD_SEPARATOR, DEADLINE_TYPE, status,
                    deadline.getDescription(), deadline.getBy().toString());
        }
        if (task instanceof Event) {
            Event event = (Event) task;
            return String.join(FIELD_SEPARATOR, EVENT_TYPE, status,
                    event.getDescription(), event.getFrom(), event.getTo());
        }
        return String.join(FIELD_SEPARATOR, TODO_TYPE, status, task.getDescription());
    }

    /**
     * Reconstructs one task from a line of stored data.
     *
     * @param line serialized task.
     * @param lineNumber line number used when reporting malformed data.
     * @return reconstructed task.
     * @throws YqrException if the line is malformed.
     */
    private Task parseTask(String line, int lineNumber) throws YqrException {
        String[] fields = line.split(Pattern.quote(FIELD_SEPARATOR), -1);
        if (fields.length < 3) {
            throw invalidData(lineNumber);
        }

        Task task;
        switch (fields[0]) {
            case TODO_TYPE:
                if (fields.length != 3) {
                    throw invalidData(lineNumber);
                }
                task = new Todo(fields[2]);
                break;
            case DEADLINE_TYPE:
                if (fields.length != 4) {
                    throw invalidData(lineNumber);
                }
                try {
                    task = new Deadline(fields[2], LocalDate.parse(fields[3]));
                } catch (DateTimeParseException e) {
                    throw invalidData(lineNumber);
                }
                break;
            case EVENT_TYPE:
                if (fields.length != 5) {
                    throw invalidData(lineNumber);
                }
                task = new Event(fields[2], fields[3], fields[4]);
                break;
            default:
                throw invalidData(lineNumber);
        }

        if (fields[1].equals(DONE_STATUS)) {
            task.markAsDone();
        } else if (!fields[1].equals(NOT_DONE_STATUS)) {
            throw invalidData(lineNumber);
        }
        return task;
    }

    /**
     * Creates a consistent error for a malformed line in the data file.
     *
     * @param lineNumber number of the malformed line.
     * @return exception describing the malformed data.
     */
    private YqrException invalidData(int lineNumber) {
        return new YqrException("Invalid saved task on line " + lineNumber);
    }
}
