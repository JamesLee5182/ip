package longfrog.command;
import longfrog.task.Task;
import longfrog.task.TaskList;
import longfrog.ui.Ui;

/** Marks the selected task as incomplete. */
public class UnmarkCommand implements Command {
    private final Task task;

    public UnmarkCommand(TaskList taskList, int index) {
        this.task = taskList.getTask(index);
    }

    /** Unmarks the task and displays the outcome. */
    @Override
    public boolean execute(Ui ui) {
        if (task == null) {
            ui.showMessage("I can't do that. The task doesn't exist");
        } else {
            task.unmarkAsDone();
            ui.showMessage("I unmarked the task: " + task.getName());
        }

        return false;
    }
}
