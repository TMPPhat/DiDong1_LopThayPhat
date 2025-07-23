package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 5000; // 2 giây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean("first_launch", true);

        if (!isFirstLaunch) {
            // Không phải lần đầu → chuyển thẳng sang HomeActivity
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Lần đầu → hiển thị intro layout
        setContentView(R.layout.activity_intro);

        // Sau 2 giây → chuyển sang HomeActivity
        new Handler().postDelayed(() -> {
            // Đánh dấu không còn là lần đầu
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("first_launch", false);
            editor.apply();

            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            finish();
        }, SPLASH_TIME);
    }
}
