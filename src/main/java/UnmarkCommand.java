public class UnmarkCommand implements Command {
    private final Task task;

    public UnmarkCommand(TaskList taskList, int index) {
        this.task = taskList.getTask(index);
    }

    @Override
    public boolean execute() {
        if (task == null) {
            Longfrog.printMessage("I can't do that. The task doesn't exist");
        } else {
            task.unmarkAsDone();
            Longfrog.printMessage("I unmarked the task: " + task.getName());
        }

        return false;
    }
}
