package longfrog.util;

import java.time.format.DateTimeFormatter;

/** Provides shared date and time formats for parsing, persistence, and display. */
public class FormatUtils {
    // Used for reading user input and saving/loading from the text file
    public static final DateTimeFormatter INPUT_SAVE_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");

    // For date only
    public static final DateTimeFormatter DATE_ONLY_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy");

    // Used for printing nicely to the UI
    public static final DateTimeFormatter PRINT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
}
