package com.example.tranminhphat_2123110213;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final List<Products> cartItems = new ArrayList<>();

    // ✅ Thêm sản phẩm (nếu đã có thì tăng số lượng)
    public static void addToCart(Products newProduct) {
        for (Products p : cartItems) {
            if (p.getId() == newProduct.getId()) {
                p.setQuantity(p.getQuantity() + 1);  // tăng số lượng nếu đã tồn tại
                return;
            }
        }
        // Nếu chưa có, thêm mới và đặt số lượng = 1 nếu chưa có
        if (newProduct.getQuantity() == 0) {
            newProduct.setQuantity(1);
        }
        cartItems.add(newProduct);
    }

    // ✅ Truy cập danh sách
    public static List<Products> getCartItems() {
        return cartItems;
    }

    public static List<Products> getCartList() {
        return cartItems;
    }
    public static void setCartItems(List<Products> items) {
        cartItems.clear();
        cartItems.addAll(items);
    }


    // ✅ Xoá tất cả sản phẩm trong giỏ
    public static void clearCart() {
        cartItems.clear();
    }

    // ✅ Tính tổng số lượng sản phẩm trong giỏ
    public static int getTotalQuantity() {
        int total = 0;
        for (Products product : cartItems) {
            total += product.getQuantity();
        }
        return total;
    }

    // ✅ Tổng số lượng các sản phẩm đã chọn (checkbox true)
    public static int getSelectedItemCount() {
        int count = 0;
        for (Products product : cartItems) {
            if (product.isChecked()) {
                count++;
            }
        }
        return count;
    }

    // ✅ Tổng tiền các sản phẩm đã chọn
    public static int getSelectedTotalPrice() {
        int total = 0;
        for (Products product : cartItems) {
            if (product.isChecked()) {
                total += product.getQuantity() * product.getPrice();
            }
        }
        return total;
    }


}
