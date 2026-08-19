package longfrog.command;
import longfrog.task.*;
import longfrog.Longfrog;

public class AddCommand implements Command {
    private TaskList taskList;
    private Task task;

    public AddCommand(TaskList taskList, String input) {
        this.taskList = taskList;
        this.task = new Task(input);
    }

    @Override
    public boolean execute() {
        taskList.addToList(task);
        Longfrog.printMessage("Sure dude. I added: " + task.getName());
        return false;
    }
}
