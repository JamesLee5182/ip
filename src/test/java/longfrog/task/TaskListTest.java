package longfrog.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class TaskListTest {
    @Test
    void constructor_nullLoadedTasks_createsEmptyList() {
        TaskList taskList = new TaskList(null);

        assertEquals(0, taskList.getCount());
        assertTrue(taskList.getAll().isEmpty());
    }

    @Test
    void addToList_addsTaskAndMakesItRetrievable() {
        TaskList taskList = new TaskList();
        Todo todo = new Todo("read book");

        taskList.addToList(todo);

        assertEquals(1, taskList.getCount());
        assertSame(todo, taskList.getTask(0));
        assertEquals(1, taskList.getAll().size());
    }

    @Test
    void taskExistsAndGetTask_boundaryIndexes_reportCorrectResult() {
        TaskList taskList = new TaskList(new ArrayList<>());
        Todo first = new Todo("first");
        Todo second = new Todo("second");
        taskList.addToList(first);
        taskList.addToList(second);

        assertFalse(taskList.taskExists(-1));
        assertTrue(taskList.taskExists(0));
        assertTrue(taskList.taskExists(1));
        assertFalse(taskList.taskExists(2));
        assertSame(first, taskList.getTask(0));
        assertSame(second, taskList.getTask(1));
        assertNull(taskList.getTask(-1));
        assertNull(taskList.getTask(2));
    }

    @Test
    void removeTask_middleIndex_removesTaskAndReindexesFollowingTasks() {
        TaskList taskList = new TaskList();
        Todo first = new Todo("first");
        Todo middle = new Todo("middle");
        Todo last = new Todo("last");
        taskList.addToList(first);
        taskList.addToList(middle);
        taskList.addToList(last);

        Task removedTask = taskList.removeTask(1);

        assertSame(middle, removedTask);
        assertEquals(2, taskList.getCount());
        assertSame(first, taskList.getTask(0));
        assertSame(last, taskList.getTask(1));
    }

    @Test
    void removeTask_invalidIndex_returnsNullWithoutChangingList() {
        TaskList taskList = new TaskList();
        Todo todo = new Todo("read book");
        taskList.addToList(todo);

        assertNull(taskList.removeTask(-1));
        assertNull(taskList.removeTask(1));
        assertEquals(1, taskList.getCount());
        assertSame(todo, taskList.getTask(0));
    }
}
