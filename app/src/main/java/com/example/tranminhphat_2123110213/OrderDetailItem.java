package com.example.tranminhphat_2123110213;

public class OrderDetailItem {
    private String productName;
    private String imageUrl;
    private int quantity;
    private int price;

    public OrderDetailItem(String productName, String imageUrl, int quantity, int price) {
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductName() { return productName; }
    public String getImageUrl() { return imageUrl; }
    public int getQuantity() { return quantity; }
    public int getPrice() { return price; }
}

