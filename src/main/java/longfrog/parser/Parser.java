package longfrog.parser;

import longfrog.command.*;
import longfrog.exception.LongfrogException;
import longfrog.task.*;
import longfrog.util.FormatUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

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
            throw new LongfrogException("Bruh, can you enter a command?");
        }

        String[] words = cleanInput.split(" ", 2);
        CommandType commandType = CommandType.fromKeyword(words[0])
                .orElseThrow(() -> new LongfrogException("Bruh, I don't know that command."));

        switch (commandType) {
            case BYE:
                return new ExitCommand();

            case TODO:
                String todoName = getArgument(words, "todo TASK");
                return new AddCommand(this.taskList, new Todo(todoName));

            case DEADLINE:
                String[] deadlineParts = getArgument(words, "deadline TASK /by d/M/yyyy HHmm").split(" /by ", 2);
                if (deadlineParts.length < 2 || deadlineParts[0].isBlank() || deadlineParts[1].isBlank()) {
                    throw new LongfrogException("Dude, use: deadline TASK /by d/M/yyyy HHmm");
                }
                String deadlineName = deadlineParts[0].trim();
                LocalDateTime by = parseDateTime(deadlineParts[1].trim());

                return new AddCommand(this.taskList, new Deadline(deadlineName, by));

            case EVENT:
                String[] eventParts = getArgument(words, "event TASK /from d/M/yyyy HHmm /to d/M/yyyy HHmm").split(" /from ", 2);
                if (eventParts.length < 2 || eventParts[0].isBlank() || eventParts[1].isBlank()) {
                    throw new LongfrogException("Dude, use: event TASK /from d/M/yyyy HHmm /to d/M/yyyy HHmm");
                }
                String eventName = eventParts[0].trim();

                String[] timeParts = eventParts[1].split(" /to ", 2);
                if (timeParts.length < 2 || timeParts[0].isBlank() || timeParts[1].isBlank()) {
                    throw new LongfrogException("Dude, use: event TASK /from d/M/yyyy HHmm /to d/M/yyyy HHmm");
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

            case CHECK: // or case "check":
                String dateString = getArgument(words, "check d/M/yyyy (e.g., check 2/12/2019)");
                LocalDate targetDate = parseDate(dateString);
                return new CheckCommand(this.taskList, targetDate);

            default:
                throw new LongfrogException("Dude, I don't know that command.");
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
            throw new LongfrogException("Dude, use: " + usage);
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
            throw new LongfrogException("Dude, invalid date format! Use: d/M/yyyy HHmm (e.g., 2/12/2019 1800)");
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
            throw new LongfrogException("Dude, invalid date format! Use: d/M/yyyy (e.g., 2/12/2019)");
        }
    }

    /**
     * Extracts and validates the integer index from the command arguments.
     * @param words The split input array containing the keyword and argument.
     * @return The 0-based task index.
     */
    private int parseIndex(String[] words) throws LongfrogException {
        // Check if the user didn't supply an argument
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new LongfrogException("Dude can you specify a task number like: " + words[0] + " 1");
        }

        try {
            int userIndex = Integer.parseInt(words[1].trim());
            if (userIndex <= 0) {
                throw new LongfrogException("Dude, task numbers start at 1.");
            }
            return userIndex - 1;
        } catch (NumberFormatException e) {
            throw new LongfrogException("Dude, the task number must be a valid integer!");
        }
    }
}
