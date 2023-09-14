package schedual;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import schedual.helper.JDBC;

public class LogInController extends MainViewController {
    @FXML
    private TextField userNameInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private Text locationText;
    @FXML
    private Label logInLabel;
    @FXML
    private Button logInButton;
    @FXML
    private Label locationLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label passwordLabel;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(url);
        this.locationText.setText(ZoneId.systemDefault().getId());
        resourceBundle = ResourceBundle.getBundle("schedual/bundle/lang", Locale.getDefault());
        this.logInLabel.setText(resourceBundle.getString("LogInForm"));
        this.logInButton.setText(resourceBundle.getString("LogIn"));
        this.userNameLabel.setText(resourceBundle.getString("Username") + ":");
        this.passwordLabel.setText(resourceBundle.getString("Password") + ":");
        this.locationLabel.setText(resourceBundle.getString("Location") + ":");
    }

    @FXML
    protected void onLoginClick(ActionEvent logIn) throws IOException {
//        if (JDBC.openConnection(this.userNameInput.getText(), this.passwordInput.getText())) {
//            this.loadFXML(logIn, "mainMenu.fxml");
//        }
        if (JDBC.openConnection("sqlUser", "orangeMan1")) {
            this.loadFXML(logIn, "mainMenu.fxml");
        }

    }
}