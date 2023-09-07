package schedual;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;

import schedual.helper.JDBC;

/**
 * Main Class for the ScheDual Program.
 * @author Ben Porter
 * <p> Relevant Filepaths for this Project:
 * <p> ScheDual\src\main\java\ben\schedual\main
 * <p>  ScheDual\src\main\java\ben\schedual\model
 * <p>  ScheDual\src\main\resources\view
 * <p>  ScheDual\src\main\java\ben\schedual\controller
 * <p>  ScheDual\javadoc
*/

public class Main extends Application {

    /** Launches the application.
     * @param args default input parameter.
     * */

    public static void main(String[] args) {
//        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
//        TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
//        Locale.setDefault(new Locale("fr"));
        launch();
    }

    /** Loads in Log-In FXML File
     * @param stage determines the view being displayed
     * @throws IOException when the file could not be loaded.
     * */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("logIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ResourceBundle resourceBundle = ResourceBundle.getBundle("schedual/bundle/lang", Locale.getDefault());
        stage.setTitle(resourceBundle.getString("Title"));
        stage.setScene(scene);
        stage.show();
    }

    /** Only Used by "Exit" button on Main View */
    public static void quit() {
        Platform.exit();
        JDBC.closeConnection();
    }
}