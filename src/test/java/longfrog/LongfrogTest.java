package longfrog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    private Longfrog createLongfrog() {
        return new Longfrog(temporaryDirectory.resolve(TASK_FILE_NAME).toString());
    }
}
