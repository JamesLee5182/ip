package longfrog.command;

import longfrog.task.Task;
import longfrog.task.TaskList;
import longfrog.ui.Ui;

/** Marks the selected task as incomplete. */
public class UnmarkCommand implements Command {
    private final Task task;

    /**
     * Creates a command that marks the task at the specified index as unfinished.
     *
     * @param taskList the task list containing the target task
     * @param index the zero-based task index
     */
    public UnmarkCommand(TaskList taskList, int index) {
        this.task = taskList.getTask(index);
    }

    /** Unmarks the task and displays the outcome. */
    @Override
    public boolean execute(Ui ui) {
        if (task == null) {
            ui.showMessage("Index error: no task exists at that position. Ribbit.");
        } else if (!task.isDone()) {
            ui.showMessage("That task is already unfinished: " + task.getName());
        } else {
            task.unmarkAsDone();
            ui.showMessage("Back into the pond: " + task.getName());
        }

        return false;
    }
}
