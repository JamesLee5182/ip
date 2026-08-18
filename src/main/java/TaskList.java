public class TaskList {
    private static int maxCap = 100;
    private Task[] list;
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

    public Task getTask(int i) {
        if (i >= maxCap) return null;

        return list[i];
    }
}
