package schedual;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(url);
    }

    public void loadFXML(ActionEvent event, String resource) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(resource));
        fxmlLoader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.getRoot()));
        stage.show();
    }

    public void OnExitClick() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Close Application");
        confirmation.setContentText("Are you sure you want Exit?");
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            Main.quit();
        }
    }
    public void OnAppointmentsClick(ActionEvent event) throws IOException {
        loadFXML(event, "appointments.fxml");
    }
    public void OnCustomerClick() {}
    public void OnReportsClick() {}
}
