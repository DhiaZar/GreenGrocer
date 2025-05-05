package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.CartItem;
import java.io.IOException; // Added import
import java.text.DecimalFormat;

public class CartController {
    @FXML private VBox cartContainer;
    @FXML private Label totalLabel;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @FXML
    public void initialize() {
        updateCartDisplay();
    }

    public void updateCartDisplay() {
        cartContainer.getChildren().clear();
        ProductCatalogController.getCart().getItems().removeIf(item -> item.getQuantity() <= 0);
        for (CartItem item : ProductCatalogController.getCart().getItems()) {
            HBox itemBox = new HBox(10);
            itemBox.getStyleClass().add("cart-item-box");

            ImageView imageView = new ImageView();
            String imagePath = "/images/" + item.getProduct().getImagePath();
            try {
                java.net.URL imageUrl = getClass().getResource(imagePath);
                if (imageUrl != null) {
                    imageView.setImage(new Image(imageUrl.toExternalForm()));
                } else {
                    imageView.setImage(new Image(getClass().getResource("/images/default.jpg").toExternalForm()));
                }
            } catch (Exception e) {
                imageView.setImage(new Image(getClass().getResource("/images/default.jpg").toExternalForm()));
            }
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);

            Label itemLabel = new Label(item.getProduct().getName() + " - $" + df.format(item.getProduct().getPrice()));
            itemLabel.getStyleClass().add("quantity-label");

            Button decrementButton = new Button("-");
            decrementButton.getStyleClass().add("quantity-button");
            decrementButton.setOnAction(e -> {
                item.decrement();
                updateCartDisplay();
            });

            Label quantityLabel = new Label(String.valueOf(item.getQuantity()));
            quantityLabel.getStyleClass().add("quantity-label");

            Button incrementButton = new Button("+");
            incrementButton.getStyleClass().add("quantity-button");
            incrementButton.setOnAction(e -> {
                int availableStock = item.getStock();
                if(item.getQuantity() < availableStock) {
                    item.increment();
                }

                updateCartDisplay();
            });

            itemBox.getChildren().addAll(imageView, itemLabel, decrementButton, quantityLabel, incrementButton);
            cartContainer.getChildren().add(itemBox);
        }
        totalLabel.setText("Total: $" + df.format(ProductCatalogController.getCart().getTotal()));
    }

    @FXML
    private void clearCart() {
        ProductCatalogController.getCart().clear();
        updateCartDisplay();
    }

    @FXML
    private void goBack() throws IOException {
        Stage stage = (Stage) cartContainer.getScene().getWindow();
        new ProductCatalogController().showProductCatalog(stage);
    }
}