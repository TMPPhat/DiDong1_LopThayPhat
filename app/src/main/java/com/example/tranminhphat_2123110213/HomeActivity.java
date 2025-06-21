package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton btnCart = findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(it);
            }
        });

        GridView gridView = findViewById(R.id.SanPhamNoiBat);

        List<Products> danhSach = new ArrayList<>();
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổppppp"));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ đẹp"));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổooooo"));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ đẹp"));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng không cổ"));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ bạc"));

        ProductGridAdapter adapter = new ProductGridAdapter(this, danhSach);
        gridView.setAdapter(adapter);
    }
}
