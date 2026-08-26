/**
 * Displays all tasks in the task list.
 */
public class ListCommand extends Command {
    /**
     * Displays the current task list.
     *
     * @param tasks task list to display
     * @param ui user interface used to display the list
     * @param storage storage component, which this command does not modify
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks.getTasks());
    }
}
