package longfrog.task;

public class TaskList {
    private final static int maxCap = 100;
    private final Task[] list;
    private int index;

    public TaskList() {
        this.list = new Task[maxCap];
        this.index = 0;
    }

    public int getCount() {
        return index;
    }

    public void addToList(Task task) {
        list[index] = task;
        index++;
    }

    public boolean taskExists(int index) {
        if (index >= maxCap || index < 0) return false;
        return list[index] != null;
    }

    public Task getTask(int i) {
        if (!taskExists(i)) return null;

        return list[i];
    }
}
