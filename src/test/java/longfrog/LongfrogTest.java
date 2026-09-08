package longfrog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LongfrogTest {
    private static final String TASK_FILE_NAME = "tasks.txt";

    @TempDir
    Path temporaryDirectory;

    @Test
    void getResponse_todoCommand_returnsConfirmationAndUpdatesList() {
        Longfrog longfrog = createLongfrog();

        String response = longfrog.getResponse("todo read book");

        assertEquals("Ribbit! Task compiled into the list: [T][ ] read book", response);
        assertEquals("Task database snapshot:" + System.lineSeparator() + "1: [T][ ] read book",
                longfrog.getResponse("list"));
        assertFalse(longfrog.isExitRequested());
    }

    @Test
    void getResponse_invalidCommand_returnsParserErrorWithoutAddingTask() {
        Longfrog longfrog = createLongfrog();

        assertEquals("Unknown command token. My parser cannot compute that, ribbit.",
                longfrog.getResponse("dance"));
        assertEquals("Task database snapshot:" + System.lineSeparator()
                + "No tasks detected; the queue is an empty set. Ribbit.", longfrog.getResponse("list"));
    }

    @Test
    void getResponse_byeCommand_returnsFarewellAndRequestsExit() {
        Longfrog longfrog = createLongfrog();

        assertEquals("Ribbit and good night! Shutting down the lily-pad terminal.", longfrog.getResponse("bye"));
        assertTrue(longfrog.isExitRequested());
    }

    @Test
    void getResponse_duplicateTodo_rejectsTaskAndPreservesList() {
        Longfrog longfrog = createLongfrog();
        longfrog.getResponse("todo Read Book");

        assertEquals("Duplicate detected; task already exists at position 1: [T][ ] Read Book",
                longfrog.getResponse("todo read   book"));
        assertEquals("Task database snapshot:" + System.lineSeparator() + "1: [T][ ] Read Book",
                longfrog.getResponse("list"));
    }

    @Test
    void getStartupWarning_duplicateSavedTasks_reportsCountWithoutChangingCommandResponse() throws IOException {
        Path saveFile = temporaryDirectory.resolve(TASK_FILE_NAME);
        Files.writeString(saveFile, String.join(System.lineSeparator(),
                "T | 0 | Read Book",
                "T | 1 | read   book",
                "T | 0 | READ BOOK"));

        Longfrog longfrog = new Longfrog(saveFile.toString());

        assertEquals("Warning: 2 duplicate task entries detected in saved data. Existing entries were preserved.",
                longfrog.getStartupWarning());
        assertEquals("Task database snapshot:" + System.lineSeparator()
                + "1: [T][ ] Read Book" + System.lineSeparator()
                + "2: [T][X] read   book" + System.lineSeparator()
                + "3: [T][ ] READ BOOK", longfrog.getResponse("list"));
    }

    @Test
    void getStartupWarning_oneOrNoDuplicate_usesSingularOrNoWarning() throws IOException {
        Path saveFile = temporaryDirectory.resolve(TASK_FILE_NAME);
        Files.writeString(saveFile, String.join(System.lineSeparator(),
                "T | 0 | task",
                "T | 1 | TASK"));
        Longfrog longfrogWithDuplicate = new Longfrog(saveFile.toString());

        assertEquals("Warning: 1 duplicate task entry detected in saved data. Existing entries were preserved.",
                longfrogWithDuplicate.getStartupWarning());

        Files.writeString(saveFile, "T | 0 | unique");
        Longfrog longfrogWithoutDuplicate = new Longfrog(saveFile.toString());
        assertEquals("", longfrogWithoutDuplicate.getStartupWarning());
    }

    private Longfrog createLongfrog() {
        return new Longfrog(temporaryDirectory.resolve(TASK_FILE_NAME).toString());
    }
}
