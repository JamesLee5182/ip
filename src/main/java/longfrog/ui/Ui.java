package longfrog.ui;

import java.util.Scanner;

/**
 * Handles all console input and output for Longfrog.
 */
public class Ui {
    private static final String SEPARATOR = "─".repeat(50);
    private final Scanner scanner;

    /**
     * Creates a UI that reads commands from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Returns the next command entered by the user. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays the initial greeting. */
    public void showGreeting() {
        showMessage(" _                             __                    \n"
                + "| |       ___   _ __    __ _  / _| _ __   ___    __ _ \n"
                + "| |      / _ \\ | '_ \\  / _` || |_ | '__| / _ \\  / _` |\n"
                + "| |___  | (_) || | | || (_| ||  _|| |   | (_) || (_| |\n"
                + "|_____|  \\___/ |_| |_| \\__, ||_|  |_|    \\___/  \\__, |\n"
                + "                       |___/                    |___/ \n");
        showLine();
        showMessage("I am Longfrog.Longfrog.\nWhat do you want?");
        showLine();
    }

    /** Displays the farewell message. */
    public void showExitMessage() {
        showMessage("I'm going to sleep. Bye.");
    }

    /** Displays a message when saved tasks cannot be loaded. */
    public void showLoadingError() {
        showMessage("Warning: Unable to load save file. Starting with an empty list.");
    }

    /** Displays a message when tasks cannot be saved. */
    public void showSavingError() {
        showMessage("Error: Failed to save tasks to file.");
    }

    /** Displays a message on the console. */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /** Displays the separator used around command responses. */
    public void showLine() {
        showMessage(SEPARATOR);
    }

    /** Displays an empty line. */
    public void showEmptyLine() {
        showMessage("");
    }

    /** Closes the input reader after the application exits. */
    public void close() {
        scanner.close();
    }
}
