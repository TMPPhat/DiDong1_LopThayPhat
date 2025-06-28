package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AllProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_products);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.AllProduct), (v, insets) -> {
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

        GridView products = findViewById(R.id.products);

        List<Products> danhSach = new ArrayList<>();
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổppppp", 120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ đẹp",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổooooo",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ đẹp",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng không cổ",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ bạc",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổppppp", 120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ đẹp",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổooooo",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ đẹp",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng không cổ",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ bạc",120000, 35));

        ProductGridAdapter adapter = new ProductGridAdapter(this, danhSach);
        products.setAdapter(adapter);

        BottomNavHelper.setupBottomNav(this, R.id.nav_products);

        // Trong onCreate:
        LinearLayout layoutFilter = findViewById(R.id.LayoutFilter);
        layoutFilter.setOnClickListener(v -> showFilterBottomSheet());


    }
    // Hàm mở filter
    private void showFilterBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = LayoutInflater.from(this).inflate(R.layout.activity_filter, null);
        bottomSheetDialog.setContentView(sheetView);

        Button btnApply = sheetView.findViewById(R.id.btnApplyFilter);
        btnApply.setOnClickListener(view -> {
            // TODO: lấy dữ liệu lọc ở đây nếu cần
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}