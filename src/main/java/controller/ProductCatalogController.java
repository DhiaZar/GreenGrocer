package controller;

import dao.ProductDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Product;
import java.io.IOException;
import java.sql.SQLException;

public class ProductCatalogController {
    @FXML private GridPane productGrid;
    @FXML private Button adminUpdateButton;
    private ProductDAO productDAO = new ProductDAO();
    private static String currentRole;

    public static void setCurrentRole(String role) {
        currentRole = role;
    }

    @FXML
    public void initialize() {
        if (currentRole != null && currentRole.equals("admin")) {
            adminUpdateButton.setVisible(true);
        } else {
            adminUpdateButton.setVisible(false);
        }

        try {
            loadProducts();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            Label errorLabel = new Label("Failed to load products.");
            productGrid.add(errorLabel, 0, 0);
        } catch (Exception e) {
            System.err.println("Image loading error: " + e.getMessage());
            Label errorLabel = new Label("Failed to load images.");
            productGrid.add(errorLabel, 0, 0);
        }
    }

    private void loadProducts() throws SQLException {
        productGrid.getChildren().clear();
        var products = productDAO.getAllProducts();
        int row = 0, col = 0;
        for (Product product : products) {
            Label nameLabel = new Label(product.getName() + "\n$" + product.getPrice());
            ImageView imageView = new ImageView();
            String imagePath = "/images/" + product.getImagePath();
            System.out.println("Attempting to load image: " + imagePath); // Debug output
            try {
                java.net.URL imageUrl = getClass().getResource(imagePath);
                if (imageUrl != null) {
                    imageView.setImage(new Image(imageUrl.toExternalForm()));
                } else {
                    System.err.println("Image not found: " + imagePath);
                    imageView.setImage(new Image(getClass().getResource("/images/default.jpg").toExternalForm()));
                }
            } catch (Exception e) {
                System.err.println("Error loading image for " + product.getName() + ": " + e.getMessage());
                imageView.setImage(new Image(getClass().getResource("/images/default.jpg").toExternalForm()));
            }
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            productGrid.add(imageView, col, row);
            productGrid.add(nameLabel, col, row + 1);
            col++;
            if (col > 3) {
                col = 0;
                row += 2;
            }
        }
    }

    @FXML
    private void goToCart() {
        // Implement cart navigation
    }

    @FXML
    private void showAdminPanel() throws IOException, SQLException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdminPanel.fxml"));
        stage.setScene(new Scene(root, 300, 200));
        stage.setTitle("Admin Panel");
        stage.showAndWait();
    }
}