package schedual.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import schedual.helper.JDBC;
import schedual.model.Customer;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;


/**
 * Secondary Controller - CRUD Operations for Customers Table.
 * <p>
 *     (1) Presents Customers Menu Option
 *     <p>(2) Allows user to perform CRUD (Create, Read, Update, Delete) operations on Customers Table in MySQL.
 *      (Create) Performed by the Add Button.
 *      (Read) Performed on initialization and on updates.
 *      (Update) Performed by the Save Button for the selected item in the TableView. Edit button autofills selection's values to the field items below the TableView.
 *      (Delete) Performed by the Delete Button for the selected item in the TableView.
 */
public class CustomersController extends MainViewController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField phoneField;

    @FXML private Button backButton;
    @FXML private Button addButton;
    @FXML private Button clearButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private Button saveButton;


    @FXML
    private ComboBox<String> countryBox;
    @FXML
    private ComboBox<String> divisionBox;

    @FXML
    private TableView<Customer> customersTable;
    ObservableList<Integer> divisionValue = FXCollections.observableArrayList();
    private final String[] COLUMNS = {"Customer_ID", "Customer_Name", "Address", "Postal_Code", "Phone", "Division_Name", "Country_Name"};
    private ObservableList<String> countryList = FXCollections.observableArrayList();
    private ObservableList<String> countryListWithId = FXCollections.observableArrayList();
    private ObservableList<String> divisionList = FXCollections.observableArrayList();
    private ObservableList<String> divisionListWithIds = FXCollections.observableArrayList();

    /**
     * Initializes the Default Resources to the FXML Stage. FXML Handlers are provided with Event Actions through Lambda Expressions.
     * <p>
     * FXML Functional Interface Handlers as Lambda Expressions: FXML Handlers are functional interfaces known as Event Listeners. Rather than passing entire objects
     * that implement the interface, the use of Lambdas here passes a block of code that only runs when the button is clicked.
     * <p> (backButton.onAction) Loads the url for mainMenu.fxml when the Back Button is clicked by user.
     * <p> (clearButton.setOnAction) Updates the value fields below the TableView with null values.
     * <p> (addButton.setOnAction) Prompts the user with an alert and confirms if the user wishes to add a new value to the customers table in MySQL based on the parameters in the fields below the TableView.
     * <p> (editButton.setOnAction) Updates the value fields below the TableView with the corresponding fields of the Customer Object currently selected in the TableView.
     * <p> (deleteButton.setOnAction) Prompts the user with an alert and confirms if the user wishes to permanently remove the selected Customer in the TableView to the matching record in the MySQL database table.
     * <p> (saveButton.setOnAction) Prompts the user with an alert and confirms if the user wishes to overwrite the selected Customer in the TableView to the matching record in the MySQL database table.
     * <p> (countryBox.setOnAction) Sets the Division ID ComboBox with the first level division values for the Country_ID in the ComboBox above it.
     * @param url The filepath of this .fxml file.
     * @param resourceBundle Abstract Class which retrieves locale-specific objects.
     * */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadCountries();
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION from loadDivisions: " + e.getMessage());
            throw new RuntimeException(e);
        }

        //FXML HANDLERS
        backButton.setOnAction(event -> this.loadFXML(event, "mainMenu.fxml"));
        clearButton.setOnAction(event -> {
            nameField.clear();
            addressField.clear();
            postalCodeField.clear();
            phoneField.clear();
            countryBox.getSelectionModel().clearSelection();
            divisionBox.getSelectionModel().clearSelection();
        });
        addButton.setOnAction(event -> {
            try {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Add Customer Record");
                confirmation.setContentText("Do you want to add this new customer?");
                if (confirmation.showAndWait().get() == ButtonType.OK) {
                    inputValidation();
                    Customer customer = addCustomer();
                    String sql = "INSERT INTO Customers (customer_name, address, postal_code, phone," +
                            " create_date, created_by, last_update, last_updated_by, division_id)\n" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
                    PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
                    ps.setString(1, customer.getCustomer_Name());
                    ps.setString(2, customer.getAddress());
                    ps.setString(3, customer.getPostal_Code());
                    ps.setString(4, customer.getPhone());
                    ps.setString(5, LocalDateTime.now().toString());
                    ps.setString(6, JDBC.getUserName());
                    ps.setString(7, LocalDateTime.now().toString());
                    ps.setString(8, JDBC.getUserName());
                    ps.setInt(9, customer.getDivision_ID());
                    ps.executeUpdate();
                    loadTable();
                }
            } catch (Exception e) {
                Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                errorMessage.setTitle("Error Message");
                errorMessage.setContentText(e.getMessage());
                errorMessage.show();
            }
        });
        editButton.setOnAction(event -> {
            try {
                Customer selectCustomer = customersTable.getSelectionModel().getSelectedItem();
                nameField.setText(selectCustomer.getCustomer_Name());
                addressField.setText(selectCustomer.getAddress());
                postalCodeField.setText(selectCustomer.getPostal_Code());
                phoneField.setText(selectCustomer.getPhone());
                if (selectCustomer.getDivision_ID() > 54) {
                    if (selectCustomer.getDivision_ID() > 100) {
                        countryBox.getSelectionModel().select(1);
                        divisionBox.getSelectionModel().select(selectCustomer.getDivision_ID()-101);
                    } else {
                        countryBox.getSelectionModel().select(2);
                        divisionBox.getSelectionModel().select(selectCustomer.getDivision_ID()-60);
                    }
                } else {
                    countryBox.getSelectionModel().select(0);
                    divisionBox.getSelectionModel().select(selectCustomer.getDivision_ID()-1);
                }
            } catch (NullPointerException e) {
                Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                errorMessage.setTitle("Error Message");
                errorMessage.setContentText("No Selection Indicated");
                errorMessage.show();
            }
        });
        deleteButton.setOnAction(event -> {
            try{
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Delete Customer Record");
                confirmation.setContentText("Do you want to delete this customer and all associated appointments?\nID: "
                        + customersTable.getSelectionModel().getSelectedItem().getCustomer_ID()
                        + " - " + customersTable.getSelectionModel().getSelectedItem().getCustomer_Name());
                if (confirmation.showAndWait().get() == ButtonType.OK) {
                    Customer selectCustomer = customersTable.getSelectionModel().getSelectedItem();
                    String sql = "DELETE FROM Customers WHERE Customer_Id = ?";
                    PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
                    ps.setInt(1, selectCustomer.getCustomer_ID());
                    ps.execute();
                    loadTable();
                }
            } catch (SQLException e) {
                Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                errorMessage.setTitle("Error Message");
                errorMessage.setContentText(e.getMessage());
                errorMessage.show();
            } catch (NullPointerException e) {
                Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                errorMessage.setTitle("Error Message");
                    errorMessage.setContentText("No Selection Indicated");
                errorMessage.show();
            }
        });
        saveButton.setOnAction(event -> {
            try {
                Customer selectCustomer = customersTable.getSelectionModel().getSelectedItem();
                String sql = "UPDATE Customers SET customer_name = ?, address = ?, postal_code = ?, phone = ?, last_update = CURRENT_TIMESTAMP," +
                        " last_updated_by = ?, division_id = ? WHERE Customer_Id = ?";
                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
                ps.setString(1, nameField.getText());
                ps.setString(2, addressField.getText());
                ps.setString(3, postalCodeField.getText());
                ps.setString(4, phoneField.getText());
                ps.setString(5, JDBC.getUserName());
                ps.setInt(6, divisionValue.get((divisionBox.getSelectionModel().getSelectedIndex())));
                ps.setInt(7, selectCustomer.getCustomer_ID());
                ps.executeUpdate();
                loadTable();
            } catch (SQLException | NullPointerException e) {
                Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                errorMessage.setTitle("Error Message");
                errorMessage.setContentText(e.getMessage());
                errorMessage.show();
            }
        });
        countryBox.setOnAction(event -> {
            try {
                divisionValue = FXCollections.observableArrayList();
                divisionListWithIds = FXCollections.observableArrayList();
                divisionList = FXCollections.observableArrayList();
                String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
                ps.setInt(1,countryBox.getSelectionModel().getSelectedIndex()+1);
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    divisionList.add(rs.getString("Division"));
                    divisionListWithIds.add(rs.getInt("Division_ID") + " - " + rs.getString("Division"));
                    divisionValue.add(rs.getInt("Division_ID"));
                }
                divisionBox.setItems(divisionListWithIds);
            } catch (SQLException e) {
                Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                errorMessage.setTitle("Error Message");
                errorMessage.setContentText(e.getMessage());
                errorMessage.show();
            }
        });
        try {
            loadTable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the Country ID Combobox from countries table in MySQL.
     * @throws SQLException If a connection to the MySQL database cannot be established.
     * */
    public void loadCountries() throws SQLException {
        String sql = "SELECT * FROM countries";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            countryList.add(rs.getString("Country"));
            countryListWithId.add(rs.getInt("Country_ID") + " - " + rs.getString("Country"));
        }
        countryBox.setItems(countryListWithId);

    }

    /**
     * Loads customersTable TableView with all records from customers table in MySQL database.
     * @throws SQLException If a connection to the MySQL database cannot be established.
     * */
    public void loadTable() throws SQLException {
        ObservableList<Customer> customersList = FXCollections.observableArrayList();
        String sql = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, customers.Division_ID, Division, Country_ID " +
                "FROM customers JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID;";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Customer customer = new Customer(
                    rs.getInt("Customer_ID"),
                    rs.getString("Customer_Name"),
                    rs.getString("Address"),
                    rs.getString("Postal_Code"),
                    rs.getString("Phone"),
                    rs.getInt("Division_ID"),
                    rs.getString("Division"),
                    rs.getInt("Country_ID"),
                    countryList.get(rs.getInt("Country_ID") - 1));
            customersList.add(customer);
        }
        int  index = 0;
        for (String var : COLUMNS) {
            customersTable.getColumns().get(index++).setCellValueFactory(new PropertyValueFactory<>(var));
        }
        this.customersTable.setItems(customersList);
    }

    /**
     * Helper Method used when Add Button is selected.
     * @return Customer Object based on the fields below the TableView.
     * */
    public Customer addCustomer() {
        return new Customer(
                0,
                nameField.getText(),
                addressField.getText(),
                postalCodeField.getText(),
                phoneField.getText(),
                divisionValue.get((divisionBox.getSelectionModel().getSelectedIndex())),
                divisionList.get(divisionBox.getSelectionModel().getSelectedIndex()),
                countryBox.getSelectionModel().getSelectedIndex()+1,
                countryBox.getSelectionModel().getSelectedItem());
    }

    /** Performs Standard Input Validations for values in Field
     * @throws NullPointerException if no selection is indicated.
     * */
    public void inputValidation() throws NullPointerException {
        if (nameField.getText().isEmpty()
                || addressField.getText().isEmpty()
                || postalCodeField.getText().isEmpty()
                || phoneField.getText().isEmpty()
                || countryBox.getValue().isEmpty()
                || divisionBox.getValue().isEmpty()) {

            throw new RuntimeException("Fields May Not Be Empty");
        }
    }
}