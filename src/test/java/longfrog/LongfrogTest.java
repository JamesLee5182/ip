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

        assertEquals("Sure dude. I added: [T][ ] read book", response);
        assertEquals("ok buddy" + System.lineSeparator() + "1: [T][ ] read book",
                longfrog.getResponse("list"));
        assertFalse(longfrog.isExitRequested());
    }

    @Test
    void getResponse_invalidCommand_returnsParserErrorWithoutAddingTask() {
        Longfrog longfrog = createLongfrog();

        assertEquals("Bruh, I don't know that command.", longfrog.getResponse("dance"));
        assertEquals("ok buddy" + System.lineSeparator()
                + "you didn't anything yet. What do you want from me?", longfrog.getResponse("list"));
    }

    @Test
    void getResponse_byeCommand_returnsFarewellAndRequestsExit() {
        Longfrog longfrog = createLongfrog();

        assertEquals("I'm going to sleep. Bye.", longfrog.getResponse("bye"));
        assertTrue(longfrog.isExitRequested());
    }

    private Longfrog createLongfrog() {
        return new Longfrog(temporaryDirectory.resolve(TASK_FILE_NAME).toString());
    }
}
