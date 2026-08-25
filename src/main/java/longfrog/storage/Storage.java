package longfrog.storage;

import longfrog.task.Deadline;
import longfrog.task.Event;
import longfrog.task.Task;
import longfrog.task.Todo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the file. Creates directory and file if they do not exist.
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        try {
            // Create parent directories if missing (e.g. data/)
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            // If file doesn't exist, create an empty one and return empty list
            if (!file.exists()) {
                file.createNewFile();
                return tasks;
            }

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                Task task = parseLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
            scanner.close();

        } catch (IOException e) {
            System.out.println("Warning: Unable to load save file. Starting with an empty list.");
        }

        return tasks;
    }

    /**
     * Parses a single line from the file into a Task object.
     */
    private Task parseLine(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) return null;

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String name = parts[2];

        Task task = null;
        switch (type) {
            case "T":
                task = new Todo(name);
                break;
            case "D":
                if (parts.length >= 4) {
                    task = new Deadline(name, parts[3]);
                }
                break;
            case "E":
                if (parts.length >= 5) {
                    task = new Event(name, parts[3], parts[4]);
                }
                break;
        }

        if (task != null && isDone) {
            task.markAsDone();
        }

        return task;
    }

    /**
     * Overwrites the file with the current list of tasks.
     */
    public void save(List<Task> tasks) {
        try {
            File file = new File(filePath);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            FileWriter writer = new FileWriter(file);
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: Failed to save tasks to file.");
        }
    }
}