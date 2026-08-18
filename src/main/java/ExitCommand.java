public class ExitCommand implements Command {
    @Override
    public boolean execute() {
        Longfrog.printLine();
        Longfrog.printExitMessage();
        return true;
    }
}
