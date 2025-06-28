package com.example.tranminhphat_2123110213;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;

public class ProductGridAdapter extends BaseAdapter {
    private final Context context;
    private final List<Products> products;

    public ProductGridAdapter(Context context, List<Products> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_products, parent, false);

        // View binding
        ImageView img = convertView.findViewById(R.id.imageViewSanPham);
        TextView txtTen = convertView.findViewById(R.id.textViewTen);
        TextView txtGia = convertView.findViewById(R.id.textViewGia);
        TextView txtDaBan = convertView.findViewById(R.id.textViewDaBan);
        CardView cardView = convertView.findViewById(R.id.cardViewSanPham);

        // Gán dữ liệu
        Products product = products.get(i);
        img.setImageResource(product.imageResId);
        txtTen.setText(product.tenSanPham);
        txtGia.setText("Giá: " + product.gia + "₫");
        txtDaBan.setText("Đã bán: " + product.daBan);

        // Click ở toàn bộ item
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("tenSanPham", product.tenSanPham);
                intent.putExtra("imageResId", product.imageResId);
                intent.putExtra("gia", product.gia);
                intent.putExtra("daBan", product.daBan);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

}
