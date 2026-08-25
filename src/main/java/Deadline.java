/**
 * Represents a task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description description of the task
     * @param by deadline as entered by the user
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline details.
     *
     * @return deadline as entered by the user
     */
    public String getBy() {
        return by;
    }

    /**
     * Returns a displayable version of this deadline task.
     *
     * @return deadline type icon, task details, and deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
