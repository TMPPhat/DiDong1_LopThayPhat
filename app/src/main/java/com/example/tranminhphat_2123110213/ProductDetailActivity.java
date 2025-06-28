package com.example.tranminhphat_2123110213;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Nhận dữ liệu từ Intent
        String tenSanPham = getIntent().getStringExtra("tenSanPham");
        int imageResId = getIntent().getIntExtra("imageResId", 0);

        // Gán dữ liệu vào view
        ImageView imageView = findViewById(R.id.imageProduct);
        TextView textView = findViewById(R.id.textProductName);

        textView.setText(tenSanPham);
        imageView.setImageResource(imageResId);

        GridView SanPhamLienQuan = findViewById(R.id.SanPhamLienQuan);

        List<Products> danhSach = new ArrayList<>();
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổppppp", 120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ đẹp",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổooooo",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ đẹp",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng không cổ",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ bạc",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng không cổ",120000, 35));
        danhSach.add(new Products(R.drawable.day_chuyen_vang_hong_00a9ab1338, "Vòng cổ bạc",120000, 35));

        ProductGridAdapter adapter = new ProductGridAdapter(this, danhSach);
        SanPhamLienQuan.setAdapter(adapter);


        ExpandableHeightGridView gridViewLienQuan = findViewById(R.id.SanPhamLienQuan);
        gridViewLienQuan.setExpanded(true);


        TextView textOldPrice = findViewById(R.id.textOldPrice);;
        textOldPrice.setPaintFlags(textOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        LinearLayout layoutAddToCart = findViewById(R.id.layoutAddToCart);

        layoutAddToCart.setOnClickListener(v -> {
            // Lấy dữ liệu sản phẩm
            int gia = 120000; // hoặc lấy từ TextView nếu có

            // Tạo sản phẩm và thêm vào giỏ
            Products p = new Products(imageResId, tenSanPham, gia, 35);
            CartManager.addToCart(p);

            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });

    }


}