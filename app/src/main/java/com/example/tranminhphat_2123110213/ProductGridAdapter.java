package com.example.tranminhphat_2123110213;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        ImageView img = convertView.findViewById(R.id.imageViewSanPham);
        TextView txt = convertView.findViewById(R.id.textViewTen);

        Products product = products.get(i);
        img.setImageResource(product.imageResId);
        txt.setText(product.tenSanPham);

        return convertView;
    }
}
