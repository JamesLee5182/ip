package longfrog.command;

import longfrog.Ui;
import longfrog.task.Deadline;
import longfrog.task.Event;
import longfrog.task.Task;
import longfrog.task.TaskList;
import longfrog.util.FormatUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Lists deadlines and events that occur on a selected date. */
public class CheckCommand implements Command {
    private final TaskList taskList;
    private final LocalDate targetDate;

    public CheckCommand(TaskList taskList, LocalDate targetDate) {
        this.taskList = taskList;
        this.targetDate = targetDate;
    }

    /** Finds matching tasks and displays them through the UI. */
    @Override
    public boolean execute(Ui ui) {
        List<Task> matchingTasks = new ArrayList<>();

        for (int i = 0; i < taskList.getCount(); i++) {
            Task task = taskList.getTask(i);
            if (task == null) continue;

            // 1. Check if it is a Deadline on this date
            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                if (deadline.getBy().toLocalDate().equals(targetDate)) {
                    matchingTasks.add(task);
                }
            }
            // 2. Check if it is an Event occurring on this date
            else if (task instanceof Event) {
                Event event = (Event) task;
                LocalDate fromDate = event.getFrom().toLocalDate();
                LocalDate toDate = event.getTo().toLocalDate();

                // Target date falls within the event's start and end dates (inclusive)
                if (!targetDate.isBefore(fromDate) && !targetDate.isAfter(toDate)) {
                    matchingTasks.add(task);
                }
            }
        }

        // Print results
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
