package com.example.tranminhphat_2123110213;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private EditText edtName, edtAddress, edtPhone;
    private RadioGroup radioGroupPayment;
    private RadioButton radioCOD, radioOnline;
    private TextView txtTotal;
    private Button btnConfirm;

    private int tongTien = 1200000; // Tổng tiền cố định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment); // Layout đã sửa

        // Ánh xạ view
        edtName = findViewById(R.id.editTextName);
        edtAddress = findViewById(R.id.editTextAddress);
        edtPhone = findViewById(R.id.editTextPhone);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        radioCOD = findViewById(R.id.radioCOD);
        radioOnline = findViewById(R.id.radioOnline);
        txtTotal = findViewById(R.id.textTotal);
        btnConfirm = findViewById(R.id.btnConfirmPayment);
        LinearLayout cartItemsContainer = findViewById(R.id.cartItemsContainer);

        // Gán tổng tiền
        txtTotal.setText("Tổng tiền: " + tongTien + "đ");

        // Sự kiện xác nhận
        btnConfirm.setOnClickListener(v -> xuLyThanhToan());

        // Tạo danh sách sản phẩm
        List<Products> gioHang = new ArrayList<>();
        gioHang.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Dây chuyền đá quý", 120000, 35));
        gioHang.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Nhẫn vàng 24k", 120000, 35));
        gioHang.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng tay bạc", 120000, 35));
        gioHang.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Lắc tay vàng", 120000, 35));

        // Inflate từng item vào LinearLayout
        for (Products product : gioHang) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.item_cart_payment, cartItemsContainer, false);

            TextView txtName = itemView.findViewById(R.id.textName);
            TextView txtPrice = itemView.findViewById(R.id.textPrice);
            TextView txtQuantity = itemView.findViewById(R.id.textQuantity);
            android.widget.ImageView img = itemView.findViewById(R.id.imageProduct);

            txtName.setText(product.getName());
            txtPrice.setText(product.getPrice() + "đ");
            txtQuantity.setText("SL: " + product.getQuantity());
            img.setImageResource(product.getImage());

            cartItemsContainer.addView(itemView);
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

        String phuongThuc = (selectedId == R.id.radioCOD) ? "Thanh toán khi nhận hàng" : "Chuyển khoản ngân hàng";

        String thongTin = "Tên: " + ten +
                "\nĐịa chỉ: " + diaChi +
                "\nSĐT: " + soDT +
                "\nPTTT: " + phuongThuc +
                "\nTổng tiền: " + tongTien + "đ";

        Toast.makeText(this, "Đặt hàng thành công!\n" + thongTin, Toast.LENGTH_LONG).show();
    }
}
