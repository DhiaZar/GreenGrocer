package model;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = Math.max(0, quantity); // Allow quantity to be 0
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, quantity); // Allow quantity to be 0
    }

    public void increment() {
        setQuantity(quantity + 1);
    }

    public void decrement() {
        setQuantity(quantity - 1);
    }

    public int getStock() {
        return product.getStock();
    }
}