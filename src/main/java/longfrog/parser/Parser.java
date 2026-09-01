package longfrog.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import longfrog.command.AddCommand;
import longfrog.command.Command;
import longfrog.command.DateCommand;
import longfrog.command.DeleteCommand;
import longfrog.command.ExitCommand;
import longfrog.command.FindCommand;
import longfrog.command.ListCommand;
import longfrog.command.MarkCommand;
import longfrog.command.UnmarkCommand;
import longfrog.exception.LongfrogException;
import longfrog.task.Deadline;
import longfrog.task.Event;
import longfrog.task.TaskList;
import longfrog.task.Todo;
import longfrog.util.FormatUtils;

/**
 * Handles the parsing of raw user text inputs into executable {@link Command} objects.
 */
public class Parser {
    private final TaskList taskList;

    public Parser(TaskList taskList) {
        this.taskList = taskList;
    }

    /**
     * Parses the user input string and returns the corresponding {@code Longfrog.Commands.Command}.
     * @param fullInput The complete raw text entered by the user.
     * @return A {@link Command} ready for execution.
     * @throws LongfrogException if the input is not a complete, supported command.
     */
    public Command parse(String fullInput) throws LongfrogException {
        String cleanInput = fullInput.trim();
        if (cleanInput.isEmpty()) {
            throw new LongfrogException("Input buffer is empty. Please enter a command, ribbit.");
        }

        String[] words = cleanInput.split(" ", 2);
        CommandType commandType = CommandType.fromKeyword(words[0])
                .orElseThrow(() -> new LongfrogException(
                        "Unknown command token. My parser cannot compute that, ribbit."));

        switch (commandType) {
            case BYE:
                return new ExitCommand();

            case TODO:
                String todoName = getArgument(words, "todo TASK");
                return new AddCommand(this.taskList, new Todo(todoName));

            case DEADLINE:
                String[] deadlineParts = getArgument(words, "deadline TASK /by d/M/yyyy HHmm")
                        .split(" /by ", 2);
                if (deadlineParts.length < 2 || deadlineParts[0].isBlank() || deadlineParts[1].isBlank()) {
                    throw new LongfrogException("Syntax error. Expected: deadline TASK /by d/M/yyyy HHmm");
                }
                String deadlineName = deadlineParts[0].trim();
                LocalDateTime by = parseDateTime(deadlineParts[1].trim());

                return new AddCommand(this.taskList, new Deadline(deadlineName, by));

            case EVENT:
                String[] eventParts = getArgument(words,
                        "event TASK /from d/M/yyyy HHmm /to d/M/yyyy HHmm")
                        .split(" /from ", 2);
                if (eventParts.length < 2 || eventParts[0].isBlank() || eventParts[1].isBlank()) {
                    throw new LongfrogException(
                            "Syntax error. Expected: event TASK /from d/M/yyyy HHmm /to d/M/yyyy HHmm");
                }
                String eventName = eventParts[0].trim();

                String[] timeParts = eventParts[1].split(" /to ", 2);
                if (timeParts.length < 2 || timeParts[0].isBlank() || timeParts[1].isBlank()) {
                    throw new LongfrogException(
                            "Syntax error. Expected: event TASK /from d/M/yyyy HHmm /to d/M/yyyy HHmm");
                }
                LocalDateTime from = parseDateTime(timeParts[0].trim());
                LocalDateTime to = parseDateTime(timeParts[1].trim());

                return new AddCommand(this.taskList, new Event(eventName, from, to));

            case LIST:
                return new ListCommand(this.taskList);

            case MARK:
                int markIndex = parseIndex(words);
                return new MarkCommand(this.taskList, markIndex);

            case UNMARK:
                int unmarkIndex = parseIndex(words);
                return new UnmarkCommand(this.taskList, unmarkIndex);

            case DELETE:
                int deleteIndex = parseIndex(words);
                return new DeleteCommand(this.taskList, deleteIndex);

            case DATE:
                return parseDateCommand(words);

            case FIND:
                String keyword = getArgument(words, "find KEYWORD");
                return new FindCommand(this.taskList, keyword);

            default:
                throw new LongfrogException("Unknown command token. My parser cannot compute that, ribbit.");
        }
    }

    /**
     * Returns a non-empty command argument or reports the command's required format.
     *
     * @param words the input split into its keyword and optional argument
     * @param usage the required command format
     * @return the trimmed command argument
     * @throws LongfrogException if the argument is missing or blank
     */
    private String getArgument(String[] words, String usage) throws LongfrogException {
        if (words.length < 2 || words[1].isBlank()) {
            throw new LongfrogException("Syntax error. Expected: " + usage);
        }
        return words[1].trim();
    }

    /**
     * Parses a date-time string into a {@link LocalDateTime} object using the global format.
     *
     * @param dateTimeString the date-time text to parse (e.g., "2/12/2019 1800")
     * @return the parsed LocalDateTime object
     * @throws LongfrogException if the date-time format is invalid
     */
    private LocalDateTime parseDateTime(String dateTimeString) throws LongfrogException {
        try {
            return LocalDateTime.parse(dateTimeString, FormatUtils.INPUT_SAVE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new LongfrogException(
                    "Temporal parsing failed. Expected: d/M/yyyy HHmm (e.g., 2/12/2019 1800)");
        }
    }

    /**
     * Parses a date-only string into a {@link LocalDate} object.
     *
     * @param dateString the date string (e.g., "2/12/2019")
     * @return the parsed LocalDate
     * @throws LongfrogException if the date format is invalid
     */
    private LocalDate parseDate(String dateString) throws LongfrogException {
        try {
            return LocalDate.parse(dateString, FormatUtils.DATE_ONLY_FORMAT);
        } catch (DateTimeParseException e) {
            throw new LongfrogException("Temporal parsing failed. Expected: d/M/yyyy (e.g., 2/12/2019)");
        }
    }

    /**
     * Parses a date command.
     *
     * @param words the command keyword and date argument
     * @return a command that lists tasks occurring on the requested date
     * @throws LongfrogException if the date argument is missing or invalid
     */
    private Command parseDateCommand(String[] words) throws LongfrogException {
        String dateString = getArgument(words, "date d/M/yyyy (e.g., date 2/12/2019)");
        LocalDate targetDate = parseDate(dateString);
        return new DateCommand(this.taskList, targetDate);
    }

    /**
     * Extracts and validates the integer index from the command arguments.
     * @param words The split input array containing the keyword and argument.
     * @return The 0-based task index.
     */
    private int parseIndex(String[] words) throws LongfrogException {
        // Verify that the user supplied an argument.
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new LongfrogException("Index argument missing. Try: " + words[0] + " 1");
        }

        try {
            int userIndex = Integer.parseInt(words[1].trim());
            if (userIndex <= 0) {
                throw new LongfrogException("Index underflow: task numbers start at 1.");
            }
            return userIndex - 1;
        } catch (NumberFormatException e) {
            throw new LongfrogException("Type mismatch: task number must be an integer.");
        }
    }
}
