package controller;

import dao.ProductDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Product;
import java.sql.SQLException;

public class AdminPanelController {
    @FXML private ChoiceBox<Product> productChoice; // Changed to ChoiceBox<Product>
    @FXML private TextField priceField;
    @FXML private TextField stockField;

    private ProductDAO productDAO = new ProductDAO();

    @FXML
    public void initialize() throws SQLException {
        var products = productDAO.getAllProducts();
        productChoice.getItems().addAll(products);
        // Set the string representation for the ChoiceBox (display product name)
        productChoice.setConverter(new javafx.util.StringConverter<Product>() {
            @Override
            public String toString(Product product) {
                return product != null ? product.getName() : "";
            }

            @Override
            public Product fromString(String string) {
                return productChoice.getItems().stream()
                        .filter(p -> p.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    @FXML
    private void updateProduct() throws SQLException {
        Product selectedProduct = productChoice.getValue();
        if (selectedProduct != null) {
            int id = selectedProduct.getId(); // Get the ID from the selected Product
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            productDAO.updateProduct(id, price, stock);
        }
        Stage stage = (Stage) productChoice.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) productChoice.getScene().getWindow();
        stage.close();
    }
}