package com.example.ict372ecommerceapp;

// ✅ A simple data model representing one product
public class Product {
    public int id;
    public String name;
    public String price;
    public String description;
    public String imageUrl;
    public int imageResId; // Drawable resource ID for local images
    public String[] colors;
    public int[] colorImageResIds; // Image for each color option

    public Product(int id, String name, String price, String description, String imageUrl, String[] colors) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.imageResId = 0;
        this.colors = colors;
        this.colorImageResIds = new int[0];
    }

    public Product(int id, String name, String price, String description, int imageResId, String[] colors) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = "";
        this.imageResId = imageResId;
        this.colors = colors;
        this.colorImageResIds = new int[0];
    }

    public Product(int id, String name, String price, String description, String[] colors, int[] colorImageResIds) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = "";
        this.imageResId = colorImageResIds.length > 0 ? colorImageResIds[0] : 0;
        this.colors = colors;
        this.colorImageResIds = colorImageResIds;
    }
}
