package longfrog;

import longfrog.command.Command;
import longfrog.exception.LongfrogException;
import longfrog.parser.Parser;
import longfrog.task.TaskList;
import java.util.Scanner;
import longfrog.storage.Storage;

public class Longfrog {
    private static final String FILE_PATH = "data/longfrog.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage(FILE_PATH);
        TaskList taskList = new TaskList(storage.load());
        Parser parser = new Parser(taskList);

        printGreetingMessage();

        boolean isExit = false;
        while (!isExit) {
            String userInput = scanner.nextLine();

            printLine();
            try {
                Command c = parser.parse(userInput);
                isExit = c.execute();
                storage.save(taskList.getAll());
            } catch (LongfrogException e) {
                printMessage(e.getMessage());
            }

            printEmptyLine();
            printLine();
            printEmptyLine();
        }

        scanner.close();
    }

    public static void printGreetingMessage()
    {
        String greeting =
                " _                             __                    \n"
                + "| |       ___   _ __    __ _  / _| _ __   ___    __ _ \n"
                + "| |      / _ \\ | '_ \\  / _` || |_ | '__| / _ \\  / _` |\n"
                + "| |___  | (_) || | | || (_| ||  _|| |   | (_) || (_| |\n"
                + "|_____|  \\___/ |_| |_| \\__, ||_|  |_|    \\___/  \\__, |\n"
                + "                       |___/                    |___/ \n";
        printMessage(greeting);
        printLine();
        printMessage("I am Longfrog.Longfrog.\nWhat do you want?");
        printLine();
    }

    public static void printExitMessage() {
        printMessage("I'm going to sleep. Bye.");
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void printEmptyLine() {
        printMessage("");
    }

    public static void printLine() {
        printMessage("─".repeat(50));
    }
}
