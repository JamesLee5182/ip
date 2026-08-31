package longfrog;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import longfrog.ui.MainWindow;

/**
 * Starts the Longfrog GUI defined in FXML.
 */
public class Main extends Application {
    private static final String FILE_PATH = "data/longfrog.txt";
    private static final String USER_IMAGE_PATH = "/images/User.png";
    private static final String LONGFROG_IMAGE_PATH = "/images/Longfrog.png";

    /**
     * Loads the main FXML view and injects the Longfrog model into its controller.
     *
     * @param stage the primary JavaFX window
     * @throws IOException if the FXML view cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        AnchorPane root = fxmlLoader.load();

        MainWindow mainWindow = fxmlLoader.getController();
        mainWindow.setLongfrog(new Longfrog(FILE_PATH));
        Image userImage = new Image(Main.class.getResourceAsStream(USER_IMAGE_PATH));
        Image longfrogImage = new Image(Main.class.getResourceAsStream(LONGFROG_IMAGE_PATH));
        mainWindow.setUserImage(userImage);
        mainWindow.setLongfrogImage(longfrogImage);

        Scene scene = new Scene(root);
        stage.setTitle("Longfrog");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
