package longfrog.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the tasks in their display order.
 *
 * <p>An {@link ArrayList} is used so later delete operations can remove a task and shift following
 * tasks without manually managing array capacity or indices.</p>
 */
public class TaskList {
    private final List<Task> list;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.list = new ArrayList<>();
    }

    /**
     * Returns the number of tasks currently stored.
     *
     * @return the number of tasks
     */
    public int getCount() {
        return list.size();
    }

    /**
     * Appends a task to the end of the list.
     *
     * @param task the task to add
     */
    public void addToList(Task task) {
        list.add(task);
    }

    /**
     * Removes and returns the task at a zero-based index.
     *
     * @param index the zero-based task index
     * @return the removed task, or {@code null} when the index is invalid
     */
    public Task removeTask(int index) {
        if (!taskExists(index)) {
            return null;
        }

        return list.remove(index);
    }

    /**
     * Checks whether a zero-based index identifies a stored task.
     *
     * @param index the zero-based task index
     * @return whether the index is within the list bounds
     */
    public boolean taskExists(int index) {
        return index >= 0 && index < list.size();
    }

    /**
     * Returns the task at a zero-based index, if present.
     *
     * @param index the zero-based task index
     * @return the task, or {@code null} when the index is invalid
     */
    public Task getTask(int index) {
        if (!taskExists(index)) {
            return null;
        }

        return list.get(index);
    }
}
