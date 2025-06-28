package com.example.tranminhphat_2123110213;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BottomNavHelper {

    public static void setupBottomNav(Activity activity, int selectedNavId) {
        // Khai báo các LinearLayout nav
        LinearLayout navHome = activity.findViewById(R.id.nav_home);
        LinearLayout navOrder = activity.findViewById(R.id.nav_order);
        LinearLayout navProducts = activity.findViewById(R.id.nav_products);
        LinearLayout navProfile = activity.findViewById(R.id.nav_profile);

        // Reset màu tất cả icon + text
        resetTabColors(activity);

        // Set màu cam cho tab đang chọn
        highlightSelectedTab(activity, selectedNavId);

        navHome.setOnClickListener(v -> {
            if (selectedNavId != R.id.nav_home)
                activity.startActivity(new Intent(activity, HomeActivity.class));
        });

        navOrder.setOnClickListener(v -> {
            if (selectedNavId != R.id.nav_order)
                activity.startActivity(new Intent(activity, OrdersActivity.class));
        });

        navProducts.setOnClickListener(v -> {
            if (selectedNavId != R.id.nav_products)
                activity.startActivity(new Intent(activity, AllProductsActivity.class));
        });

        navProfile.setOnClickListener(v -> {
            if (selectedNavId != R.id.nav_profile)
                activity.startActivity(new Intent(activity, FragmentProfileActivity.class));
        });
    }

    private static void resetTabColors(Activity activity) {
        int defaultColor = Color.parseColor("#888888");

        int[] iconIds = {
                R.id.icon_home, R.id.icon_order, R.id.icon_products, R.id.icon_profile, R.id.icon_chat
        };
        int[] textIds = {
                R.id.text_home, R.id.text_order, R.id.text_products, R.id.text_profile, R.id.text_chat
        };

        for (int i = 0; i < iconIds.length; i++) {
            ImageView icon = activity.findViewById(iconIds[i]);
            TextView text = activity.findViewById(textIds[i]);
            if (icon != null) icon.setColorFilter(defaultColor);
            if (text != null) text.setTextColor(defaultColor);
        }
    }

    private static void highlightSelectedTab(Activity activity, int selectedNavId) {
        int highlightColor = Color.parseColor("#F4511E");

        int iconId = 0, textId = 0;

        if (selectedNavId == R.id.nav_home) {
            iconId = R.id.icon_home;
            textId = R.id.text_home;
        } else if (selectedNavId == R.id.nav_order) {
            iconId = R.id.icon_order;
            textId = R.id.text_order;
        } else if (selectedNavId == R.id.nav_products) {
            iconId = R.id.icon_products;
            textId = R.id.text_products;
        } else if (selectedNavId == R.id.nav_profile) {
            iconId = R.id.icon_profile;
            textId = R.id.text_profile;
        }

        ImageView icon = activity.findViewById(iconId);
        TextView text = activity.findViewById(textId);

        if (icon != null) icon.setColorFilter(highlightColor);
        if (text != null) text.setTextColor(highlightColor);
    }

}
