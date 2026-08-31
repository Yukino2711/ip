package yqr.gui;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import yqr.Duke;

/**
 * Displays the yqr chatbot as a JavaFX application.
 */
public class Main extends Application {
    private static final double WINDOW_WIDTH = 720;
    private static final double WINDOW_HEIGHT = 640;

    private final Duke duke = new Duke();

    /**
     * Loads the main FXML view and shows it in the primary stage.
     *
     * @param stage primary JavaFX stage.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = loader.load();
            MainWindow controller = loader.getController();
            controller.setDuke(duke);

            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("/css/main.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);

            stage.setTitle("yqr");
            stage.setMinWidth(520);
            stage.setMinHeight(480);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load the yqr interface", e);
        }
    }
}
