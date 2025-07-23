package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private List<Products> danhSach;
    private ExpandableHeightGridView gridNoiBat, gridGiamGia;
    private final String apiUrl = "https://api.baserow.io/api/database/rows/table/604727/?user_field_names=true";

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

        ViewFlipper bannerFlipper = findViewById(R.id.bannerFlipper);
        int[] banners = { R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4 };
        for (int banner : banners) {
            ImageView img = new ImageView(this);
            img.setImageResource(banner);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            bannerFlipper.addView(img);
        }

        ImageButton btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

        gridNoiBat = findViewById(R.id.SanPhamNoiBat);
        gridGiamGia = findViewById(R.id.SanPhamGiamGia);
        gridNoiBat.setExpanded(true);
        gridGiamGia.setExpanded(true);

        loadDataFromApi();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
            if (id == R.id.nav_profile) startActivity(new Intent(this, FragmentProfileActivity.class));
            else if (id == R.id.nav_products) startActivity(new Intent(this, AllProductsActivity.class));
            else if (id == R.id.nav_order) startActivity(new Intent(this, OrdersActivity.class));
            return true;
        });
    }

    private void loadDataFromApi() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    List<Products> noiBatList = new ArrayList<>();
                    List<Products> giamGiaList = new ArrayList<>();
                    danhSach = new ArrayList<>();

                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject obj = results.getJSONObject(i);

                            int id = obj.optInt("id");
                            String name = obj.optString("product_name");
                            int price = obj.optInt("price", 0);
                            int views = obj.optInt("views", 0);
                            boolean isOnSale = obj.optBoolean("is_on_sale", false);
                            String brand = obj.optString("brand");
                            String category = obj.optString("category");

                            // ✅ Lấy tên ảnh
                            String imageName = "";
                            if (obj.has("image") && !obj.isNull("image")) {
                                Object imageObj = obj.opt("image");

                                if (imageObj instanceof JSONArray) {
                                    JSONArray images = (JSONArray) imageObj;
                                    if (images.length() > 0) {
                                        JSONObject imageFile = images.getJSONObject(0);
                                        imageName = imageFile.optString("name", "");
                                    }
                                } else if (imageObj instanceof String) {
                                    imageName = (String) imageObj;
                                }
                            }

                            // ✅ Chuẩn hóa tên ảnh: bỏ đuôi, viết thường, thay - thành _
                            if (imageName.contains(".")) {
                                imageName = imageName.substring(0, imageName.lastIndexOf('.'));
                            }
                            imageName = imageName.toLowerCase().replace("-", "_");

                            // ✅ Tìm ID ảnh trong drawable
                            int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                            if (imageResId == 0) {
                                imageResId = R.drawable.default_image;
                            }

                            Products p = new Products(id, imageResId, name, price, views, brand, category);
                            danhSach.add(new Products(id, imageResId, name, price, views, brand, category));
                            if (isOnSale) {
                                giamGiaList.add(p);
                            } else if (views >= 20) {
                                noiBatList.add(p);
                            }
                        }

                        // ✅ Giới hạn 8 sản phẩm
                        int limit = 8;
                        List<Products> limitedNoiBat = noiBatList.size() > limit ? noiBatList.subList(0, limit) : noiBatList;
                        List<Products> limitedGiamGia = giamGiaList.size() > limit ? giamGiaList.subList(0, limit) : giamGiaList;

                        // ✅ Gán adapter
                        gridNoiBat.setAdapter(new ProductGridAdapter(this, limitedNoiBat));
                        gridGiamGia.setAdapter(new ProductGridAdapter(this, limitedGiamGia));

                        // ✅ Gộp dữ liệu để lưu trữ toàn bộ sản phẩm

                        ProductRepository.setProductList(danhSach);

                    } catch (JSONException e) {
                        Toast.makeText(this, "Lỗi phân tích JSON", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Không thể tải dữ liệu sản phẩm", Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", "Volley error: " + error.getMessage(), error);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token A1Qn1X9pYbQyNahPGDWHlEdpbMd6qDBj");
                return headers;
            }
        };

        queue.add(request);
    }
}
