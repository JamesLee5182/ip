import java.util.Scanner;

public class Longfrog {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printGreetingMessage();

        boolean isExit = false;

        while (!isExit) {
            String userInput = scanner.nextLine();

            Command c = Parser.parse(userInput);
            isExit = c.execute();
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
        printMessage("I am Longfrog.\nWhat do you want?");
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