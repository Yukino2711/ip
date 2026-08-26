import java.nio.file.Path;

/**
 * Coordinates the yqr chatbot's user interface, task list, parser, and storage.
 */
public class Duke {
    private final Storage storage;
    private final Ui ui;
    private TaskList taskList;

    /**
     * Creates a chatbot that saves its tasks at the given file path.
     *
     * @param filePath path of the task data file
     */
    public Duke(String filePath) {
        storage = new Storage(Path.of(filePath));
        ui = new Ui();
    }

    /**
     * Starts the chatbot and processes commands until the user exits.
     */
    public void run() {
        ui.showWelcome();
        loadTasks();

        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command command = Parser.parse(fullCommand);
                command.execute(taskList, ui, storage);
                isExit = command.isExit();
            } catch (YqrException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Starts the chatbot using its default storage file.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new Duke(Path.of("data", "yqr.txt").toString()).run();
    }

    /**
     * Loads saved tasks, falling back to an empty task list when loading fails.
     */
    private void loadTasks() {
        try {
            taskList = storage.loadTasks();
        } catch (YqrException e) {
            ui.showLoadingError(e.getMessage());
            taskList = new TaskList();
        }
    }
}
