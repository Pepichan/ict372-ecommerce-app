package com.example.ict372ecommerceapp;

import java.util.List;

public class Order {
    public int id;
    public int userId;
    public String fullName;
    public String email;
    public String address;
    public String city;
    public String postalCode;
    public String phoneNumber;
    public String paymentMethod;
    public String orderDate;
    public double totalAmount;
    public String status;
    public String itemsJson; // JSON string of cart items

    public Order() {}

    public Order(int userId, String fullName, String email, String address, String city,
                 String postalCode, String phoneNumber, String paymentMethod,
                 String orderDate, double totalAmount, String status, String itemsJson) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.itemsJson = itemsJson;
    }

    public String getTotalFormatted() {
        return String.format("$%.2f", totalAmount);
    }
}
