package longfrog.command;
import longfrog.task.*;
import longfrog.Longfrog;

public class MarkCommand implements Command {
    private final Task task;

    public MarkCommand(TaskList taskList, int index) {
        this.task = taskList.getTask(index);
    }

    @Override
    public boolean execute() {
        if (task == null) {
            Longfrog.printMessage("I can't do that. The task doesn't exist");
        } else {
            task.markAsDone();
            Longfrog.printMessage("I marked the task: " + task.getName());
        }

        return false;
    }
}
