import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class FxApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scenee = new Scene(new StackPane(l), 640, 480);

        Parent application = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        Scene scene = new Scene(application);

        stage.setScene(scene);
        stage.setTitle("Bank");
        stage.show();
    }

    public static  void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(e.getMessage());

        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch();
    }
}