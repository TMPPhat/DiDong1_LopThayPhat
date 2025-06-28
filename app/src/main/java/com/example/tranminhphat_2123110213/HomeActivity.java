package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        //SliderBanner
        ViewFlipper bannerFlipper = findViewById(R.id.bannerFlipper);

        // Danh sách ảnh banner (thay bằng ảnh thật của bạn)
        int[] banners = {
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3,
                R.drawable.banner4,
        };

        for (int banner : banners) {
            ImageView img = new ImageView(this);
            img.setImageResource(banner);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            bannerFlipper.addView(img);
        }




        ImageButton btnCart = findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(it);
            }
        });

        GridView SanPhamNoiBat = findViewById(R.id.SanPhamNoiBat);
        GridView SanPhamMoi = findViewById(R.id.SanPhamMoi);
        GridView SanPhamGiamGia = findViewById(R.id.SanPhamGiamGia);

        List<Products> danhSach = new ArrayList<>();
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổppppp", 120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ đẹp",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổooooo",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ đẹp",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng không cổ",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ bạc",120000, 35));

        ProductGridAdapter adapter = new ProductGridAdapter(this, danhSach);
        SanPhamNoiBat.setAdapter(adapter);SanPhamMoi.setAdapter(adapter);SanPhamGiamGia.setAdapter(adapter);


        ExpandableHeightGridView gridViewNoiBat = findViewById(R.id.SanPhamNoiBat);
        gridViewNoiBat.setExpanded(true);
        ExpandableHeightGridView gridViewMoi = findViewById(R.id.SanPhamMoi);
        gridViewMoi.setExpanded(true);
        ExpandableHeightGridView gridViewGiamGia = findViewById(R.id.SanPhamGiamGia);
        gridViewGiamGia.setExpanded(true);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent it = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(it);
                return true;
            } else if (id == R.id.nav_profile) {
                Intent it = new Intent(getApplicationContext(), FragmentProfileActivity.class);
                startActivity(it);
                return true;
            } else if (id == R.id.nav_products) {
                Intent it = new Intent(getApplicationContext(), AllProductsActivity.class);
                startActivity(it);
                return true;
            } else if (id == R.id.nav_order) {
                Intent it = new Intent(getApplicationContext(), OrdersActivity.class);
                startActivity(it);
                return true;
            }
            return false;
        });
    }
}
