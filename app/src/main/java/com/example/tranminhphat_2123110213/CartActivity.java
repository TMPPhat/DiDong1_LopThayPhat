package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cartLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnPayment = findViewById(R.id.btnPayment);

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), PaymentActivity.class);
                startActivity(it);
            }
        });


        ListView listView = findViewById(R.id.listViewCart);

        List<Products> gioHang = CartManager.getCartList();


        CartAdapter adapter = new CartAdapter(this, gioHang);
        listView.setAdapter(adapter);


        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        TextView btnDelete = findViewById(R.id.textDelete); // phải có id này trong layout XML
        btnDelete.setOnClickListener(v -> {
            List<Products> selectedItems = adapter.getSelectedItems();
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Bạn chưa chọn sản phẩm nào để xóa", Toast.LENGTH_SHORT).show();
            } else {
                CartManager.getCartList().removeAll(selectedItems); // cập nhật dữ liệu toàn cục nếu cần
                adapter.removeItems(selectedItems);                 // cập nhật giao diện
                Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });



    }
}
