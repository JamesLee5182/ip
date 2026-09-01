package longfrog.command;
import longfrog.task.Task;
import longfrog.task.TaskList;
import longfrog.ui.Ui;

/** Adds one task and reports the added task through the UI. */
public class AddCommand implements Command {
    private final TaskList taskList;
    private final Task task;

    /**
     * Creates a command that adds a task to the supplied list.
     *
     * @param taskList the list that receives the task
     * @param task the task to add
     */
    public AddCommand(TaskList taskList, Task task) {
        this.taskList = taskList;
        this.task = task;
    }

    /** Adds the task and displays a confirmation. */
    @Override
    public boolean execute(Ui ui) {
        taskList.addToList(task);
        ui.showMessage("Ribbit! Task compiled into the list: " + task);
        return false;
    }
}
