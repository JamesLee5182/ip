package longfrog.command;
import longfrog.task.*;
import longfrog.Ui;

/** Displays every task currently in the task list. */
public class ListCommand implements Command {
    private TaskList taskList;

    public ListCommand(TaskList taskList) {
        this.taskList = taskList;
    }

    /** Displays the task list or its empty-list message. */
    @Override
    public boolean execute(Ui ui) {
        ui.showMessage("ok buddy");

        int count = taskList.getCount();
        if (count == 0) {
            ui.showMessage("you didn't anything yet. What do you want from me?");
        } else {
            for (int i = 0; i < count; i++) {
                Task task = taskList.getTask(i);
                if (task == null) break;

                ui.showMessage((i + 1) + ": " + task);
            }
        }

        return false;
    }
}
