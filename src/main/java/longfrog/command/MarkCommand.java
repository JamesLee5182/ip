package longfrog.command;

import longfrog.task.Task;
import longfrog.task.TaskList;
import longfrog.ui.Ui;

/** Marks the selected task as complete. */
public class MarkCommand implements Command {
    private final Task task;

    /**
     * Creates a command that marks the task at the specified index.
     *
     * @param taskList the task list containing the target task
     * @param index the zero-based task index
     */
    public MarkCommand(TaskList taskList, int index) {
        this.task = taskList.getTask(index);
    }

    /** Marks the task and displays the outcome. */
    @Override
    public boolean execute(Ui ui) {
        if (task == null) {
            ui.showMessage("Index error: no task exists at that position. Ribbit.");
        } else if (task.isDone()) {
            ui.showMessage("That task is already marked done: " + task.getName());
        } else {
            task.markAsDone();
            ui.showMessage("Caught it. Marked done: " + task.getName());
        }

        return false;
    }
}
