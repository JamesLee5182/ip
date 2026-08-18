public class EchoCommand implements Command {
    private String echoMessage;

    public EchoCommand(String input)
    {
        this.echoMessage = input;
    }

    @Override
    public boolean execute() {
        Longfrog.printLine();
        Longfrog.printMessage(echoMessage);
        Longfrog.printLine();
        Longfrog.printEmptyLine();
        return false;
    }
}
