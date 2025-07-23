package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.*;

public class PaymentActivity extends AppCompatActivity {

    private EditText edtName, edtAddress, edtPhone;
    private RadioGroup radioGroupPayment;
    private RadioButton radioCOD, radioOnline;
    private TextView txtTotal;
    private Button btnConfirm;
    private LinearLayout cartItemsContainer;
    private ImageView qrImageView;

    private int tongTien = 0;
    private List<Products> gioHang = new ArrayList<>();
    private static final String TOKEN = "A1Qn1X9pYbQyNahPGDWHlEdpbMd6qDBj"; // 🔐 Thay bằng token thật

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        edtName = findViewById(R.id.editTextName);
        edtAddress = findViewById(R.id.editTextAddress);
        edtPhone = findViewById(R.id.editTextPhone);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        radioCOD = findViewById(R.id.radioCOD);
        radioOnline = findViewById(R.id.radioOnline);
        txtTotal = findViewById(R.id.textTotal);
        btnConfirm = findViewById(R.id.btnConfirmPayment);
        cartItemsContainer = findViewById(R.id.cartItemsContainer);
        qrImageView = findViewById(R.id.qrImageView);
        qrImageView.setVisibility(View.GONE);

        gioHang = (List<Products>) getIntent().getSerializableExtra("selected_items");
        if (gioHang == null) gioHang = new ArrayList<>();

        hienThiSanPhamDaChon();
        txtTotal.setText("Tổng tiền: " + tongTien + "đ");

        radioGroupPayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioOnline) {
                hienThiQRThanhToan();
            } else {
                qrImageView.setVisibility(View.GONE);
            }
        });

        // Đổ thông tin user
        UserManager userManager = new UserManager(this);
        User currentUser = userManager.getUser();
        if (currentUser != null) {
            edtName.setText(currentUser.getFullname());
            edtPhone.setText(currentUser.getPhone());
            edtAddress.setText(currentUser.getAddress());
        }

        btnConfirm.setOnClickListener(v -> xuLyThanhToan());
    }

    private void hienThiSanPhamDaChon() {
        for (Products product : gioHang) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_cart_payment, cartItemsContainer, false);
            TextView txtName = itemView.findViewById(R.id.textName);
            TextView txtPrice = itemView.findViewById(R.id.textPrice);
            TextView txtQuantity = itemView.findViewById(R.id.textQuantity);
            ImageView img = itemView.findViewById(R.id.imageProduct);

            txtName.setText(product.getProduct_name());
            txtPrice.setText(product.getPrice() + "đ");
            txtQuantity.setText("SL: " + product.getQuantity());

            int imageResId = product.getImageResId();
            if (imageResId != 0) {
                img.setImageResource(imageResId);
            } else {
                img.setImageResource(R.drawable.default_image);
            }

            cartItemsContainer.addView(itemView);
            tongTien += product.getPrice() * product.getQuantity();
        }
    }

    private void xuLyThanhToan() {
        String ten = edtName.getText().toString().trim();
        String diaChi = edtAddress.getText().toString().trim();
        String soDT = edtPhone.getText().toString().trim();

        if (ten.isEmpty() || diaChi.isEmpty() || soDT.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = radioGroupPayment.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
            return;
        }

        User currentUser = new UserManager(this).getUser();
        if (currentUser == null) {
            Toast.makeText(this, "Không có thông tin người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }

        String paymentMethod = (selectedId == R.id.radioCOD) ? "COD" : "QR";
        String urlOrder = "https://api.baserow.io/api/database/rows/table/617808/?user_field_names=true";

        JSONObject orderData = new JSONObject();
        try {
            orderData.put("Name", "Don hang cua " + ten);
            orderData.put("user_id", currentUser.getId());
            orderData.put("name", ten);
            orderData.put("email", currentUser.getEmail());
            orderData.put("phone", soDT);
            orderData.put("address", diaChi);
            orderData.put("status", "Chờ xác nhận");
            orderData.put("qty", tinhTongSoLuong());
            orderData.put("total", tongTien);
            orderData.put("payment", paymentMethod);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest orderRequest = new JsonObjectRequest(Request.Method.POST, urlOrder, orderData,
                response -> {
                    try {
                        int orderId = response.getInt("id");
                        guiChiTietDonHang(orderId);
                        Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                        xoaSanPhamDaChonKhoiGioHang();
                        Intent intent = new Intent(PaymentActivity.this, OrdersActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String body = new String(error.networkResponse.data);
                        Log.e("ORDER_ERROR", body);
                        Toast.makeText(this, "Lỗi tạo đơn hàng: " + body, Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("ORDER_ERROR", "Volley error: " + error.toString());
                        Toast.makeText(this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " + TOKEN);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(orderRequest);
    }

    private void guiChiTietDonHang(int orderId) {
        String urlDetail = "https://api.baserow.io/api/database/rows/table/617823/?user_field_names=true";

        for (Products p : gioHang) {
            JSONObject data = new JSONObject();
            try {
                data.put("order_id", orderId);
                data.put("product_id", p.getId());
                data.put("qty", p.getQuantity());
                data.put("price", p.getPrice());
                data.put("product_name", p.getProduct_name());
                data.put("image", p.getImageResId());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlDetail, data,
                    response -> {},
                    error -> {
                        Toast.makeText(this, "Lỗi khi gửi chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Token " + TOKEN);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            Volley.newRequestQueue(this).add(request);
        }
    }

    private int tinhTongSoLuong() {
        int tong = 0;
        for (Products p : gioHang) {
            tong += p.getQuantity();
        }
        return tong;
    }

    private void hienThiQRThanhToan() {
        String stk = "930082005"; // Số tài khoản
        int amount = tongTien;
        String orderNote = "Thanh toán đơn tại shop TMP";

        String qrUrl = "https://img.vietqr.io/image/970422-" + stk + "-compact.jpg"
                + "?amount=" + amount
                + "&addInfo=" + Uri.encode(orderNote);

        qrImageView.setVisibility(View.VISIBLE);
        Glide.with(this).load(qrUrl).into(qrImageView);
    }
    private void xoaSanPhamDaChonKhoiGioHang() {
        for (Products p : gioHang) {
            int rowId = p.getCartRowId(); // Đảm bảo cartRowId đã được gán khi load từ Baserow
            if (rowId > 0) {
                String url = "https://api.baserow.io/api/database/rows/table/616928/" + rowId + "/?user_field_names=true";
                StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                        response -> Log.d("DELETE", "Đã xoá sản phẩm ID: " + rowId),
                        error -> Log.e("DELETE", "Lỗi xoá: " + error.getMessage())) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Token " + TOKEN);
                        return headers;
                    }
                };
                Volley.newRequestQueue(this).add(deleteRequest);
            }
        }
    }

}
