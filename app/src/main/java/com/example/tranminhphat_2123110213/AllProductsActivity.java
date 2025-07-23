package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class AllProductsActivity extends AppCompatActivity {

    private List<Products> danhSach;          // Danh sách gốc
    private List<Products> danhSachLoc;       // Danh sách sau lọc
    private ProductGridAdapter adapter;
    private GridView productsGrid;
    private EditText edtSearch;

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
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

        edtSearch = findViewById(R.id.edtSearch);
        productsGrid = findViewById(R.id.products);

        danhSach = new ArrayList<>();
        danhSachLoc = new ArrayList<>();
        adapter = new ProductGridAdapter(this, danhSachLoc);
        productsGrid.setAdapter(adapter);

        fetchProductsFromBaserow();
        BottomNavHelper.setupBottomNav(this, R.id.nav_products);

        LinearLayout layoutFilter = findViewById(R.id.LayoutFilter);
        layoutFilter.setOnClickListener(v -> showFilterBottomSheet());

        productsGrid.setOnItemClickListener((parent, view, position, id) -> {
            Products selected = danhSachLoc.get(position);
            Intent intent = new Intent(AllProductsActivity.this, ProductDetailActivity.class);
            intent.putExtra("product_id", selected.getId());
            startActivity(intent);
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}   

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }
        });
    }

    private void fetchProductsFromBaserow() {
        String apiUrl = "https://api.baserow.io/api/database/rows/table/604727/?user_field_names=true";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        danhSach.clear();

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject item = results.getJSONObject(i);
                            int id = item.optInt("id");
                            String name = item.optString("product_name");
                            int price = item.optInt("price", 0);
                            int views = item.optInt("views", 0);
                            String brand = item.optString("brand");
                            String categoryName = item.optString("category");

                            JSONArray categoryIdArray = item.optJSONArray("category_id");
                            int categoryId = -1;
                            if (categoryIdArray != null && categoryIdArray.length() > 0) {
                                categoryId = categoryIdArray.getInt(0); // lấy ID đầu tiên
                            }

                            String imageName = item.optString("image");
                            if (imageName.contains(".")) {
                                imageName = imageName.substring(0, imageName.lastIndexOf('.'));
                            }
                            imageName = imageName.toLowerCase().replace("-", "_");
                            int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                            if (imageResId == 0) {
                                imageResId = R.drawable.default_image;
                            }

                            Products product = new Products(id, imageResId, name, price, views, brand, categoryName);
                            product.setCategoryId(categoryId); // thêm ID danh mục vào sản phẩm
                            danhSach.add(product);
                        }

                        ProductRepository.setProductList(danhSach);
                        danhSachLoc.clear();
                        danhSachLoc.addAll(danhSach);
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Không thể kết nối tới API", Toast.LENGTH_LONG).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token A1Qn1X9pYbQyNahPGDWHlEdpbMd6qDBj");
                return headers;
            }
        };

        queue.add(request);
    }

    private void filterProducts(String keyword) {
        danhSachLoc.clear();
        keyword = keyword.toLowerCase();

        for (Products p : danhSach) {
            if (p.getProduct_name().toLowerCase().contains(keyword) ||
                    p.getBrand().toLowerCase().contains(keyword) ||
                    p.getCategory().toLowerCase().contains(keyword)) {
                danhSachLoc.add(p);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void showFilterBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = LayoutInflater.from(this).inflate(R.layout.activity_filter, null);
        bottomSheetDialog.setContentView(sheetView);

        LinearLayout checkboxContainer = sheetView.findViewById(R.id.categoryCheckboxContainer);
        Button btnApply = sheetView.findViewById(R.id.btnApplyFilter);

        List<CheckBox> checkBoxList = new ArrayList<>();
        List<Integer> categoryIdList = new ArrayList<>();

        String apiCategoryUrl = "https://api.baserow.io/api/database/rows/table/604728/?user_field_names=true";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest categoryRequest = new JsonObjectRequest(Request.Method.GET, apiCategoryUrl, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject item = results.getJSONObject(i);
                            int categoryID = item.optInt("id");
                            String categoryName = item.optString("category_name");

                            CheckBox checkBox = new CheckBox(this);
                            checkBox.setText(categoryName);
                            checkBox.setTextSize(16f);
                            checkboxContainer.addView(checkBox);

                            checkBoxList.add(checkBox);
                            categoryIdList.add(categoryID);
                        }

                        btnApply.setOnClickListener(view -> {
                            List<Integer> selectedCategoryIds = new ArrayList<>();
                            for (int i = 0; i < checkBoxList.size(); i++) {
                                if (checkBoxList.get(i).isChecked()) {
                                    selectedCategoryIds.add(categoryIdList.get(i));
                                }
                            }

                            danhSachLoc.clear();
                            for (Products p : danhSach) {
                                if (selectedCategoryIds.contains(p.getCategoryId())) {
                                    danhSachLoc.add(p);
                                }
                            }

                            adapter.notifyDataSetChanged();
                            bottomSheetDialog.dismiss();
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Lỗi tải danh mục", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token A1Qn1X9pYbQyNahPGDWHlEdpbMd6qDBj");
                return headers;
            }
        };

        queue.add(categoryRequest);
        bottomSheetDialog.show();
    }
}
