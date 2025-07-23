package com.example.tranminhphat_2123110213;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    // Thông tin sản phẩm
    private String productName;
    private int price;
    private int views;
    private int imageResId;
    private int productId;
    private String textDescription;
    private String brand = "";
    private String category = "";

    private final String BASE_PRODUCT_URL = "https://api.baserow.io/api/database/rows/table/604727/";
    private final String BASE_CART_URL = "https://api.baserow.io/api/database/rows/table/616928/?user_field_names=true";
    private final String AUTH_TOKEN = "Token A1Qn1X9pYbQyNahPGDWHlEdpbMd6qDBj";

    UserManager userManager;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        // Áp dụng padding cho hệ thống thanh điều hướng
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userManager = new UserManager(this);
        currentUser = userManager.getUser();

        // Lấy product_id từ Intent
        productId = getIntent().getIntExtra("product_id", -1);
        if (productId != -1) {
            fetchProductById(productId);
        } else {
            Toast.makeText(this, "Không có ID sản phẩm", Toast.LENGTH_SHORT).show();
        }

        // Chuyển sang giỏ hàng khi bấm nút
        ImageButton btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

        // Xử lý thêm vào giỏ
        LinearLayout layoutAddToCart = findViewById(R.id.layoutAddToCart);
        layoutAddToCart.setOnClickListener(v -> addToCartOnline(productId));

        // Mở rộng GridView sản phẩm liên quan nếu cần
        GridView gridViewLienQuan = findViewById(R.id.SanPhamLienQuan);
        if (gridViewLienQuan instanceof ExpandableHeightGridView) {
            ((ExpandableHeightGridView) gridViewLienQuan).setExpanded(true);
        }
    }

    // Lấy thông tin chi tiết sản phẩm
    private void fetchProductById(int id) {
        String url = BASE_PRODUCT_URL + id + "/?user_field_names=true";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Lấy dữ liệu sản phẩm
                        productName = response.optString("product_name");
                        price = response.optInt("price", 0);
                        views = response.optInt("views", 0);
                        textDescription = response.optString("description", "");
                        brand = response.getJSONArray("brand").getJSONObject(0).optString("value", "");
                        category = response.getJSONArray("category").getJSONObject(0).optString("value", "");

                        laySanPhamLienQuan(brand, category);

                        // Xử lý hình ảnh
                        String imageName = response.optString("image", "").toLowerCase().replace("-", "_");
                        if (imageName.contains(".")) {
                            imageName = imageName.substring(0, imageName.lastIndexOf('.'));
                        }
                        imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());

                        // Gán dữ liệu vào View
                        ((TextView) findViewById(R.id.textProductName)).setText(productName);
                        ((TextView) findViewById(R.id.textDescription)).setText(textDescription);

                        TextView textPrice = findViewById(R.id.textPrice);
                        textPrice.setText(Utils.dinhDangTienVietNam(price));

                        TextView textOldPrice = findViewById(R.id.textOldPrice);
                        textOldPrice.setText(Utils.dinhDangTienVietNam(price + 50000));
                        textOldPrice.setPaintFlags(textOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        ImageView imageProduct = findViewById(R.id.imageProduct);
                        imageProduct.setImageResource(imageResId != 0 ? imageResId : R.drawable.default_image);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý dữ liệu sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Không thể lấy dữ liệu sản phẩm", Toast.LENGTH_LONG).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", AUTH_TOKEN);
                return headers;
            }
        };

        queue.add(request);
    }

    // Lấy danh sách sản phẩm liên quan
    private void laySanPhamLienQuan(String brand, String category) {
        String url = BASE_PRODUCT_URL + "?user_field_names=true";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<Products> danhSachLienQuan = new ArrayList<>();
                        JSONArray results = response.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject item = results.getJSONObject(i);
                            int id = item.getInt("id");
                            if (id == productId) continue;

                            String itemBrand = item.getJSONArray("brand").optJSONObject(0).optString("value", "");
                            String itemCategory = item.getJSONArray("category").optJSONObject(0).optString("value", "");

                            if (itemBrand.equalsIgnoreCase(brand) || itemCategory.equalsIgnoreCase(category)) {
                                String name = item.optString("product_name");
                                int price = item.optInt("price", 0);
                                int views = item.optInt("views", 0);

                                String imageName = item.optString("image", "").toLowerCase().replace("-", "_");
                                if (imageName.contains(".")) {
                                    imageName = imageName.substring(0, imageName.lastIndexOf('.'));
                                }

                                int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                                danhSachLienQuan.add(new Products(id, imageResId, name, price, views, itemBrand, itemCategory));

                                if (danhSachLienQuan.size() >= 6) break;
                            }
                        }

                        GridView gridViewLienQuan = findViewById(R.id.SanPhamLienQuan);
                        gridViewLienQuan.setAdapter(new ProductGridAdapter(this, danhSachLienQuan));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý sản phẩm liên quan", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Không thể lấy sản phẩm liên quan", Toast.LENGTH_LONG).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", AUTH_TOKEN);
                return headers;
            }
        };

        queue.add(request);
    }

    // Thêm sản phẩm vào giỏ hàng (API POST)
    private void addToCartOnline(int productId) {
        String baseUrl = "https://api.baserow.io/api/database/rows/table/616928/?user_field_names=true";

        RequestQueue queue = Volley.newRequestQueue(this);

        ProgressDialog dialog = new ProgressDialog(ProductDetailActivity.this);
        dialog.setMessage("Đang thêm vào giỏ...");
        dialog.setCancelable(false);
        dialog.show();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        boolean productExists = false;

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject item = results.getJSONObject(i);
                            int userId = item.optInt("user_id", -1);
                            int existingProductId = item.optInt("product_id", -1);

                            if (userId == currentUser.getId() && existingProductId == productId) {
                                int rowId = item.getInt("id");
                                int currentQty = item.getInt("qty");
                                updateCartItemQty(queue, rowId, currentQty + 1);
                                productExists = true;
                                break;
                            }
                        }

                        if (!productExists) {
                            JSONObject data = new JSONObject();
                            data.put("product_id", productId);
                            data.put("qty", 1);
                            data.put("user_id", currentUser.getId());

                            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, baseUrl, data,
                                    res -> {
                                        dialog.dismiss();
                                        Toast.makeText(this, "Đã thêm sản phẩm!", Toast.LENGTH_SHORT).show();
                                    },
                                    err -> {
                                        dialog.dismiss();
                                        Toast.makeText(this, "Lỗi thêm sản phẩm!", Toast.LENGTH_SHORT).show();
                                    }) {
                                @Override
                                public Map<String, String> getHeaders() {
                                    Map<String, String> headers = new HashMap<>();
                                    headers.put("Authorization", AUTH_TOKEN);
                                    headers.put("Content-Type", "application/json");
                                    return headers;
                                }
                            };
                            queue.add(postRequest);
                        } else {
                            // Nếu PATCH đã gọi trong updateCartItemQty, nên dismiss ở đó
                            dialog.dismiss();
                            Toast.makeText(this, "Đã cập nhật số lượng!", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        dialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý giỏ hàng!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    dialog.dismiss();
                    error.printStackTrace();
                    Toast.makeText(this, "Lỗi mạng!", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", AUTH_TOKEN);
                return headers;
            }
        };

        queue.add(getRequest);

    }


    private void updateCartItemQty(RequestQueue queue, int rowId, int newQty) {
        String url = "https://api.baserow.io/api/database/rows/table/616928/" + rowId + "/?user_field_names=true";

        try {
            JSONObject data = new JSONObject();
            data.put("qty", newQty);

            JsonObjectRequest patchRequest = new JsonObjectRequest(Request.Method.PATCH, url, data,
                    response -> {
                        Toast.makeText(this, "Đã cập nhật số lượng!", Toast.LENGTH_SHORT).show();
                        Log.d("UpdateCart", "Success PATCH: " + response.toString());
                    },
                    error -> {
                        error.printStackTrace();
                        Toast.makeText(this, "Cập nhật số lượng thất bại!", Toast.LENGTH_SHORT).show();
                        Log.e("UpdateCart", "PATCH error: " + error.toString());
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", AUTH_TOKEN);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            queue.add(patchRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi cập nhật giỏ hàng", Toast.LENGTH_SHORT).show();
        }
    }

}
