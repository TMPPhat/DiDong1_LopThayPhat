package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class FragmentProfileActivity extends AppCompatActivity {

    private TextView txtUsername, txtEmail;
    private EditText edtName, edtBirthDate, edtPhone, edtEmail;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private ImageView imgAvatar;
    private Button btnSave, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profileLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavHelper.setupBottomNav(this, R.id.nav_profile);

        // Ánh xạ các view
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        edtName = findViewById(R.id.edtName);
        edtBirthDate = findViewById(R.id.edtBirthDate);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);

        // Lấy user hiện tại từ UserManager
        UserManager userManager = new UserManager(this);
        User currentUser = userManager.getUser();


        if (currentUser != null) {
            txtUsername.setText(currentUser.getFullname());
            txtEmail.setText(currentUser.getEmail());
            edtName.setText(currentUser.getFullname());
            edtPhone.setText(currentUser.getPhone());
            edtEmail.setText(currentUser.getEmail());
            edtBirthDate.setText(currentUser.getBirthday());

            // Xử lý giới tính
            if (currentUser.getGender().equalsIgnoreCase("male")) {
                rbMale.setChecked(true);
            } else if (currentUser.getGender().equalsIgnoreCase("female")) {
                rbFemale.setChecked(true);
            }

            // Load ảnh từ URL
            if (currentUser.getImageUrl() != null && !currentUser.getImageUrl().isEmpty()) {
                Glide.with(this)
                        .load(currentUser.getImageUrl())
                        .placeholder(R.drawable.ic_user)
                        .into(imgAvatar);
            }
        }

        // Nút Lưu (chưa xử lý lưu thực tế)
        btnSave.setOnClickListener(v -> {
            Toast.makeText(this, "Đã lưu thông tin (demo)", Toast.LENGTH_SHORT).show();
        });

        // Nút Đăng xuất
        btnLogout.setOnClickListener(v -> {
            UserManager userManager1 = new UserManager(getApplicationContext());
            userManager1.clearUser(); // Xóa thông tin người dùng đã lưu

            Toast.makeText(getApplicationContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            new android.os.Handler().postDelayed(() -> {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa ngăn xếp
                startActivity(intent);
                finish(); // Đóng ProfileActivity
            }, 200);
        });

    }
}
