package longfrog.command;

import java.time.LocalDate;
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
        List<Task> matchingTasks = taskList.getAll().stream()
                .filter(this::occursOnTargetDate)
                .toList();

        String formattedDate = targetDate.format(FormatUtils.DATE_ONLY_FORMAT);
        if (matchingTasks.isEmpty()) {
            ui.showMessage("Temporal query returned zero tasks for " + formattedDate + ".");
        } else {
            ui.showMessage("Temporal query complete for " + formattedDate + ":");
            for (int i = 0; i < matchingTasks.size(); i++) {
                ui.showMessage((i + 1) + ": " + matchingTasks.get(i));
            }
        }

        return false;
    }

    /** Returns whether a deadline or event occurs on the selected date. */
    private boolean occursOnTargetDate(Task task) {
        if (task instanceof Deadline deadline) {
            return deadline.getBy().toLocalDate().equals(targetDate);
        }

        if (task instanceof Event event) {
            LocalDate fromDate = event.getFrom().toLocalDate();
            LocalDate toDate = event.getTo().toLocalDate();
            return !targetDate.isBefore(fromDate) && !targetDate.isAfter(toDate);
        }

        return false;
    }
}
