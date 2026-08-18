/**
 * Handles the parsing of raw user text inputs into executable {@link Command} objects.
 */
public class Parser {
    private final TaskList taskList;

    public Parser(TaskList taskList) {
        this.taskList = taskList;
    }

    /**
     * Parses the user input string and returns the corresponding {@code Command}.
     * @param fullInput The complete raw text entered by the user.
     * @return A {@link Command} ready for execution.
     */
    public Command parse(String fullInput) {
        String cleanInput = fullInput.trim();

        String[] words = cleanInput.split(" ", 2);
        String keyword = words[0].toLowerCase();

        switch (keyword) {
            case "bye":
                return new ExitCommand();
            case "list":
                return new ListCommand(this.taskList);
            case "mark":
                int markIndex = parseIndex(words);
                return new MarkCommand(this.taskList, markIndex);
            case "unmark":
                int unmarkIndex = parseIndex(words);
                return new UnmarkCommand(this.taskList, unmarkIndex);
            default:
                return new AddCommand(this.taskList, fullInput);
        }
    }

    /**
     * Extracts and validates the integer index from the command arguments.
     * @param words The split input array containing the keyword and argument.
     * @return The 0-based task index.
     */
    private int parseIndex(String[] words) throws IllegalArgumentException {
        // Check if the user didn't supply an argument
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new IllegalArgumentException("Dude can you specify a task number like: " + words[0] + " 1");
        }

        try {
            int userIndex = Integer.parseInt(words[1].trim());
            return userIndex - 1;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The task number must be a valid integer!");
        }
    }
}
