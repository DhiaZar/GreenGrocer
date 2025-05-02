package model;

public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private String description;
    private String imagePath;

    public Product(int id, String name, double price, int stock, String description, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.imagePath = imagePath;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
}