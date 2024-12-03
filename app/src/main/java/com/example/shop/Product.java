package com.example.shop;
public class Product {
    private String id;
    private String title;
    private String description;
    private String price;
    private String imagePath;

    public Product(String id, String title, String description, String price, String imagePath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
