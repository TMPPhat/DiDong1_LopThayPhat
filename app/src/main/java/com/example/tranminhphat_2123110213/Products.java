package com.example.tranminhphat_2123110213;

public class Products {
    int imageResId;
    String tenSanPham;
    int gia;
    int daBan;
    public boolean isChecked = false;
    // Constructor
    public Products(int imageResId, String tenSanPham, int gia, int daBan) {
        this.imageResId = imageResId;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.daBan = daBan;
    }

    // Getter và Setter
    public int getImage() {
        return imageResId;
    }

    public void setImage(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getName() {
        return tenSanPham;
    }

    public void setName(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getPrice() {
        return gia;
    }

    public void setPrice(int gia) {
        this.gia = gia;
    }

    public int getQuantity() {
        return daBan;
    }

    public void setQuantity(int daBan) {
        this.daBan = daBan;
    }


}
