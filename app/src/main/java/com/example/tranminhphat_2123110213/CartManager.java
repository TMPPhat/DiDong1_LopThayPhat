// CartManager.java
package com.example.tranminhphat_2123110213;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final List<Products> cartList = new ArrayList<>();

    public static void addToCart(Products product) {
        cartList.add(product);
    }

    public static List<Products> getCartList() {
        return cartList;
    }

    public static void clearCart() {
        cartList.clear();
    }
}
