package longfrog.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import longfrog.task.TaskList;
import longfrog.task.Todo;
import longfrog.ui.Ui;

class MarkCommandTest {
    @Test
    void execute_indexBeyondTaskList_reportsValidRange() {
        TaskList taskList = new TaskList(new ArrayList<>(List.of(new Todo("swim"))));
        List<String> messages = new ArrayList<>();

        boolean shouldExit = new MarkCommand(taskList, 7).execute(new Ui(messages::add));

        assertFalse(shouldExit);
        assertEquals(List.of("There is no task 8. Choose a task number from 1 to 1."), messages);
    }

    @Test
    void execute_completedTask_preservesStateAndReportsNoChange() {
        Todo task = new Todo("swim");
        task.markAsDone();
        TaskList taskList = new TaskList(new ArrayList<>(List.of(task)));
        List<String> messages = new ArrayList<>();

        boolean shouldExit = new MarkCommand(taskList, 0).execute(new Ui(messages::add));

        assertFalse(shouldExit);
        assertTrue(task.isDone());
        assertEquals(List.of("That task is already marked done: swim"), messages);
    }
}
