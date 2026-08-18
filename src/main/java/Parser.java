/**
 * Handles the parsing of raw user text inputs into executable {@link Command} objects.
 */
public class Parser {

    /**
     * Parses the user input string and returns the corresponding {@code Command}.
     * @param fullInput The complete raw text entered by the user.
     * @return A {@link Command} ready for execution.
     */
    public static Command parse(String fullInput) {
        String cleanInput = fullInput.trim();

        String[] words = cleanInput.split(" ", 2);
        String keyword = words[0].toLowerCase();

        switch (keyword) {
            case "bye":
                return new ExitCommand();
            default:
                return new EchoCommand(fullInput);
        }
    }
}
