package schedual.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Pair;
import schedual.helper.JDBC;
import schedual.model.Appointment;
import schedual.model.Engagement;
import schedual.model.MonthTypeCount;

import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * Secondary Controller - Specialized Report Dashboard for Users.
 * <p>
 * (1) Presents Reports Menu Option.
 * <p>
 * (2) Allows user to view a Dashboard of the following reports: Appointment Totals, Customer Engagement, and Contact Schedule.
 */

public class ReportsController extends MainViewController{
    @FXML private ComboBox<String> contactIdBox;
    @FXML private TableView<MonthTypeCount> monthTable;
    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableView<Engagement> engagementTable;
    @FXML private Button goButton;
    @FXML private Button backButton;
    @FXML private Text followUpCount;

    private final String[] COLUMNS = {"Appointment_ID", "title", "description",
            "location", "type", "start", "end", "customerID", "userID", "contactID"};
    private final String [] ENGAGEMENT_COLUMNS = {"Customer_ID", "Customer_Name", "Last_Appointment", "Next_Appointment"};

    ObservableList<MonthTypeCount> monthTypeCountList = FXCollections.observableArrayList();
    ObservableList<Pair<Month, String>> monthTypeList = FXCollections.observableArrayList();
    ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
    ObservableList<Engagement> customerEngagementList = FXCollections.observableArrayList();

    private final ObservableList<Integer> contactValue = FXCollections.observableArrayList();
    private final ObservableList<String> contactList = FXCollections.observableArrayList();
    private int customersWithNoFollowUp;

    /**
     * Initializes the Default Resources to the FXML Stage. FXML Handlers are provided with Event Actions through Lambda Expressions.
     * <p> FXML Functional Interface Handlers as Lambda Expressions: FXML Handlers are functional interfaces known as Event Listeners. Rather than passing entire objects
     * that implement the interface, the use of Lambdas here passes a block of code that only runs when the button is clicked.
     * <p>
     * (backButton.onAction) Loads the url for mainMenu.fxml when the Back Button is clicked by user.
     * <p>
     * (goButton.onAction) Determines which view Contact ID is selected, loads all appointments for the selected contact.
     * @param url The filepath of this .fxml file.
     * @param resourceBundle Abstract Class which retrieves locale-specific objects.
     * */

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // (1) GO BUTTON HANDLER
        goButton.setOnAction(go -> {
            try {
                loadAppointments(contactIdBox.getSelectionModel().getSelectedIndex()+1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        // (2) BACK BUTTON HANDLER
        backButton.setOnAction(back -> {
            this.loadFXML(back, "mainMenu.fxml");
        });

        // LOADER GROUP
        try {
            loadContacts();
            contactIdBox.getSelectionModel().select(0);
            loadMonthTypeCounts();
            loadAppointments(1);
            loadEngagementTable();
        } catch (SQLException e) {
            Alert errorMessage = new Alert(Alert.AlertType.ERROR);
            errorMessage.setTitle("SQL Exception: Loader Group");
            errorMessage.setContentText(e.getMessage());
            errorMessage.show();
        }
    }

    /**
     * Queries MySQL database for all contact IDs in contacts table. Loads the values into the ComboBox above the Contact Schedule Tableview.
     * @throws SQLException when a connection to MySQL database could not be established.
     */
    public void loadContacts() throws SQLException {
        PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT * FROM contacts");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            contactList.add(rs.getInt("Contact_ID") + " - " + rs.getString("Contact_Name"));
            contactValue.add(rs.getInt("Contact_ID"));
        }
        contactIdBox.setItems(contactList);
    }

    /**
     * Queries MySQL database for all appointments belonging to the selected contact ID in the ComboBox above the Contact Schedule Tableview.
     * @param ContactID the int value of the Contact to be loaded.
     * @throws SQLException when a connection to MySQL database could not be established.
     */
    public void loadAppointments(int ContactID) throws SQLException {
        appointmentsList = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.getConnection().prepareStatement(
                "SELECT * FROM appointments WHERE Contact_ID = ? ORDER BY Start");
        ps.setInt(1, ContactID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment appointment = new Appointment(
                    rs.getInt("Appointment_ID"),
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getString("Location"),
                    rs.getString("Type"),
                    rs.getTimestamp("Start").toLocalDateTime().format(
                            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                    rs.getTimestamp("End").toLocalDateTime().format(
                            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                    rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"),
                    rs.getInt("Contact_ID"));
            appointmentsList.add(appointment);
        }
        int  index = 0;
        for (String col : COLUMNS) {
            appointmentsTable.getColumns().get(index++).setCellValueFactory(new PropertyValueFactory<>(col));
        }
        this.appointmentsTable.setItems(appointmentsList);
    }
    /**
     * Loads the TableView for Customer Engagement.
     * @throws SQLException when a connection to MySQL database could not be established.
     */
    public void loadEngagementTable() throws SQLException {
        customerEngagementList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String[] appts = retrieveCustomerAppointments(rs.getInt("Customer_ID"));
            Engagement engagement =new Engagement(
                    rs.getInt("Customer_ID"),
                    rs.getString("Customer_Name"),
                    appts[0],
                    appts[1]);
            customerEngagementList.add(engagement);
        }
        int  index = 0;
        for (String var : ENGAGEMENT_COLUMNS) {
            engagementTable.getColumns().get(index++).setCellValueFactory(new PropertyValueFactory<>(var));
        }
        this.engagementTable.setItems(customerEngagementList);
        followUpCount.setText(String.valueOf(customersWithNoFollowUp));
    }


    /**
     * Queries MySQL database for all distinct types for use by the Appointment Totals Report.
     * @throws SQLException when a connection to MySQL database could not be established.
     */
    public void loadMonthTypeCounts() throws SQLException {
        PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT * FROM appointments ORDER BY month(Start), Type");
        ResultSet rs = ps.executeQuery();
        int index = 0;
        int consecutive = 1;
        while(rs.next()) {
            Month month = rs.getTimestamp("Start").toLocalDateTime().getMonth();
            String type = rs.getString("Type");
            Pair<Month, String> monthTypePair = new Pair<>(month, type);
            MonthTypeCount record = new MonthTypeCount(month, type, 1);
            if (!monthTypeList.contains(monthTypePair)) { //NEW ITEM
                monthTypeList.add(monthTypePair);
                monthTypeCountList.add(record);
                monthTypeCountList.get(index).setCount(consecutive);
                consecutive = 1;
                index++;
            } else { //REPEAT MONTHTYPE
                consecutive++;
            }
        }
        monthTable.setItems(monthTypeCountList);
        monthTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("Month"));
        monthTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("Type"));
        monthTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("Count"));

    }

    /**
     * Retrieves the Customer's next appointment and last appointment, provides null if a value is not found.
     * @param CustomerID the integer value of the Customer's ID.
     * @throws SQLException when a connection to MySQL database could not be established.
     * @throws NullPointerException when a selection is not indicated.
     * @return String[] Containing the Formatted DateTime of the Last and Next Appointment for the Customer.
     */

    public String[] retrieveCustomerAppointments(int CustomerID) throws SQLException, NullPointerException {
        PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT * FROM appointments WHERE Customer_ID = ?");
        ps.setInt(1, CustomerID);
        ResultSet rs = ps.executeQuery();
        LocalDateTime lastAppointment = LocalDateTime.MIN;
        LocalDateTime nextAppointment = LocalDateTime.MAX;
        while (rs.next()) {
            if (rs.getTimestamp("Start").before(Timestamp.from(Instant.now()))) {
                if (rs.getTimestamp("Start").toLocalDateTime().isAfter((lastAppointment))) {
                    lastAppointment = rs.getTimestamp("Start").toLocalDateTime();
                }
            }
            if (rs.getTimestamp("Start").after(Timestamp.from(Instant.now()))) {
                if (rs.getTimestamp("Start").toLocalDateTime().isBefore(nextAppointment)) {
                    nextAppointment = rs.getTimestamp("Start").toLocalDateTime();
                }
            }
        }
        String[] times = new String[2];

        if (!nextAppointment.equals(LocalDateTime.MAX)) {
            times[1] = nextAppointment.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));

        } else {
            customersWithNoFollowUp++;
        }
        if (!lastAppointment.equals(LocalDateTime.MIN)) {
            times[0] = lastAppointment.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
        }
        return times;
    }
}
