package longfrog.task;

/** Represents a task with a name and completion state. */
public abstract class Task {
    protected String name;
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given name.
     *
     * @param name the task description
     */
    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the symbol used to show this task's completion state.
     *
     * @return {@code "X"} when complete, otherwise a space
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /** Marks this task as complete. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void unmarkAsDone() {
        isDone = false;
    }

    /**
     * Returns the completion state and description used by concrete task displays.
     *
     * @return the formatted completion state and task description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.name;
    }

    /**
     * Formats the task as a delimited string for saving to disk.
     */
    public abstract String toFileFormat();
}
