package longfrog.ui;

import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import longfrog.Longfrog;

/**
 * Controls the main Longfrog GUI defined in {@code MainWindow.fxml}.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Longfrog longfrog;
    private Image userImage;
    private Image longfrogImage;
    private Runnable exitAction;

    /**
     * Binds the scroll position to the dialog container's height.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Longfrog model used to process user commands.
     *
     * @param longfrog the application model
     */
    public void setLongfrog(Longfrog longfrog) {
        this.longfrog = longfrog;
    }

    /**
     * Sets the avatar displayed beside user messages.
     *
     * @param userImage the user's avatar
     */
    public void setUserImage(Image userImage) {
        this.userImage = userImage;
    }

    /**
     * Sets the avatar displayed beside Longfrog's responses.
     *
     * @param longfrogImage Longfrog's avatar
     */
    public void setLongfrogImage(Image longfrogImage) {
        this.longfrogImage = longfrogImage;
    }

    /**
     * Sets the action used to close the application window.
     *
     * @param exitAction the action to run after an exit command
     */
    public void setExitAction(Runnable exitAction) {
        this.exitAction = Objects.requireNonNull(exitAction);
    }

    /**
     * Adds the user's message and Longfrog's response to the dialog container.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = longfrog.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getLongfrogDialog(response, longfrogImage));
        userInput.clear();

        if (longfrog.isExitRequested()) {
            exitAction.run();
        }
    }
}
