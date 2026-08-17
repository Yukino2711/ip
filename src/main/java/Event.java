/**
 * Represents a task that takes place between specified start and end times.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an incomplete event task.
     *
     * @param description description of the event
     * @param from start date or time as entered by the user
     * @param to end date or time as entered by the user
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns a displayable version of this event task.
     *
     * @return event type icon, task details, and start and end times
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
