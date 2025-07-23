// File: Utils.java
package com.example.tranminhphat_2123110213;

import java.text.NumberFormat;
import java.util.Locale;

public class Utils {
    public static String dinhDangTienVietNam(int gia) {
        NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
        return format.format(gia) + " VNĐ";
    }
}
