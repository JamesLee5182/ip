package longfrog.command;
import longfrog.task.*;
import longfrog.Longfrog;

public class AddCommand implements Command {
    private final TaskList taskList;
    private final Task task;

    public AddCommand(TaskList taskList, Task task) {
        this.taskList = taskList;
        this.task = task;
    }

    @Override
    public boolean execute() {
        taskList.addToList(task);
        Longfrog.printMessage("Sure dude. I added: " + task.getName());
        return false;
    }
}
