package longfrog.command;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import longfrog.task.Deadline;
import longfrog.task.Event;
import longfrog.task.Task;
import longfrog.task.TaskList;
import longfrog.ui.Ui;
import longfrog.util.FormatUtils;

/** Lists deadlines and events that occur on a selected date. */
public class DateCommand implements Command {
    private final TaskList taskList;
    private final LocalDate targetDate;

    /**
     * Creates a date command for the selected date.
     *
     * @param taskList the tasks to inspect
     * @param targetDate the date to search
     */
    public DateCommand(TaskList taskList, LocalDate targetDate) {
        this.taskList = taskList;
        this.targetDate = targetDate;
    }

    /** Finds matching deadlines and events and displays them through the UI. */
    @Override
    public boolean execute(Ui ui) {
        List<Task> matchingTasks = new ArrayList<>();

        for (int i = 0; i < taskList.getCount(); i++) {
            Task task = taskList.getTask(i);
            if (task == null) {
                continue;
            }

            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                if (deadline.getBy().toLocalDate().equals(targetDate)) {
                    matchingTasks.add(task);
                }
            } else if (task instanceof Event) {
                Event event = (Event) task;
                LocalDate fromDate = event.getFrom().toLocalDate();
                LocalDate toDate = event.getTo().toLocalDate();

                if (!targetDate.isBefore(fromDate) && !targetDate.isAfter(toDate)) {
                    matchingTasks.add(task);
                }
            }
        }

        String formattedDate = targetDate.format(FormatUtils.DATE_ONLY_FORMAT);
        if (matchingTasks.isEmpty()) {
            ui.showMessage("No deadlines or events found on " + formattedDate + ".");
        } else {
            ui.showMessage("Here are the tasks happening on " + formattedDate + ":");
            for (int i = 0; i < matchingTasks.size(); i++) {
                ui.showMessage((i + 1) + ": " + matchingTasks.get(i));
            }
        }

        return false;
    }
}
