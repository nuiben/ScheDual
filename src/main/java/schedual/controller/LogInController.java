package schedual.controller;

import java.io.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import schedual.Main;
import schedual.helper.JDBC;

/**
  * Secondary Controller - Challenges the user with a username and password prompt.
*/

public class LogInController extends MainViewController {
    @FXML
    private TextField userNameInput;
    @FXML
    private PasswordField passwordInput;
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


    /**
     * Initializes the Default Resources to the FXML Stage. FXML Handlers are provided with Event Actions through Lambda Expressions.
     * <p> Lambda Expression: FXML Handlers are functional interfaces known as Event Listeners. Rather than passing entire objects that implement the interface, the use of Lambdas here passes a block of code that only runs when the button is clicked.
     * <p>(logInButton.setOnAction) Passes username and password fields to JBDC, loads mainMenu.fxml if successful. Throws an SQLException if a connection could not be established.
     * @param url The filepath of this .fxml file.
     * @param resourceBundle Abstract Class which retrieves locale-specific objects.
     * */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.locationText.setText(ZoneId.systemDefault().getId());
        resourceBundle = ResourceBundle.getBundle("schedual/bundle/lang", Locale.getDefault());
        this.logInLabel.setText(resourceBundle.getString("LogInForm"));
        this.logInButton.setText(resourceBundle.getString("LogIn"));
        this.userNameLabel.setText(resourceBundle.getString("Username") + ":");
        this.passwordLabel.setText(resourceBundle.getString("Password") + ":");
        this.locationLabel.setText(resourceBundle.getString("Location") + ":");

        logInButton.setOnAction(event -> {
//            userNameInput.setText("sqlUser");
//            passwordInput.setText("Passw0rd!");
            if (JDBC.openConnection(userNameInput.getText(), passwordInput.getText())) {
                System.out.println(Main.class.getResource("mainMenu.fxml"));
                try {
                    updateLogInFile(true);
                } catch (IOException e) {
                    Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                    errorMessage.setTitle("Error Message");
                    errorMessage.setContentText(e.getMessage());
                    errorMessage.show();
                }
                loadFXML(event, "mainMenu.fxml");
                try {
                    checkUpcomingAppointments();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    updateLogInFile(false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    /**
     * Identifies Appointments from MySQL which begin within the next 15 minutes and Alerts User of result..
     * @throws SQLException If a connection to MySQL could not be established.
     * */

    public void checkUpcomingAppointments() throws SQLException {
        LocalDateTime upcomingStartTime = LocalDateTime.now().plusDays(1);
        String upcomingAppointmentTitle = "";
        int upcomingAppointmentID = 0;
        String sql = "SELECT * FROM appointments WHERE Start between ? and ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().plusMinutes(15)));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            upcomingStartTime = rs.getTimestamp("Start").toLocalDateTime();
            upcomingAppointmentTitle = rs.getString("Title");
            upcomingAppointmentID = rs.getInt("Appointment_ID");
        }
        if (upcomingStartTime.isBefore(LocalDateTime.now().plusMinutes(15))) {
            Alert message = new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("Appointment Begins Soon");
            message.setContentText("Appointment: " + upcomingAppointmentID + "\nTitle: " + upcomingAppointmentTitle +
                    "\nDate: " + upcomingStartTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
                    + "\nStart Time: " + upcomingStartTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
            if (message.showAndWait().get() == ButtonType.OK) {}
        } else {
            Alert message = new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("Upcoming Appointments");
            message.setContentText("You Have No Appointments Beginning Soon");
            if (message.showAndWait().get() == ButtonType.OK) {}
        }
    }

    /**
     * Prints a standardized output to login_activities.txt indicating when a User attempted to sign-in,
     * whether the attempt was successful, and what username and password combination was tried. (
     * @param success Boolean indicator if the user log-in attempt was successful.
     * @throws IOException If an issue arises updating the designated file.
     * */

    public void updateLogInFile(Boolean success) throws IOException {
        String filename = "login_activity.txt", item;
        FileWriter fileWriter = new FileWriter(filename, true);
        PrintWriter outputFile= new PrintWriter(fileWriter);
        System.out.print("Saving Log-In Attempt...");
        item = "Log-In Attempt (Pass/Fail): " + success + "\n@ " + Timestamp.from(Instant.now());
        outputFile.println(item + "\n");
        outputFile.close();
        System.out.println("File Written");

    }
}