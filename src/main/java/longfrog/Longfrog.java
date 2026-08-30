package longfrog;

import longfrog.command.Command;
import longfrog.exception.LongfrogException;
import longfrog.parser.Parser;
import longfrog.storage.Storage;
import longfrog.task.TaskList;
import longfrog.ui.Ui;

/** Coordinates command parsing, task persistence, and the console UI. */
public class Longfrog {
    private static final String FILE_PATH = "data/longfrog.txt";
    private final Storage storage;
    private final TaskList taskList;
    private final Ui ui;

    /** Creates Longfrog using the supplied task storage file. */
    public Longfrog(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (LongfrogException e) {
            ui.showLoadingError();
            loadedTasks = new TaskList();
        }
        taskList = loadedTasks;
    }

    /** Starts the command loop and continues until the user enters {@code bye}. */
    public void run() {
        Parser parser = new Parser(taskList);

        ui.showGreeting();

        boolean isExit = false;
        while (!isExit) {
            String userInput = ui.readCommand();

            ui.showLine();
            try {
                Command c = parser.parse(userInput);
                isExit = c.execute(ui);
                if (!storage.save(taskList.getAll())) {
                    ui.showSavingError();
                }
            } catch (LongfrogException e) {
                ui.showMessage(e.getMessage());
            }

            ui.showEmptyLine();
            ui.showLine();
            ui.showEmptyLine();
        }

        ui.close();
    }

    /**
     * Starts Longfrog using its default save-file location.
     *
     * @param args command-line arguments, which are ignored
     */
    public static void main(String[] args) {
        new Longfrog(FILE_PATH).run();
    }
}
