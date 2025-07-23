package com.example.tranminhphat_2123110213;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {

    private int orderId;
    private final String detailUrl = "https://api.baserow.io/api/database/rows/table/617823/?user_field_names=true";
    private final String orderUrlBase = "https://api.baserow.io/api/database/rows/table/617808/";
    private TextView txtOrderTotal, txtOrderStatus, txtOrderAddress, txtOrderPayment, txtOrderCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.orderDetailActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        orderId = getIntent().getIntExtra("order_id", -1);
        if (orderId == -1) {
            Toast.makeText(this, "Không tìm thấy đơn hàng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Gán TextView
        txtOrderCode = findViewById(R.id.txtOrderCode);
        txtOrderTotal = findViewById(R.id.txtTotal);
        txtOrderStatus = findViewById(R.id.txtOrderStatus);
        txtOrderAddress = findViewById(R.id.txtShippingAddress);
        txtOrderPayment = findViewById(R.id.txtPaymentMethod);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        loadOrderDetails();     // Chi tiết sản phẩm
        loadOrderInfo(orderId); // Thông tin đơn hàng
    }

    private void loadOrderDetails() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, detailUrl, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        List<OrderDetailItem> detailList = new ArrayList<>();

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject item = results.getJSONObject(i);

                            if (item.getInt("order_id") == orderId) {
                                String name = item.getString("product_name");
                                String image = item.getString("image"); // Tên ảnh trong drawable
                                int qty = item.getInt("qty");
                                int price = item.getInt("price");

                                detailList.add(new OrderDetailItem(name, image, qty, price));
                            }
                        }

                        showOrderDetails(detailList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token A1Qn1X9pYbQyNahPGDWHlEdpbMd6qDBj");
                return headers;
            }
        };

        queue.add(request);
    }

    private void loadOrderInfo(int id) {
        String url = orderUrlBase + id + "/?user_field_names=true";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        int total = response.getInt("total");
                        String address = response.getString("address"); // <-- Sửa ở đây

                        JSONObject statusObj = response.getJSONObject("status");
                        JSONObject paymentObj = response.getJSONObject("payment");

                        String status = statusObj.getString("value");
                        String payment = paymentObj.getString("value");


                        txtOrderCode.setText("Mã đơn hàng: #" + response.getInt("id"));
                        txtOrderTotal.setText("Tổng tiền: ₫" + total);
                        txtOrderStatus.setText("Trạng thái: " + status);
                        txtOrderAddress.setText("Địa chỉ: " + address);
                        txtOrderPayment.setText("Thanh toán: " + payment);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi đọc dữ liệu đơn hàng!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Lỗi kết nối API đơn hàng!", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token A1Qn1X9pYbQyNahPGDWHlEdpbMd6qDBj");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void showOrderDetails(List<OrderDetailItem> details) {
        LinearLayout layout = findViewById(R.id.productListContainer);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (OrderDetailItem item : details) {
            android.view.View view = inflater.inflate(R.layout.item_order_detail, layout, false);

            ImageView imgProduct = view.findViewById(R.id.imgProduct);
            TextView txtName = view.findViewById(R.id.txtProductName);
            TextView txtQty = view.findViewById(R.id.txtProductQuantity);
            TextView txtPrice = view.findViewById(R.id.txtProductPrice);

            txtName.setText(item.getProductName());
            txtQty.setText("Số lượng: " + item.getQuantity());
            txtPrice.setText("₫" + item.getPrice());

            int imageResId = getResources().getIdentifier(item.getImageUrl(), "drawable", getPackageName());
            if (imageResId != 0) {
                imgProduct.setImageResource(imageResId);
            }

            layout.addView(view);
        }
    }
}
