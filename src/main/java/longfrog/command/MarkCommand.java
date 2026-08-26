package longfrog.command;
import longfrog.task.Task;
import longfrog.task.TaskList;
import longfrog.ui.Ui;

/** Marks the selected task as complete. */
public class MarkCommand implements Command {
    private final Task task;

    public MarkCommand(TaskList taskList, int index) {
        this.task = taskList.getTask(index);
    }

    /** Marks the task and displays the outcome. */
    @Override
    public boolean execute(Ui ui) {
        if (task == null) {
            ui.showMessage("I can't do that. The task doesn't exist");
        } else {
            task.markAsDone();
            ui.showMessage("I marked the task: " + task.getName());
        }

        return false;
    }
}
