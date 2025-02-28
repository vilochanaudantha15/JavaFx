import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for JavaFX.
 * 
 * @author vilochana udantha
 */
public class NewFXMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file for your login page
            Parent root = FXMLLoader.load(getClass().getResource("FXML.fxml"));

            // Create a scene with the loaded FXML root node
            Scene scene = new Scene(root);

            // Set the title of the primary stage (window)
            primaryStage.setTitle("Cafe Management System");
            primaryStage.setMinHeight(600);
            primaryStage.setMinHeight(400);
            // Set the scene to the stage
            primaryStage.setScene(scene);

            // Show the stage
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace(); // Print any errors to the console
        }
    }

    /**
     * The main method. Entry point of the application.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
