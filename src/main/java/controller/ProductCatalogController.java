package controller;

import dao.ProductDAO;
import model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.sql.SQLException;

public class ProductCatalogController {
    @FXML private GridPane productGrid;
    private ProductDAO productDAO = new ProductDAO();

    @FXML
    public void initialize() {
        try {
            loadProducts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() throws SQLException {
        productGrid.getChildren().clear();
        var products = productDAO.getAllProducts();
        int row = 0, col = 0;
        for (Product product : products) {
            Label label = new Label(product.getName() + "\n$" + product.getPrice());
            productGrid.add(label, col, row);
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    private void goToCart() {
        // Navigate to cart screen (implement navigation logic)
    }
}