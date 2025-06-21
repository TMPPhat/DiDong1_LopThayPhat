package com.example.tranminhphat_2123110213;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private EditText edtName, edtAddress, edtPhone;
    private RadioGroup radioGroupPayment;
    private RadioButton radioCOD, radioOnline;
    private TextView txtTotal;
    private Button btnConfirm;

    private int tongTien = 1200000; // Giả định tổng tiền cố định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Ánh xạ
        edtName = findViewById(R.id.editTextName);
        edtAddress = findViewById(R.id.editTextAddress);
        edtPhone = findViewById(R.id.editTextPhone);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        radioCOD = findViewById(R.id.radioCOD);
        radioOnline = findViewById(R.id.radioOnline);
        txtTotal = findViewById(R.id.textTotal);
        btnConfirm = findViewById(R.id.btnConfirmPayment);

        // Hiển thị tổng tiền
        txtTotal.setText("Tổng tiền: " + tongTien + "đ");

        // Sự kiện xác nhận
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyThanhToan();
            }
        });
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

        // Xử lý logic thanh toán (hiện tại chỉ hiển thị thông báo)
        String thongTin = "Tên: " + ten +
                "\nĐịa chỉ: " + diaChi +
                "\nSĐT: " + soDT +
                "\nPTTT: " + phuongThuc +
                "\nTổng tiền: " + tongTien + "đ";

        Toast.makeText(this, "Đặt hàng thành công!\n" + thongTin, Toast.LENGTH_LONG).show();

        // Sau khi xác nhận có thể:
        // - Gửi dữ liệu về server
        // - Điều hướng sang màn hình "Cảm ơn"
        // - Xóa giỏ hàng, v.v.
    }
}
