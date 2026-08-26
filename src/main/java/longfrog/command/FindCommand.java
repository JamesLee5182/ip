package longfrog.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import longfrog.task.Task;
import longfrog.task.TaskList;
import longfrog.ui.Ui;

/** Finds tasks whose descriptions contain a requested keyword. */
public class FindCommand implements Command {
    private final TaskList taskList;
    private final String keyword;

    /**
     * Creates a find command for a task-description keyword.
     *
     * @param taskList the tasks to search
     * @param keyword the case-insensitive description substring to find
     */
    public FindCommand(TaskList taskList, String keyword) {
        this.taskList = taskList;
        this.keyword = keyword.toLowerCase(Locale.ROOT);
    }

    /** Displays matching tasks in their original task-list order. */
    @Override
    public boolean execute(Ui ui) {
        List<Task> matchingTasks = new ArrayList<>();
        for (int i = 0; i < taskList.getCount(); i++) {
            Task task = taskList.getTask(i);
            if (task != null && task.getName().toLowerCase(Locale.ROOT).contains(keyword)) {
                matchingTasks.add(task);
            }
        }

        if (matchingTasks.isEmpty()) {
            ui.showMessage("No matching tasks found.");
        } else {
            ui.showMessage("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                ui.showMessage((i + 1) + ": " + matchingTasks.get(i));
            }
        }

        return false;
    }
}
