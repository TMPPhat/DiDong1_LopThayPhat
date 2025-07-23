package com.example.tranminhphat_2123110213;

import java.io.Serializable;
import java.util.List;

public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Integer> categoryIdList;
    // === Thuộc tính cơ bản ===
    private final int id;
    private int imageResId;
    private final String product_name;
    private final int price;
    private final int views;

    private int qty; // số lượng trong giỏ hàng
    private boolean isChecked = false; // trạng thái chọn trong giỏ
    private int cartRowId = -1; // ID hàng trong bảng cart trên Baserow (nếu có)

    private String brand;    // thương hiệu
    private String category; // danh mục

    // === Constructor ===
    public Products(int id, int imageResId, String product_name, int price, int views, String brand, String category) {
        this.id = id;
        this.imageResId = imageResId;
        this.product_name = product_name;
        this.price = price;
        this.views = views;
        this.qty = 1; // mặc định là 1 khi thêm mới
        this.brand = brand;
        this.category = category;
    }

    // ✅ Copy constructor
    public Products(Products other) {
        this.id = other.id;
        this.imageResId = other.imageResId;
        this.product_name = other.product_name;
        this.price = other.price;
        this.views = other.views;
        this.qty = other.qty;
        this.brand = other.brand;
        this.category = other.category;
        this.cartRowId = other.cartRowId;
        this.isChecked = other.isChecked;
    }

    // === Getter ===
    public int getId() {
        return id;
    }


    public List<Integer> getCategoryIdList() {
        return categoryIdList;
    }

    private int categoryId;

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryIdList(List<Integer> categoryIdList) {
        this.categoryIdList = categoryIdList;
    }
    public int getImageResId() {
        return imageResId;
    }

    public String getProduct_name() {
        return product_name;
    }

    public int getPrice() {
        return price;
    }

    public int getViews() {
        return views;
    }

    public int getQuantity() {
        return qty;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public int getCartRowId() {
        return cartRowId;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    // === Setter ===
    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public void setQuantity(int qty) {
        this.qty = qty;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    public void setCartRowId(int cartRowId) {
        this.cartRowId = cartRowId;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
