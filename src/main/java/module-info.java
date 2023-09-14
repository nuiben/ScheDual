/**
 * Schedual Package Includes Model, View, and Controller Packages.
 */

module schedual {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens schedual to javafx.fxml;
    exports schedual;
    exports schedual.model;
    opens schedual.model to javafx.fxml;
    exports schedual.controller;
    opens schedual.controller to javafx.fxml;
}