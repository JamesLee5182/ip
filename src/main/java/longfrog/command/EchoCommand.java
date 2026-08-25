package longfrog.command;
import longfrog.Ui;

/** Displays a fixed message surrounded by separators. */
public class EchoCommand implements Command {
    private String echoMessage;

    public EchoCommand(String input)
    {
        this.echoMessage = input;
    }

    /** Displays the echo message. */
    @Override
    public boolean execute(Ui ui) {
        ui.showLine();
        ui.showMessage(echoMessage);
        ui.showLine();
        ui.showEmptyLine();
        return false;
    }
}
