package controller;

import dao.ProductDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Product;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminPanelController {
    @FXML private ChoiceBox<Product> productChoice;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private ChoiceBox<String> imageChoice;
    private ProductDAO productDAO = new ProductDAO();

    @FXML
    public void initialize() throws SQLException {
        var products = productDAO.getAllProducts();
        productChoice.getItems().addAll(products);
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

        List<String> imageFiles = new ArrayList<>();
        try {
            URL imageDirUrl = getClass().getResource("/images/");
            if (imageDirUrl != null) {
                File imageDir = new File(imageDirUrl.toURI());
                if (imageDir.exists() && imageDir.isDirectory()) {
                    File[] files = imageDir.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));
                    if (files != null) {
                        for (File file : files) {
                            imageFiles.add(file.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
        }

        if (imageFiles.isEmpty()) {
            imageFiles.add("default.jpg");
        }
        imageChoice.getItems().addAll(imageFiles);
        imageChoice.setValue("default.jpg");
    }

    @FXML
    private void updateProduct() throws SQLException, IOException {
        Product selectedProduct = productChoice.getValue();
        if (selectedProduct == null) {
            System.out.println("Please select a product to update");
            return;
        }

        int id = selectedProduct.getId();
        boolean updated = false;

        if (!priceField.getText().isEmpty()) {
            try {
                double price = Double.parseDouble(priceField.getText());
                productDAO.updateProductPrice(id, price);
                updated = true;
            } catch (NumberFormatException e) {
                System.err.println("Invalid price format: " + e.getMessage());
                return;
            }
        }

        if (!stockField.getText().isEmpty()) {
            try {
                int stock = Integer.parseInt(stockField.getText());
                productDAO.updateProductStock(id, stock);
                updated = true;
            } catch (NumberFormatException e) {
                System.err.println("Invalid stock format: " + e.getMessage());
                return;
            }
        }

        if (updated) {
            System.out.println("Product updated successfully");
            Stage stage = (Stage) productChoice.getScene().getWindow();
            new ProductCatalogController().showProductCatalog(stage);
        }
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) productChoice.getScene().getWindow();
        try {
            new ProductCatalogController().showProductCatalog(stage);
        } catch (IOException e) {
            System.err.println("Error navigating back: " + e.getMessage());
        }
    }

    @FXML
    private void goBack() throws IOException {
        Stage stage = (Stage) productChoice.getScene().getWindow();
        new ProductCatalogController().showProductCatalog(stage);
    }

    @FXML
    private void addProduct() throws SQLException, IOException {
        String name = nameField.getText();
        String description = descriptionField.getText();
        if (name.isEmpty() || description.isEmpty() || imageChoice.getValue() == null) {
            System.out.println("Name, description, and image must be provided");
            return;
        }
        try {
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            productDAO.addProduct(name, price, stock, description, imageChoice.getValue());
            System.out.println("Product added successfully");
            Stage stage = (Stage) nameField.getScene().getWindow();
            new ProductCatalogController().showProductCatalog(stage);
        } catch (NumberFormatException e) {
            System.err.println("Invalid price or stock format");
        }
    }
}