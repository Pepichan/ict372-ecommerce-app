package com.example.ict372ecommerceapp;

// ✅ A simple data model representing one product
public class Product {
    public int id;
    public String name;
    public String price;
    public String description;
    public String imageUrl;
    public String[] colors;

    public Product(int id, String name, String price, String description, String imageUrl, String[] colors) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.colors = colors;
    }
}
