package longfrog.command;
import longfrog.Longfrog;

public class ExitCommand implements Command {
    @Override
    public boolean execute() {
        Longfrog.printExitMessage();
        return true;
    }
}
