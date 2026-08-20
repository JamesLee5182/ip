package longfrog.command;

import longfrog.Longfrog;
import longfrog.task.Task;
import longfrog.task.TaskList;

/**
 * Removes a task selected by its zero-based position in the task list.
 */
public class DeleteCommand implements Command {
    private final TaskList taskList;
    private final int index;

    /**
     * Creates a delete command for a task position.
     *
     * @param taskList the list from which to remove a task
     * @param index the zero-based position to remove
     */
    public DeleteCommand(TaskList taskList, int index) {
        this.taskList = taskList;
        this.index = index;
    }

    /**
     * Removes the selected task and reports the outcome to the user.
     *
     * @return {@code false} because deleting a task does not exit Longfrog
     */
    @Override
    public boolean execute() {
        Task deletedTask = taskList.removeTask(index);
        if (deletedTask == null) {
            Longfrog.printMessage("I can't do that. The task doesn't exist");
        } else {
            Longfrog.printMessage("Sure dude. I deleted: " + deletedTask.getName());
        }

        return false;
    }
}
