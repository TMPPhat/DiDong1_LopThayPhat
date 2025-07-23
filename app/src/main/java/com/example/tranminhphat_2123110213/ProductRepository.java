package com.example.tranminhphat_2123110213;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private static List<Products> productList = new ArrayList<>();

    public static void setProductList(List<Products> list) {
        productList = list;
    }

    public static List<Products> getProductList() {
        return productList;
    }
}
