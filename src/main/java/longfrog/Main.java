package longfrog;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import longfrog.ui.MainWindow;

/**
 * Starts the Longfrog GUI defined in FXML.
 */
public class Main extends Application {
    private static final String FILE_PATH = "data/longfrog.txt";
    private static final int AVATAR_SIZE = 64;
    private static final Color USER_AVATAR_COLOR = Color.DARKORANGE;
    private static final Color LONGFROG_AVATAR_COLOR = Color.DARKGREEN;

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
        mainWindow.setUserImage(createAvatar(USER_AVATAR_COLOR));
        mainWindow.setLongfrogImage(createAvatar(LONGFROG_AVATAR_COLOR));

        Scene scene = new Scene(root);
        stage.setTitle("Longfrog");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates a simple circular avatar image for a dialog speaker.
     *
     * @param color the avatar's fill color
     * @return the generated avatar image
     */
    private Image createAvatar(Color color) {
        WritableImage image = new WritableImage(AVATAR_SIZE, AVATAR_SIZE);
        PixelWriter pixelWriter = image.getPixelWriter();
        double center = AVATAR_SIZE / 2.0;
        double radius = AVATAR_SIZE / 2.0;

        for (int x = 0; x < AVATAR_SIZE; x++) {
            for (int y = 0; y < AVATAR_SIZE; y++) {
                double distance = Math.hypot(x - center + 0.5, y - center + 0.5);
                pixelWriter.setColor(x, y, distance <= radius ? color : Color.TRANSPARENT);
            }
        }

        return image;
    }
}
