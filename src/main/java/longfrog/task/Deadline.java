package longfrog.task;

import java.time.LocalDateTime;

import longfrog.util.FormatUtils;

/** Represents a task that must be completed by a particular date and time. */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Creates a deadline task.
     *
     * @param name the task description
     * @param by the due date and time
     */
    public Deadline(String name, LocalDateTime by) {
        super(name);
        this.by = by;
    }

    /**
     * Returns the due date and time.
     *
     * @return the deadline date and time
     */
    public LocalDateTime getBy() {
        return this.by;
    }

    /**
     * Returns this deadline in its console display format.
     *
     * @return the formatted deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by.format(FormatUtils.PRINT_FORMAT) + ")";
    }

    /**
     * Returns this deadline in the format used by the save file.
     *
     * @return the serialized deadline
     */
    @Override
    public String toFileFormat() {
        return "D | " + (isDone ? "1" : "0") + " | " + this.name + " | "
                + this.by.format(FormatUtils.INPUT_SAVE_FORMAT);
    }
}
