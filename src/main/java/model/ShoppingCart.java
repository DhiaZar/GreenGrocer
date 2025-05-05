package model;

import controller.ProductCatalogController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        return items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public void clear() {
        items.clear();
    }

    public int getItemQuantity(Product product) {
        // Iterate through the list of CartItem objects
        for (CartItem item : items) {
            // Check if the Product within the current CartItem matches the product passed in
            if (item.getProduct().getId() == product.getId()) {
                // If found, return the quantity of this CartItem
                return item.getQuantity();
            }
        }
        // If the loop finishes without finding the product, it's not in the cart, so return 0
        return 0;
    }
}