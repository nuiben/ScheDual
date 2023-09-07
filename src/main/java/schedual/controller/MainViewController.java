package schedual.controller;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import schedual.Main;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Primary Controller - Directs child Controllers.
 */
public class MainViewController implements Initializable {

    @FXML
    private Button exitButton;
    @FXML
    private Button appointmentsButton;
    @FXML
    private Button customerButton;
    @FXML
    private Button reportsButton;

    /**
     * Initializes the Default Resources to the FXML Stage. FXML Handlers are provided with Event Actions through Lambda Expressions.
     * <p> FXML Functional Interface Handlers as Lambda Expressions: FXML Handlers are functional interfaces known as Event Listeners. Rather than passing entire objects
     * that implement the interface, the use of Lambdas here passes a block of code that only runs when the button is clicked.
     * <p> (appointmentsButton.setOnAction) Loads appointments.fxml
     * <p> (customerButton.setOnAction) Loads customers.fxml
     * <p> (reportsButton.setOnAction) Loads reports.fxml
     * <p> (exitButton.onAction) Prompts user with confirmation, then Flees the program.
     * @param url The filepath of this .fxml file.
     * @param resourceBundle Abstract Class which retrieves locale-specific objects.
     * */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentsButton.setOnAction(event -> loadFXML(event, "appointments.fxml"));
        customerButton.setOnAction(event -> loadFXML(event, "customers.fxml"));
        reportsButton.setOnAction(event -> loadFXML(event, "reports.fxml"));
        exitButton.setOnAction(event -> {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Close Application");
            confirmation.setContentText("Are you sure you want Exit?");
            if (confirmation.showAndWait().get() == ButtonType.OK) {
                Main.quit();
            }
        });
    }

    /**
     * Loads FXML resources, Alerts user of IOException if file could not be loaded.
     * @param event  The ActionEvent which triggered this method.
     * @param resource The filepath of this .fxml file.
     */
    public void loadFXML(ActionEvent event, String resource) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(resource));
        fxmlLoader.setLocation(Main.class.getResource(resource));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            Alert errorMessage = new Alert(Alert.AlertType.ERROR);
            errorMessage.setTitle("Error Message");
            errorMessage.setContentText(e.getMessage());
            errorMessage.show();
        }
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.getRoot()));
        stage.show();
    }
}
