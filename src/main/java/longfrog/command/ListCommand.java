package longfrog.command;
import longfrog.task.Task;
import longfrog.task.TaskList;
import longfrog.ui.Ui;

/** Displays every task currently in the task list. */
public class ListCommand implements Command {
    private TaskList taskList;

    public ListCommand(TaskList taskList) {
        this.taskList = taskList;
    }

    /** Displays the task list or its empty-list message. */
    @Override
    public boolean execute(Ui ui) {
        ui.showMessage("Task database snapshot:");

        int count = taskList.getCount();
        if (count == 0) {
            ui.showMessage("No tasks detected; the queue is an empty set. Ribbit.");
        } else {
            for (int i = 0; i < count; i++) {
                Task task = taskList.getTask(i);
                if (task == null) {
                    break;
                }

                ui.showMessage((i + 1) + ": " + task);
            }
        }

        return false;
    }
}
