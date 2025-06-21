package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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

        List<Products> gioHang = new ArrayList<>();
        gioHang.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Nhẫn vàng 24k"));
        gioHang.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng tay bạc"));
        gioHang.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Dây chuyền đá quý"));

        CartAdapter adapter = new CartAdapter(this, gioHang);
        listView.setAdapter(adapter);
    }
}
