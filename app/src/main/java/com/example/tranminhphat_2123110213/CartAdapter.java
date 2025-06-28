package com.example.tranminhphat_2123110213;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    public Object getItem(int i) {
        return productList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);

        ImageView image = view.findViewById(R.id.imageCartProduct);
        TextView name = view.findViewById(R.id.textCartName);
        CheckBox checkbox = view.findViewById(R.id.checkboxItem); // DÒNG QUAN TRỌNG

        Products product = productList.get(i);
        image.setImageResource(product.imageResId);
        name.setText(product.tenSanPham);

        checkbox.setChecked(product.isChecked);
        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            product.isChecked = isChecked;
        });

        return view;
    }

    // Lấy danh sách sản phẩm đã chọn
    public List<Products> getSelectedItems() {
        List<Products> selected = new ArrayList<>();
        for (Products p : productList) {
            if (p.isChecked) selected.add(p);
        }
        return selected;
    }

    // Xóa những sản phẩm được chọn
    public void removeItems(List<Products> toRemove) {
        productList.removeAll(toRemove);
        notifyDataSetChanged();
    }
}
