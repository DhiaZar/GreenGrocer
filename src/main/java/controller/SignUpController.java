package controller;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import java.io.IOException;

public class SignUpController {
    @FXML private TextField newUsernameField;
    @FXML private PasswordField newPasswordField;
    @FXML private TextField roleField;
    private UserDAO userDAO = new UserDAO();

    public void showSignUp(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignUp.fxml"));
        Scene scene = new Scene(root, 800, 600); // Changed to 400x300
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Sign Up");
        stage.show();
    }

    @FXML
    private void register() throws IOException {
        String username = newUsernameField.getText();
        String password = newPasswordField.getText();
        String role = roleField.getText();
        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            System.out.println("All fields must be filled");
            return;
        }
        if (userDAO.addUser(username, password, role)) {
            System.out.println("User registered successfully");
            Stage stage = (Stage) newUsernameField.getScene().getWindow();
            new LoginController().showLogin(stage);
        } else {
            System.out.println("Registration failed");
        }
    }

    @FXML
    private void goBack() throws IOException {
        Stage stage = (Stage) newUsernameField.getScene().getWindow();
        new LoginController().showLogin(stage);
    }
}