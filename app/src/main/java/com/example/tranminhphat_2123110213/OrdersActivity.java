package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class OrdersActivity extends AppCompatActivity {

    private LinearLayout layoutDonHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.OrdersScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavHelper.setupBottomNav(this, R.id.nav_order);
        layoutDonHang = findViewById(R.id.danhSachDonHang); // LinearLayout cha chứa đơn hàng

        getOrdersFromAPI();
    }

    private void getOrdersFromAPI() {
        String url = "https://api.baserow.io/api/database/rows/table/617808/?user_field_names=true";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject order = results.getJSONObject(i);

                            int id = order.getInt("id");

                            int total = order.getInt("total");

                            // Trường kiểu single select: status và payment
                            JSONObject statusObject = order.getJSONObject("status");
                            String status = statusObject.getString("value");



                            // Gọi hàm để hiển thị đơn hàng (tuỳ theo bạn)
                            addOrderItem(id, total, status);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi đọc dữ liệu JSON!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Lỗi kết nối API!", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token A1Qn1X9pYbQyNahPGDWHlEdpbMd6qDBj"); // 🔐 thay bằng token thật
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }



    private void addOrderItem(int orderId, int total, String status) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View orderView = inflater.inflate(R.layout.item_order, layoutDonHang, false);

        TextView tvMaDon = orderView.findViewById(R.id.tvMaDon);
        TextView tvTongTien = orderView.findViewById(R.id.tvTongTien);
        TextView tvTrangThai = orderView.findViewById(R.id.tvTrangThai);

        tvMaDon.setText("Mã đơn: #" + orderId);
        tvTongTien.setText("Tổng: " + Utils.dinhDangTienVietNam(total));
        tvTrangThai.setText("Trạng thái: " + status);

        layoutDonHang.addView(orderView);

        orderView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
            intent.putExtra("order_id", orderId);
            startActivity(intent);
        });
    }
}
