package longfrog.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import longfrog.exception.LongfrogException;
import longfrog.task.Deadline;
import longfrog.task.Event;
import longfrog.task.Task;
import longfrog.task.Todo;

class StorageTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void load_missingFile_createsEmptyFileAndReturnsEmptyList() throws LongfrogException {
        Path saveFile = temporaryDirectory.resolve("new-data/tasks.txt");

        List<Task> loadedTasks = new Storage(saveFile.toString()).load();

        assertTrue(Files.isRegularFile(saveFile));
        assertTrue(loadedTasks.isEmpty());
    }

    @Test
    void saveThenLoad_tasksOfEveryType_preservesTaskDetailsAndCompletionState() throws LongfrogException {
        Path saveFile = temporaryDirectory.resolve("tasks.txt");
        Todo todo = new Todo("read book");
        Deadline deadline = new Deadline("submit report", LocalDateTime.of(2019, 12, 2, 18, 0));
        Event event = new Event("team meeting", LocalDateTime.of(2019, 12, 2, 14, 0),
                LocalDateTime.of(2019, 12, 2, 16, 0));
        deadline.markAsDone();
        event.markAsDone();

        Storage storage = new Storage(saveFile.toString());
        assertTrue(storage.save(List.of(todo, deadline, event)));

        List<Task> loadedTasks = storage.load();

        assertEquals(List.of(todo.toFileFormat(), deadline.toFileFormat(), event.toFileFormat()),
                loadedTasks.stream().map(Task::toFileFormat).toList());
        assertInstanceOf(Todo.class, loadedTasks.get(0));
        assertInstanceOf(Deadline.class, loadedTasks.get(1));
        assertInstanceOf(Event.class, loadedTasks.get(2));
    }

    @Test
    void load_mixedValidAndCorruptedEntries_recoversOnlyValidTasks() throws IOException, LongfrogException {
        Path saveFile = temporaryDirectory.resolve("tasks.txt");
        Files.writeString(saveFile, String.join(System.lineSeparator(),
                "T | 0 | read book",
                "not a saved task",
                "D | 1 | submit report | not a date",
                "X | 0 | unknown type",
                "E | 1 | team meeting | 2/12/2019 1400 | 2/12/2019 1600"));

        List<Task> loadedTasks = new Storage(saveFile.toString()).load();

        assertEquals(2, loadedTasks.size());
        assertEquals("T | 0 | read book", loadedTasks.get(0).toFileFormat());
        assertEquals("E | 1 | team meeting | 2/12/2019 1400 | 2/12/2019 1600",
                loadedTasks.get(1).toFileFormat());
    }

    @Test
    void save_existingFile_replacesItsPreviousContents() throws IOException {
        Path saveFile = temporaryDirectory.resolve("tasks.txt");
        Files.writeString(saveFile, "T | 0 | old task");

        boolean wasSaved = new Storage(saveFile.toString()).save(List.of(new Todo("new task")));

        assertTrue(wasSaved);
        assertEquals("T | 0 | new task" + System.lineSeparator(), Files.readString(saveFile));
    }

    @Test
    void save_pathThatIsDirectory_returnsFalse() throws IOException {
        Path directory = Files.createDirectory(temporaryDirectory.resolve("not-a-file"));

        boolean wasSaved = new Storage(directory.toString()).save(List.of(new Todo("task")));

        assertFalse(wasSaved);
    }
}
