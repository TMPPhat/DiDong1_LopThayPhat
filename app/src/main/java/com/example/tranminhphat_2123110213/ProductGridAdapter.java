package com.example.tranminhphat_2123110213;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_products, parent, false);

        // View binding
        ImageView img = convertView.findViewById(R.id.imageViewSanPham);
        TextView txtTen = convertView.findViewById(R.id.textViewTen);
        TextView txtGia = convertView.findViewById(R.id.textViewGia);
        TextView txtDaXem = convertView.findViewById(R.id.textViewDaBan);
        CardView cardView = convertView.findViewById(R.id.cardViewSanPham);

        // Gán dữ liệu
        Products product = products.get(position);

        // ✅ Load ảnh từ resource nội bộ
        img.setImageResource(product.getImageResId());

        txtTen.setText(product.getProduct_name());
        txtGia.setText("Giá: " + Utils.dinhDangTienVietNam(product.getPrice()));
        txtDaXem.setText("Lượt xem: " + product.getViews());

        // ✅ Khi bấm vào sản phẩm
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product_id", product.getId());
            intent.putExtra("tenSanPham", product.getProduct_name());
            intent.putExtra("imageResId", product.getImageResId());  // ✅ Dùng imageResId
            intent.putExtra("gia", product.getPrice());
            intent.putExtra("views", product.getViews());
            intent.putExtra("brand", product.getBrand());
            context.startActivity(intent);
        });

        return convertView;
    }
}
