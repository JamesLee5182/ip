package longfrog;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Hosts the JavaFX scene for Longfrog.
 *
 * <p>The initial scene is intentionally minimal so it can be replaced by the SceneBuilder/FXML
 * implementation without changing the launcher entry point.
 */
public class Main extends Application {
    private static final double INITIAL_WIDTH = 400.0;
    private static final double INITIAL_HEIGHT = 600.0;

    /**
     * Creates and displays the initial JavaFX window.
     *
     * @param stage the primary JavaFX window
     */
    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, INITIAL_WIDTH, INITIAL_HEIGHT);

        stage.setTitle("Longfrog");
        stage.setScene(scene);
        stage.show();
    }
}
