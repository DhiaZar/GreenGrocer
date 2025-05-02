module com.example.greengrocermarketplace {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports main to javafx.graphics; // For launching the application
    opens main to javafx.fxml; // For FXML loading
    opens controller to javafx.fxml; // For FXML controllers
    opens images to javafx.graphics; // For image resource access
}