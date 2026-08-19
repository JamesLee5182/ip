package longfrog.command;

public interface Command {
    /**
     * Executes the command.
     * @return whether the chat should be exited after this command is executed.
     */
    boolean execute();
}
