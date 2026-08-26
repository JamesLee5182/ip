package longfrog.task;

import longfrog.util.FormatUtils;
import java.time.LocalDateTime;

/** Represents a task with a start and end date-time range. */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String name, LocalDateTime from, LocalDateTime to) {
        super(name);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return this.from;
    }

    public LocalDateTime getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + this.from.format(FormatUtils.PRINT_FORMAT)
                + " to: " + this.to.format(FormatUtils.PRINT_FORMAT) + ")";
    }

    @Override
    public String toFileFormat() {
        return "E | " + (isDone ? "1" : "0") + " | " + this.name + " | "
                + this.from.format(FormatUtils.INPUT_SAVE_FORMAT) + " | "
                + this.to.format(FormatUtils.INPUT_SAVE_FORMAT);
    }
}
