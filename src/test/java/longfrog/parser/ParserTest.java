package longfrog.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import longfrog.command.AddCommand;
import longfrog.command.CheckCommand;
import longfrog.command.DeleteCommand;
import longfrog.command.ExitCommand;
import longfrog.command.ListCommand;
import longfrog.command.MarkCommand;
import longfrog.command.UnmarkCommand;
import longfrog.exception.LongfrogException;
import longfrog.task.TaskList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParserTest {
    private Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser(new TaskList());
    }

    @Test
    void parse_validCommands_returnsExpectedCommandTypes() throws LongfrogException {
        assertInstanceOf(ExitCommand.class, parser.parse("bye"));
        assertInstanceOf(AddCommand.class, parser.parse("todo read book"));
        assertInstanceOf(AddCommand.class, parser.parse("deadline submit report /by 2/12/2019 1800"));
        assertInstanceOf(AddCommand.class,
                parser.parse("event team meeting /from 2/12/2019 1400 /to 2/12/2019 1600"));
        assertInstanceOf(ListCommand.class, parser.parse("list"));
        assertInstanceOf(MarkCommand.class, parser.parse("mark 1"));
        assertInstanceOf(UnmarkCommand.class, parser.parse("unmark 1"));
        assertInstanceOf(DeleteCommand.class, parser.parse("delete 1"));
        assertInstanceOf(CheckCommand.class, parser.parse("check 2/12/2019"));
    }

    @Test
    void parse_commandKeywordWithDifferentCase_returnsExpectedCommand() throws LongfrogException {
        assertInstanceOf(AddCommand.class, parser.parse("TODO Read Book"));
    }

    @Test
    void parse_inputWithSurroundingWhitespace_returnsExpectedCommand() throws LongfrogException {
        assertInstanceOf(ListCommand.class, parser.parse("  list  "));
    }

    @Test
    void parse_blankInput_throwsExceptionWithHelpfulMessage() {
        assertParseError("   ", "Bruh, can you enter a command?");
    }

    @Test
    void parse_unknownCommand_throwsExceptionWithHelpfulMessage() {
        assertParseError("dance", "Bruh, I don't know that command.");
    }

    @Test
    void parse_todoWithoutTask_throwsExceptionWithUsage() {
        assertParseError("todo", "Dude, use: todo TASK");
    }

    @Test
    void parse_deadlineWithMissingOrInvalidDate_throwsExceptionWithHelpfulMessage() {
        assertParseError("deadline submit report", "Dude, use: deadline TASK /by d/M/yyyy HHmm");
        assertParseError("deadline submit report /by tomorrow",
                "Dude, invalid date format! Use: d/M/yyyy HHmm (e.g., 2/12/2019 1800)");
    }

    @Test
    void parse_eventWithMissingOrInvalidTimes_throwsExceptionWithHelpfulMessage() {
        assertParseError("event meeting /from 2/12/2019 1400",
                "Dude, use: event TASK /from d/M/yyyy HHmm /to d/M/yyyy HHmm");
        assertParseError("event meeting /from tomorrow /to 2/12/2019 1600",
                "Dude, invalid date format! Use: d/M/yyyy HHmm (e.g., 2/12/2019 1800)");
    }

    @Test
    void parse_checkWithMissingOrInvalidDate_throwsExceptionWithHelpfulMessage() {
        assertParseError("check", "Dude, use: check d/M/yyyy (e.g., check 2/12/2019)");
        assertParseError("check tomorrow", "Dude, invalid date format! Use: d/M/yyyy (e.g., 2/12/2019)");
    }

    @Test
    void parse_taskCommandsWithInvalidIndex_throwsExceptionWithHelpfulMessage() {
        assertParseError("mark", "Dude can you specify a task number like: mark 1");
        assertParseError("unmark 0", "Dude, task numbers start at 1.");
        assertParseError("delete -1", "Dude, task numbers start at 1.");
        assertParseError("mark first", "Dude, the task number must be a valid integer!");
    }

    private void assertParseError(String input, String expectedMessage) {
        LongfrogException exception = assertThrows(LongfrogException.class, () -> parser.parse(input));
        assertEquals(expectedMessage, exception.getMessage());
    }
}
