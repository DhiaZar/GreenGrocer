module com.example.greengrocermarketplace {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // For MySQL Connector/J

    opens main to javafx.fxml; // For FXML loading
    opens controller to javafx.fxml; // For FXML controllers

    exports main to javafx.graphics; // Export main package to javafx.graphics for the launcher
}