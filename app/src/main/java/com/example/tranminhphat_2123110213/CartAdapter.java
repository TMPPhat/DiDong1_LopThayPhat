package com.example.tranminhphat_2123110213;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends BaseAdapter {
    private final Context context;
    private final List<Products> productList;

    public CartAdapter(Context context, List<Products> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);

        // Liên kết View
        ImageView image = view.findViewById(R.id.imageCartProduct);
        TextView name = view.findViewById(R.id.textCartName);
        TextView brand = view.findViewById(R.id.textCartOption);
        TextView price = view.findViewById(R.id.textCartPrice);
        EditText quantity = view.findViewById(R.id.editTextQty);
        MaterialButton btnMinus = view.findViewById(R.id.btnMinus);
        MaterialButton btnPlus = view.findViewById(R.id.btnPlus);
        CheckBox checkbox = view.findViewById(R.id.checkboxItem);

        Products product = productList.get(position);

        // Load ảnh sản phẩm
        int imageResId = product.getImageResId();
        if (imageResId != 0) {
            image.setImageResource(imageResId);
        } else {
            image.setImageResource(R.drawable.default_image); // ảnh mặc định nếu không có
        }

        name.setText(product.getProduct_name());

        // Gán tên thương hiệu
        try {
            String brandName = "Không rõ";
            JSONArray brandArray = new JSONArray(product.getBrand());
            if (brandArray.length() > 0) {
                JSONObject brandObj = brandArray.getJSONObject(0);
                brandName = brandObj.optString("value", "Không rõ");
            }
            brand.setText(brandName);
        } catch (Exception e) {
            brand.setText("Không rõ");
            e.printStackTrace();
        }

        // Gán giá
        price.setText(Utils.dinhDangTienVietNam(product.getPrice()));
        quantity.setText(String.valueOf(product.getQuantity()));

        // Gán trạng thái checkbox
        checkbox.setChecked(product.isChecked());
        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.setChecked(isChecked);
        });

        // Nút tăng
        btnPlus.setOnClickListener(v -> {
            int qty = product.getQuantity() + 1;
            product.setQuantity(qty);
            quantity.setText(String.valueOf(qty));
        });

        // Nút giảm
        btnMinus.setOnClickListener(v -> {
            int qty = product.getQuantity();
            if (qty > 1) {
                qty--;
                product.setQuantity(qty);
                quantity.setText(String.valueOf(qty));
            }
        });

        // Xử lý thay đổi số lượng thủ công
        quantity.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int qty = Integer.parseInt(s.toString());
                    if (qty > 0) {
                        product.setQuantity(qty);
                    }
                } catch (NumberFormatException ignored) {}
            }
        });

        return view;
    }

    public void selectAll(boolean isSelected) {
        for (Products product : productList) {
            product.setChecked(isSelected);
        }
        notifyDataSetChanged();
    }

    // Lấy danh sách sản phẩm được chọn
    public List<Products> getSelectedItems() {
        List<Products> selected = new ArrayList<>();
        for (Products p : productList) {
            if (p.isChecked()) selected.add(p);
        }
        return selected;
    }

    // Xoá sản phẩm đã chọn
    public void removeItems(List<Products> toRemove) {
        productList.removeAll(toRemove);
        notifyDataSetChanged();
    }
}
