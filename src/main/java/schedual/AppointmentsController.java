package schedual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import schedual.helper.JDBC;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppointmentsController extends MainViewController{
    @FXML
    private TableView<Appointment> appointmentsTable;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(url);
        loadTable();
    }

    public void loadTable() {
        //this.appointmentsTable.setItems();
    }
}