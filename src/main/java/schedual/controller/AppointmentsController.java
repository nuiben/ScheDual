package schedual.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import schedual.helper.JDBC;
import schedual.model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Secondary Controller - CRUD Operations for Appointments Table.
 * <p>
 * (1) Presents Appointments Menu Option
 * <p>
 * (2) Allows user to perform CRUD (Create, Read, Update, Delete) operations on Appointments Table in MySQL.
 * (Create) Performed by the Add Button.
 * (Read) Performed by the Go Button and is determined by input parameters on the Top Row Spinners, Radio Buttons, and ComboBox.
 * The Search Bar filters these items based on user input to the TextField.
 * (Update) Performed by the Save Button for the selected item in the TableView. Edit button autofills selection's values to the field items below the TableView.
 * (Delete) Performed by the Delete Button for the selected item in the TableView.
*/

public class AppointmentsController extends MainViewController {
    private final String[] COLUMNS = {"Appointment_ID", "title", "description", "location", "type", "start", "end", "customerID", "userID", "contactID"};
    private final String[] MONTHS = {"January", "February", "March", "April", "May", "June","July","August","September","October","November","December"};
    private ObservableList<Appointment> appointmentsList = FXCollections.observableArrayList();
    private ObservableList<Integer> contactValue = FXCollections.observableArrayList();
    private ObservableList<String> contactList = FXCollections.observableArrayList();
    private ObservableList<Integer> userValue = FXCollections.observableArrayList();
    private ObservableList<String> userList = FXCollections.observableArrayList();
    private ObservableList<Integer> customerValue = FXCollections.observableArrayList();
    private ObservableList<String> customerList = FXCollections.observableArrayList();
    private ObservableList<String> monthList = FXCollections.observableArrayList();
    private ObservableList<String> yearList = FXCollections.observableArrayList();
    private ObservableList<String> weekList = FXCollections.observableArrayList();
    private ObservableList<LocalTime> localTimesList = FXCollections.observableArrayList();
    private ObservableList<String> localizedTimesList = FXCollections.observableArrayList();
    @FXML private Spinner<Integer> yearSpinner;
    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TextField titleField;
    @FXML private TextField descriptionField;
    @FXML private TextField locationField;
    @FXML private TextField typeField;
    @FXML private DatePicker startDate;
    @FXML private ComboBox<String> startTime;
    @FXML private DatePicker endDate;
    @FXML private ComboBox<String> endTime;
    @FXML private TextField appointmentSearch;
    @FXML private ComboBox<String> contactIdBox;
    @FXML private ComboBox<String> userIdBox;
    @FXML private ComboBox<String> customerIdBox;
    @FXML private RadioButton monthRadio;
    @FXML private RadioButton weekRadio;
    @FXML private RadioButton yearRadio;
    @FXML private RadioButton allRadio;
    @FXML private ToggleGroup toggleGroup;
    @FXML private ComboBox<String> viewBox;
    @FXML private Label displayDateText;
    @FXML private Text locationText;
    @FXML private Button addButton;
    @FXML private Button clearButton;
    @FXML private Button backButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private Button goButton;
    @FXML private Button saveButton;
    @FXML private Text displayCount;

    private LocalDate monthStart;
    private LocalDate monthEnd;

    /**
     * Initializes the Default Resources to the FXML Stage. FXML Handlers are provided with Event Actions through Lambda Expressions.
     * <p>
     * FXML Functional Interface Handlers as Lambda Expressions: FXML Handlers are functional interfaces known as Event Listeners. Rather than passing entire objects
     * that implement the interface, the use of Lambdas here passes a block of code that only runs when the button is clicked.
     * <p> (backButton.onAction) Loads the url for mainMenu.fxml when the Back Button is clicked by user.
     * <p> (yearSpinner.setOnMouseClicked) If Week-View is selected, updates the ComboBox to reflect the number of weeks
     *                    for the year value inside Spinner.
     * <p> (yearRadio.onAction) If the user selects the Year View, ComboBox is updated to display only one empty string.
     * <p> (monthRadio.onAction) If the user selects the Month View, ComboBox is updated to display a list of all calendar months.
     *                   The current month is selected by default.
     * <p> (weekRadio.onAction) If the user selects the Week View, ComboBox is updated to display a list 52 or 53 weeks.
     *                   Default number is determined by number of weeks in the current year.
     * <p> (goButton.onAction) Determines which view toggle is selected, loads appropriate values for the given year
     *                          as determined by the input value in the yearSpinner. If selection is empty, the user is alerted with an error message.
     *                          Catches ParseException when determining week date values or SQL Exception for loadtable function call.
     * <p> (appointmentSearch.setOnKeyPressed) While searchAppointment Textfield is selected, an event listener responds to Key Event 'ENTER', and selects
     *                  the first value in the appointmentTable TableView if one exists.
     * <p> (addButton.onAction) Prompts the user with an alert and confirms if the user wishes to add a new value to the appointments table
     *                  in MySQL based on the parameters in the fields below the TableView.
     * <p> (editButton.onAction) Updates the value fields below the TableView with the corresponding fields of the Appointment Object
     *                   currently selected in the TableView.
     * <p> (saveButton.onAction) Prompts the user with an alert and confirms if the user wishes to overwrite the selected value
     *                   in the TableView to the matching record in the MySQL database table.
     * <p> (clearButton.onAction) Updates the value fields below the TableView with null values.
     * <p> (deleteButton.onAction) Prompts the user with an alert and confirms if the user wishes to permanently remove the selected Appointment
     *                   in the TableView to the matching record in the MySQL database table.
     * @param url The filepath of this .fxml file.
     * @param resourceBundle Abstract Class which retrieves locale-specific objects.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load Default Resources
        locationText.setText(ZoneId.systemDefault().getId());
        monthStart = LocalDate.now().withDayOfMonth(1);
        monthEnd =  monthStart.plusMonths(1);

        LocalTime time = LocalTime.of(0, 0);
        for (int i = 0; i < 96; i++) {
            localTimesList.add(time);
            localizedTimesList.add(time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)) +
                    " (" + ZoneId.systemDefault().getDisplayName(TextStyle.SHORT, Locale.getDefault()) +
                    ZoneOffset.systemDefault().getRules().getOffset(Instant.now()) + ")");
            time = time.plusMinutes(15);
        }
        startTime.setItems(localizedTimesList);
        endTime.setItems(localizedTimesList);
        yearList.add("");
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2000, 2050, 2023));
        monthList.addAll(MONTHS);
        for (int i = 1; i <= Calendar.getInstance().getWeeksInWeekYear(); i++) {
            weekList.add("Week " + i);
        }
        viewBox.setItems(monthList);
        viewBox.getSelectionModel().select(LocalDate.now().getMonthValue()-1);
        displayDateText.setText(viewBox.getValue() + ", " + yearSpinner.getValue());

        try {
            loadIdBox("contact", contactList, contactValue);
            loadIdBox("user", userList, userValue);
            loadIdBox("customer", customerList, customerValue);
            contactIdBox.setItems(contactList);
            userIdBox.setItems(userList);
            customerIdBox.setItems(customerList);
            loadTable(monthStart,monthEnd);
        } catch (Exception e) {
            Alert errorMessage = new Alert(Alert.AlertType.ERROR);
            errorMessage.setTitle("Error Message");
            errorMessage.setContentText(e.getMessage());
            errorMessage.show();
        }

        // BACK BUTTON HANDLER
        backButton.setOnAction(back -> this.loadFXML(back, "mainMenu.fxml"));

        // YEAR SPINNER HANDLER
        yearSpinner.setOnMouseClicked(event -> {
                if (weekRadio.isSelected()) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(yearSpinner.getValue(), viewBox.getSelectionModel().getSelectedIndex() + 1, 1);
                    weekList = FXCollections.observableArrayList();
                    for (int i = 1; i <= cal.getWeeksInWeekYear(); i++) {
                        weekList.add("Week - " + i);
                    }
                    viewBox.setItems(weekList);
                }
        });

        // TOGGLE GROUP - YEAR / MONTH/ WEEK

        // YEAR RADIO BUTTON HANDLER
        allRadio.setOnAction(all -> {
            viewBox.setItems(yearList);
            viewBox.getSelectionModel().select(0);
        });
        // YEAR RADIO BUTTON HANDLER
        yearRadio.setOnAction(year -> {
            viewBox.setItems(yearList);
            viewBox.getSelectionModel().select(0);
        });

        // MONTH RADIO BUTTON HANDLER
        monthRadio.setOnAction(month -> {
            viewBox.setItems(monthList);
            viewBox.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        });

        // WEEK RADIO BUTTON HANDLER
        weekRadio.setOnAction(week -> {
            viewBox.setItems(weekList);
            viewBox.getSelectionModel().select((LocalDate.now().getDayOfYear() / 7));
        });

        // GO BUTTON HANDLER
        goButton.setOnAction(go -> {
            try {
                if (viewBox.getSelectionModel().isEmpty()) {
                    Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                    errorMessage.setTitle("Error Message");
                    errorMessage.setContentText("Please Make a Selection in the Dropdown Menu");
                    errorMessage.show();
                } else if (monthRadio.isSelected()) {
                    LocalDate firstDay = LocalDate.of(yearSpinner.getValue(),viewBox.getSelectionModel().getSelectedIndex()+1,1);
                    monthStart = firstDay;
                    monthEnd = firstDay.plusMonths(1);
                    loadTable(monthStart, monthEnd);
                    displayDateText.setText(MONTHS[viewBox.getSelectionModel().getSelectedIndex()] + ", " + yearSpinner.getValue());
                } else if (weekRadio.isSelected()) {
                    Calendar c = Calendar.getInstance();
                    int weekYear = viewBox.getSelectionModel().getSelectedIndex()+1;
                    c.set(yearSpinner.getValue(), 1, 1);
                    Date weekStart = java.sql.Date.valueOf(LocalDate.of(yearSpinner.getValue(),1,1));
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
                    java.util.Date firstDayUtil = sdf.parse(weekStart.toString());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(firstDayUtil);
                    java.sql.Date firstDayOfFirstWeek = java.sql.Date.valueOf(LocalDate.of(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH)+1,
                            calendar.get(Calendar.DAY_OF_MONTH)));
                    int modifier = viewBox.getSelectionModel().getSelectedIndex() * 7; // how many days to add to get the start of selected week
                    LocalDate firstDayOfSelectedWeek = firstDayOfFirstWeek.toLocalDate().plusDays(modifier);
                    LocalDate lastDayOfSelectedWeek = firstDayOfSelectedWeek.plusDays(6);
                    loadTable(firstDayOfSelectedWeek, lastDayOfSelectedWeek);
                    displayDateText.setText("Week " + weekYear + " - ["+ firstDayOfSelectedWeek.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + " - "
                            + lastDayOfSelectedWeek.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + "]");
                } else if (yearRadio.isSelected()) {
                    displayDateText.setText(yearSpinner.getValue().toString());
                    loadTable(LocalDate.of(yearSpinner.getValue(), 1, 1), LocalDate.of(yearSpinner.getValue(), 12, 31));
                } else {
                    displayDateText.setText("All Records");
                    loadTable(LocalDate.of(1999, 1, 1), LocalDate.of(2051, 1, 1));
                }
            } catch (ParseException | SQLException e) {
                Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                errorMessage.setTitle("Error Message");
                errorMessage.setContentText(e.getMessage());
                errorMessage.show();
            }
        });

        // SEARCH BAR HANDLER
        appointmentSearch.setOnKeyPressed(enter -> {
            try {
                if (enter.getCode() == KeyCode.ENTER) {
                    String query = appointmentSearch.getText();
                    if (query.isEmpty()) {
                        throw new NumberFormatException("Search Field Empty: Please Type an ID or Name");
                    }
                    try {
                        if (appointmentsTable.getItems().isEmpty()) {
                            throw new NullPointerException("Appointment Not Found");
                        } else {
                            appointmentsTable.getSelectionModel().select(0);
                        }
                    } catch (NumberFormatException exception) {
                        if (appointmentsTable.getItems().isEmpty()) {
                            throw new NullPointerException("Appointment Not Found");
                        } else {
                            appointmentsTable.getSelectionModel().select(0);
                        }
                    }
                }
                } catch (Exception e) {
                    Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                    errorMessage.setTitle("Error Message");
                    errorMessage.setContentText(e.getMessage());
                    errorMessage.show();
                }
            });

        // ADD BUTTON HANDLER
        addButton.setOnAction(add -> {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Create New Appointment");
            confirmation.setContentText("Would you like to add this entry as a new Appointment?");
            if (confirmation.showAndWait().get() == ButtonType.OK) {
                try {
                    inputValidation(-1); // 0 tells the function we are not overwriting an existing Appt
                    Appointment appointment = addAppointment();
                    String sql = "INSERT INTO Appointments (title, description, location, type, start, end, " +
                            "create_date, created_by, last_update,last_updated_by, customer_ID, user_ID, contact_ID)\n" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                    PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
                    ps.setString(1, appointment.getTitle());
                    ps.setString(2, appointment.getDescription());
                    ps.setString(2, appointment.getDescription());
                    ps.setString(3, appointment.getLocation());
                    ps.setString(4, appointment.getType());
                    ps.setTimestamp(5, Timestamp.valueOf(
                            LocalDateTime.parse(appointment.getStart(),
                                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)))); // Start
                    ps.setTimestamp(6, Timestamp.valueOf(
                            LocalDateTime.parse(appointment.getEnd(),
                                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)))); // End
                    ps.setTimestamp(7, java.sql.Timestamp.from(Instant.now())); // Create_Date
                    ps.setString(8, JDBC.getUserName());
                    ps.setTimestamp(9, java.sql.Timestamp.from(Instant.now())); // Last_Update
                    ps.setString(10, JDBC.getUserName());
                    ps.setInt(11, appointment.getCustomerID());
                    ps.setInt(12, appointment.getUserID());
                    ps.setInt(13, appointment.getContactID());
                    ps.executeUpdate();
                    goButton.fire();
                } catch (Exception e) {
                    Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                    errorMessage.setTitle("Error Message");
                    errorMessage.setContentText(e.getMessage());
                    errorMessage.show();
                }
            }
        });

        // EDIT BUTTON HANDLER
        editButton.setOnAction(edit -> {
            try {
                Appointment selectAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
                titleField.setText(selectAppointment.getTitle());
                descriptionField.setText(selectAppointment.getDescription());
                locationField.setText(selectAppointment.getLocation());
                typeField.setText(selectAppointment.getType());
                LocalDateTime localStart = LocalDateTime.parse(selectAppointment.getStart(), DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
                startDate.setValue(localStart.toLocalDate());
                startTime.getSelectionModel().select(((localStart.getHour()*4)+(localStart.getMinute()/15)));
                LocalDateTime localEnd = LocalDateTime.parse(selectAppointment.getEnd(), DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
                endDate.setValue(localEnd.toLocalDate());
                endTime.getSelectionModel().select(((localEnd.getHour()*4)+(localEnd.getMinute()/15)));
                contactIdBox.getSelectionModel().select(contactValue.indexOf(selectAppointment.getContactID()));
                customerIdBox.getSelectionModel().select(customerValue.indexOf(selectAppointment.getCustomerID()));
                userIdBox.getSelectionModel().select(userValue.indexOf(selectAppointment.getUserID()));
            } catch (NullPointerException e) {
                Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                errorMessage.setTitle("Error Message");
                errorMessage.setContentText("No Appointment Selected");
                errorMessage.show();
            }
        });

        // DELETE BUTTON HANDLER
        deleteButton.setOnAction(delete -> {
            try {
                Appointment selectAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
                int ID = selectAppointment.getAppointment_ID();
                String type = selectAppointment.getType();
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Delete Appointment Record");
                if (LocalDateTime.parse(selectAppointment.getStart(), DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)).isAfter(LocalDateTime.now())){
                    confirmation.setContentText("Are you sure you want to cancel this upcoming appointment?\nAppointment ID: " + ID + "\nType: " + type);
                } else {
                    confirmation.setContentText("Are you sure you want to delete this past appointment?\nAppointment ID: " + ID + "\nType: " + type);
                }
                if (confirmation.showAndWait().get() == ButtonType.OK) {
                    String sql = "DELETE FROM Appointments WHERE Appointment_ID = ?";
                    PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
                    ps.setInt(1, selectAppointment.getAppointment_ID());
                    ps.execute();
                    Alert information = new Alert(Alert.AlertType.INFORMATION);
                    information.setTitle("Record Purged");
                    information.setContentText("Appointment ID: " + ID + "\nType: " + type + "\nWas purged from the database.");
                    information.show();
                    goButton.fire();
                }
            } catch (Exception e) {
                Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                errorMessage.setTitle("Error Message");
                errorMessage.setContentText("No Appointment Selected");
                errorMessage.show();
            }
        });

        // CLEAR BUTTON HANDLER
        clearButton.setOnAction(clear -> {
            appointmentsTable.getSelectionModel().clearSelection();
            titleField.clear();
            descriptionField.clear();
            locationField.clear();
            typeField.clear();
            startDate.setValue(null);
            endDate.setValue(null);
            startTime.getSelectionModel().select(null);
            endTime.getSelectionModel().select(null);
            contactIdBox.getSelectionModel().clearSelection();
            customerIdBox.getSelectionModel().clearSelection();
            userIdBox.getSelectionModel().clearSelection();
        });

        // SAVE BUTTON HANDLER
        saveButton.setOnAction(save -> {
                try {
                    int selectedAppointmentIndex = appointmentsTable.getSelectionModel().getSelectedItem().getAppointment_ID();
                    String selectedAppointmentType = appointmentsTable.getSelectionModel().getSelectedItem().getType();
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmation.setTitle("Update Appointment");
                    confirmation.setContentText("Would you like to overwrite your changes to this Appointment?\nAppointment ID: "
                            + selectedAppointmentIndex + "\nType: " + selectedAppointmentType);
                    if (confirmation.showAndWait().get() == ButtonType.OK) {
                        inputValidation(selectedAppointmentIndex);
                        goButton.fire();
                        ObservableList<Appointment> appointments = appointmentsTable.getItems();
                        for (Appointment i : appointments) {
                            if (selectedAppointmentIndex == i.getAppointment_ID()) {
                                appointmentsTable.getSelectionModel().select(i);
                            }
                        }
                        Appointment selectAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
                        String sql = "UPDATE Appointments " +
                                "SET title = ?, description = ?, location = ?, type = ?, start = ?, end = ?, last_update = CURRENT_TIMESTAMP," +
                                " last_updated_by = ?, customer_id = ?, user_id = ?, contact_id = ? " +
                                "WHERE Appointment_ID = ?";
                        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
                        ps.setString(1, titleField.getText());
                        ps.setString(2, descriptionField.getText());
                        ps.setString(3, locationField.getText());
                        ps.setString(4, typeField.getText());
                        LocalDateTime localStartDate = LocalDateTime.of(startDate.getValue(), localTimesList.get(startTime.getSelectionModel().getSelectedIndex()));
                        ps.setTimestamp(5, Timestamp.valueOf(localStartDate));
                        LocalDateTime localEndDate = LocalDateTime.of(endDate.getValue(), localTimesList.get(endTime.getSelectionModel().getSelectedIndex()));
                        ps.setTimestamp(6, Timestamp.valueOf(localEndDate));
                        ps.setString(7, JDBC.getUserName());
                        ps.setInt(8, customerValue.get(customerIdBox.getSelectionModel().getSelectedIndex()));
                        ps.setInt(9, userValue.get(userIdBox.getSelectionModel().getSelectedIndex()));
                        ps.setInt(10, contactValue.get(contactIdBox.getSelectionModel().getSelectedIndex()));
                        ps.setInt(11, selectAppointment.getAppointment_ID());
                        ps.executeUpdate();
                        goButton.fire();
                    }
                } catch (SQLException e) {
                    Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                    errorMessage.setTitle("Error Message");
                    errorMessage.setContentText(e.getMessage());
                    errorMessage.show();
                } catch (NullPointerException e) {
                    Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                    errorMessage.setTitle("Error Message");
                    errorMessage.setContentText("No Selection Indicated.");
                    errorMessage.show();
                } catch (RuntimeException e) {
                    Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                    errorMessage.setTitle("Input Validation Error");
                    errorMessage.setContentText(e.getMessage());
                    errorMessage.show();
                }
        });

    }

    /**
     * Takes the String of the table items to construct Parameter to Construct SQL Query.
     * @param item for name of the SQL table and the Field names to be retrieved.
     * @param itemList name of the empty String Observable List to be filled.
     * @param itemValue name of the empty Integer Observable List to be filled.
     * @throws SQLException when a connection to the MySQL database could not be established.
     */
    public void loadIdBox(String item, ObservableList itemList, ObservableList itemValue) throws SQLException {
        String sql = "SELECT * FROM " + item + "s";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            itemList.add(rs.getInt(item + "_ID") + " - " + rs.getString(item + "_Name"));
            itemValue.add(rs.getInt(item + "_ID"));
        }
    }

    /**
     * Loads the Appointment TableView with items from appointments table between dates Start and End
     * <p>
     * Expression (Nested): This Lambda Expression makes use of functional interfaces to provide a filter for the user to quickly
     * identify appointments. Use of the Lambda Function here allows for the stream of user input to alter the items viewed in the TableView without
     * passing around excess data stored in Objects.
     * <p> Outter: (appointmentSearch.textProperty().addListener) Adds a functional interface listener to the TextField.
     * <p> Inner: (app.setpredicate) Establishes the predicate of the event listener to be the Appointment which matches the values in TextField.
     * @param Start - LocalDate Object
     * @param End - LocalDate Object
     * @throws SQLException when a connection to the MySQL database could not be established.
     * */

    public void loadTable(LocalDate Start, LocalDate End) throws SQLException {
        appointmentsList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Start between ? and ? ORDER BY Start";
        //If Start or End date are outside of the Year Range 2000-2050, return ALL
        if (Start.isBefore(LocalDate.of(2000,1,1))
                || Start.isAfter(LocalDate.of(2050,12,31))
                || End.isBefore(LocalDate.of(2000,1,1))
                || End.isAfter(LocalDate.of(2050,12,31))) {
            sql = "SELECT * FROM appointments";
        }
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        if (sql.equals("SELECT * FROM appointments WHERE Start between ? and ? ORDER BY Start")) {
            ps.setDate(1, java.sql.Date.valueOf(Start));
            ps.setDate(2, java.sql.Date.valueOf(End));
        }
        ResultSet rs = ps.executeQuery();
        int recordCount = 0;
        while (rs.next()) {
            Appointment appointment = new Appointment(
                    rs.getInt("Appointment_ID"),
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getString("Location"),
                    rs.getString("Type"),
                    rs.getTimestamp("Start").toLocalDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                    rs.getTimestamp("End").toLocalDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                    rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"),
                    rs.getInt("Contact_ID"));
            appointmentsList.add(appointment);
            recordCount++;
        }

        int  index = 0;
        for (String col : COLUMNS) {
            appointmentsTable.getColumns().get(index++).setCellValueFactory(new PropertyValueFactory<>(col));
        }
        displayCount.setText(String.valueOf(recordCount));
        this.appointmentsTable.setItems(appointmentsList);

        FilteredList<Appointment> app = new FilteredList<>(appointmentsTable.getItems(), a -> true);
        appointmentSearch.textProperty().addListener((input, previous, next) -> app.setPredicate(appointment -> {
            if (next == null || next.isEmpty()) {
                return true;
            }
            String lower = next.toLowerCase();
            if (appointment.getTitle().toLowerCase().contains(lower)) {
                return true;
            } else {
                return Integer.toString(appointment.getAppointment_ID()).contains(lower);
            }
        }));
        SortedList<Appointment> appointmentsSorted = new SortedList<>(app);
        appointmentsSorted.comparatorProperty().bind(appointmentsTable.comparatorProperty());
        appointmentsTable.setItems(appointmentsSorted);
    }

    /** Performs Standard Input Validations for values in Field
     * @param sourceID int ID of the Appointment being overwritten, new records provide -1.
     * @throws NullPointerException if no selection is indicated.
     * */

    public void inputValidation(int sourceID) throws NullPointerException {
        ZonedDateTime businessHoursStart = ZonedDateTime.of( LocalDateTime.of(startDate.getValue(),localTimesList.get(32)),
                ZoneId.of( "America/New_York" )).withZoneSameInstant(ZoneId.systemDefault()); // 8AM EST - Converted to Local TZ
        ZonedDateTime businessHoursEnd = ZonedDateTime.of( LocalDateTime.of(startDate.getValue(),localTimesList.get(88)),
                ZoneId.of( "America/New_York" )).withZoneSameInstant(ZoneId.systemDefault()); // 10PM EST - Converted to Local TZ
        ZonedDateTime inputStartTime = ZonedDateTime.of(startDate.getValue(),
                localTimesList.get(startTime.getSelectionModel().getSelectedIndex()), ZoneId.systemDefault());
        ZonedDateTime inputEndTime = ZonedDateTime.of(endDate.getValue(),
                localTimesList.get(endTime.getSelectionModel().getSelectedIndex()), ZoneId.systemDefault());
        if (
                titleField.getText().isEmpty()
                || descriptionField.getText().isEmpty()
                || locationField.getText().isEmpty()
                || typeField.getText().isEmpty()
                || startDate.getValue() == null
                || endDate.getValue() == null
                || startTime.getValue() == null
                || endTime.getValue() == null
                || customerIdBox.getValue().isEmpty()
                || userIdBox.getValue().isEmpty()
                ||  contactIdBox.getValue().isEmpty())
        {

            throw new RuntimeException("Fields May Not Be Empty");

        } else if (inputStartTime.isAfter(inputEndTime)){
            throw new RuntimeException("Start Time Must Be Before End Time");
        } else if (inputStartTime.isBefore(businessHoursStart)) {
            throw new RuntimeException("Start Time May Not Be Before: \n" + businessHoursStart.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));
        } else if (inputStartTime.isAfter(businessHoursEnd)) {
            throw new RuntimeException("Start Time May Not Be After: \n" + businessHoursEnd.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));
        } else if (inputEndTime.isBefore(businessHoursStart)) {
            throw new RuntimeException("End Time May Not Be Before: \n" + businessHoursStart.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));
        } else if (inputEndTime.isAfter(businessHoursEnd)) {
            throw new RuntimeException("End Time May Not Be After: \n" + businessHoursStart.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));
        } else if (inputStartTime.isEqual(inputEndTime)) {
            throw new RuntimeException("Meetings must be at least 15 minutes long");
        }
        try {
            loadTable(inputStartTime.toLocalDate().minusDays(1),
                    inputEndTime.toLocalDate().plusDays(1));

            for (Appointment appt : appointmentsTable.getItems()) {

                LocalDateTime start = LocalDateTime.parse(
                        appt.getStart(),
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
                LocalDateTime end = LocalDateTime.parse(
                        appt.getEnd(),
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));

                if (appt.getAppointment_ID() != sourceID) { // Validate All entries unless we were provided the Appt_ID of a value to overwrite

                    if (start.isAfter(inputStartTime.toLocalDateTime())
                            && start.isBefore(inputEndTime.toLocalDateTime())) { //Look Ahead

                        throw new RuntimeException(
                                "Overlap: Appointment Runs into an Existing Appointment's Start Time: \nID: " + appt.getAppointment_ID() + " \nTitle: " + appt.getTitle());

                    } else if (start.isBefore(inputStartTime.toLocalDateTime())
                            && end.isAfter(inputStartTime.toLocalDateTime())) { //Look Behind

                        throw new RuntimeException(
                                "Overlap: Another Appointment is in Progress at Intended Start Time: \nID: " + appt.getAppointment_ID() + " \nTitle: " + appt.getTitle());

                    } else if (start.isEqual(inputStartTime.toLocalDateTime())) { // Look At

                        throw new RuntimeException(
                                "Overlap: Another Appointment Starts at this Time: \nID: " + appt.getAppointment_ID() + " \nTitle: " + appt.getTitle());

                    }
                }
            }
        } catch (SQLException e) {
            Alert errorMessage = new Alert(Alert.AlertType.ERROR);
            errorMessage.setTitle("SQL Exception: ");
            errorMessage.setContentText(e.getMessage());
            errorMessage.show();
        }
    }

    /** Creates a new Appointment based on the field values of the
     *  current scene.
     * @return Appointment object containing the user's inputs.
     * */
    public Appointment addAppointment() {
        return new Appointment(
                0,
                titleField.getText(),
                descriptionField.getText(),
                locationField.getText(),
                typeField.getText(),
                LocalDateTime.of(startDate.getValue(),localTimesList.get(
                        startTime.getSelectionModel().getSelectedIndex())).format(
                                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                LocalDateTime.of(endDate.getValue(),localTimesList.get(
                        endTime.getSelectionModel().getSelectedIndex())).format(
                                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                customerValue.get(customerIdBox.getSelectionModel().getSelectedIndex()),
                userValue.get(userIdBox.getSelectionModel().getSelectedIndex()),
                contactValue.get(contactIdBox.getSelectionModel().getSelectedIndex()));
    }
}