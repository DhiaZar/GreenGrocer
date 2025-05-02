package controller;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void login() throws IOException, SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = userDAO.getUser(username, password);
        if (user != null) {
            ProductCatalogController.setCurrentRole(user.getRole());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ProductCatalog.fxml"));
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } else {
            System.out.println("Invalid credentials");
        }
    }
}