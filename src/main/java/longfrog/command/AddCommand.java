package longfrog.command;
import longfrog.task.*;
import longfrog.Ui;

/** Adds one task and reports the added task through the UI. */
public class AddCommand implements Command {
    private final TaskList taskList;
    private final Task task;

    public AddCommand(TaskList taskList, Task task) {
        this.taskList = taskList;
        this.task = task;
    }

    /** Adds the task and displays a confirmation. */
    @Override
    public boolean execute(Ui ui) {
        taskList.addToList(task);
        ui.showMessage("Sure dude. I added: " + task);
        return false;
    }
}
