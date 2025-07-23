package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private CartAdapter adapter;
    private ListView listView;
    private List<Products> gioHang = new ArrayList<>();
    private List<Products> allProducts = ProductRepository.getProductList();

    private CheckBox checkboxAll;
    private TextView textDelete;
    private TextView txtQty;
    private TextView txtTotal;
    private Button btnPayment;
    private ImageButton btnBack;

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

        listView = findViewById(R.id.listViewCart);
        checkboxAll = findViewById(R.id.checkboxAll);
        textDelete = findViewById(R.id.textDelete);
        btnPayment = findViewById(R.id.btnPayment);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // Nút thanh toán
        btnPayment.setOnClickListener(v -> {
            if (adapter == null) {
                Toast.makeText(this, "Chưa có dữ liệu giỏ hàng", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Products> selectedItems = adapter.getSelectedItems();
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ít nhất một sản phẩm để thanh toán", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                intent.putExtra("selected_items", new ArrayList<>(selectedItems));
                startActivity(intent);
            }
        });

        // Xử lý checkbox chọn tất cả
        checkboxAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (adapter != null) {
                adapter.selectAll(isChecked);
            }
        });

        // Xóa sản phẩm đã chọn
        textDelete.setOnClickListener(v -> {
            if (adapter == null) return;

            List<Products> selectedItems = adapter.getSelectedItems();
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Bạn chưa chọn sản phẩm nào để xóa", Toast.LENGTH_SHORT).show();
            } else {
                for (Products p : selectedItems) {
                    deleteCartItemFromBaserow(p.getCartRowId()); // Nếu bạn có trường rowId trong bảng cart
                }
                adapter.removeItems(selectedItems);
                Toast.makeText(this, "Đã xóa sản phẩm đã chọn", Toast.LENGTH_SHORT).show();
            }
        });

        loadCartFromBaserow();


    }

    // ✅ Load giỏ hàng từ Baserow
    private void loadCartFromBaserow() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.baserow.io/api/database/rows/table/616928/?user_field_names=true";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        List<Products> cartItems = new ArrayList<>();

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject row = results.getJSONObject(i);
                            int productId = row.getInt("product_id");
                            int qty = row.getInt("qty");
                            int rowId = row.getInt("id");

                            for (Products p : allProducts) {
                                if (p.getId() == productId) {
                                    Products copy = new Products(p); // Clone đối tượng nếu cần
                                    copy.setQuantity(qty);
                                    copy.setChecked(false);
                                    copy.setCartRowId(rowId); // Cần có trường rowId để xoá
                                    cartItems.add(copy);

                                    break;
                                }
                            }
                        }

                        adapter = new CartAdapter(CartActivity.this, cartItems);
                        listView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Lỗi tải giỏ hàng", Toast.LENGTH_SHORT).show();
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

    // 🗑️ Hàm xoá sản phẩm khỏi Baserow
    private void deleteCartItemFromBaserow(int itemId) {
        String url = "https://api.baserow.io/api/database/rows/table/616928/" + itemId + "/?user_field_names=true";

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> Log.d("DELETE", "Xóa thành công"),
                error -> Log.e("DELETE", "Lỗi xóa giỏ hàng: " + error.getMessage())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token A1Qn1X9pYbQyNahPGDWHlEdpbMd6qDBj");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
