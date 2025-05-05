package controller;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    private UserDAO userDAO = new UserDAO();

    public void showLogin(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(root, 800, 600); // Changed to 400x300
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Greengrocer Marketplace");
        stage.show();
    }

    @FXML
    private void login() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = userDAO.validateUser(username, password);
        if (role != null) {
            ProductCatalogController.setCurrentRole(role);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            usernameField.clear();
            passwordField.clear();
            new ProductCatalogController().showProductCatalog(stage);
        } else {
            System.out.println("Invalid login");
        }
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        usernameField.clear();
        passwordField.clear();
        stage.close();
    }

    @FXML
    private void signUp() throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        new SignUpController().showSignUp(stage);
    }
}