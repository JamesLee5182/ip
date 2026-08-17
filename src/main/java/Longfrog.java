public class Longfrog {
    public static void main(String[] args) {
        String banner = " _                             __                    \n"
                + "| |       ___   _ __    __ _  / _| _ __   ___    __ _ \n"
                + "| |      / _ \\ | '_ \\  / _` || |_ | '__| / _ \\  / _` |\n"
                + "| |___  | (_) || | | || (_| ||  _|| |   | (_) || (_| |\n"
                + "|_____|  \\___/ |_| |_| \\__, ||_|  |_|    \\___/  \\__, |\n"
                + "                       |___/                    |___/ \n";
        System.out.println(banner);

        System.out.println(greetingMessage());
        System.out.println(exitMessage());
    }

    static String greetingMessage() {
        return "I am Longfrog\nWhat do you need?";
    }

    static String exitMessage() {
        return "I'm going to sleep. Bye.";
    }
}