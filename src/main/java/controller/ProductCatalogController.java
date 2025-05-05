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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Product;
import model.ShoppingCart;
import java.io.IOException;
import java.sql.SQLException;

public class ProductCatalogController {
    @FXML private GridPane productGrid;
    @FXML private Button adminUpdateButton;
    @FXML private Button viewCartButton;
    private ProductDAO productDAO = new ProductDAO();
    private static String currentRole;
    private static final ShoppingCart cart = new ShoppingCart();

    public static void setCurrentRole(String role) {
        currentRole = role;
    }

    public static ShoppingCart getCart() {
        return cart;
    }

    public void showProductCatalog(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ProductCatalog.fxml"));
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Greengrocer Marketplace");
        stage.show();
    }

    @FXML
    public void initialize() {
        if (currentRole != null && currentRole.equals("admin")) {
            adminUpdateButton.setVisible(true);
            viewCartButton.setVisible(false);
        } else {
            adminUpdateButton.setVisible(false);
            viewCartButton.setVisible(true);
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
            VBox productBox = new VBox(5);
            productBox.getStyleClass().add("product-box");

            // Updated to include stock amount
            Label nameLabel = new Label(product.getName() + "\n$" + product.getPrice() + "\nStock: " + product.getStock());
            nameLabel.getStyleClass().add("product-label");

            ImageView imageView = new ImageView();
            String imagePath = "/images/" + product.getImagePath();
            System.out.println("Attempting to load image: " + imagePath);
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

            if (!"admin".equals(currentRole)) {
                Button addToCartButton = new Button("Add to Cart");
                addToCartButton.getStyleClass().add("add-to-cart-button");
                addToCartButton.setOnAction(event -> {
                    int availableStock = product.getStock();
                    int currentCartQuantity = cart.getItemQuantity(product);
                    if (currentCartQuantity < availableStock) {
                        cart.addItem(product, 1);
                    }
                    System.out.println("Added " + product.getName() + " to cart");
                });
                productBox.getChildren().add(addToCartButton);
            }

            productBox.getChildren().addAll(imageView, nameLabel);
            productGrid.add(productBox, col, row);
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    private void goToCart() throws IOException {
        Stage stage = (Stage) viewCartButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Cart.fxml"));
        Parent root = loader.load();
        CartController controller = loader.getController();
        controller.updateCartDisplay();
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Shopping Cart");
    }

    @FXML
    private void showAdminPanel() throws IOException, SQLException {
        Stage stage = (Stage) adminUpdateButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdminPanel.fxml"));
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Admin Panel");
    }

    @FXML
    private void goBack() throws IOException {
        Stage stage = (Stage) viewCartButton.getScene().getWindow();
        new LoginController().showLogin(stage);
    }
}