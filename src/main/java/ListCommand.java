public class ListCommand implements Command {
    private TaskList taskList;

    public ListCommand(TaskList taskList) {
        this.taskList = taskList;
    }

    @Override
    public boolean execute() {
        Longfrog.printMessage("ok buddy");

        int count = taskList.getCount();
        if (count == 0) {
            Longfrog.printMessage("you didn't anything yet. What do you want from me?");
        } else {
            for (int i = 0; i < count; i++) {
                Task task = taskList.getTask(i);
                if (task == null) break;

                Longfrog.printMessage((i + 1) + ": " + task);
            }
        }

        return false;
    }
}
