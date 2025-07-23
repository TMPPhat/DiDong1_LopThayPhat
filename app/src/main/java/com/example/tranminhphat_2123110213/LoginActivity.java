package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText objEmail, objPwd;
    private Button btnLogin, btnRegister;
    private RequestQueue mRequestQueue;

    private static final String API_URL = "https://api.baserow.io/api/database/rows/table/604717/?user_field_names=true";
    private static final String TOKEN = "Token A1Qn1X9pYbQyNahPGDWHlEdpbMd6qDBj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nếu đã đăng nhập -> chuyển sang Home luôn
        UserManager userManager = new UserManager(this);
        if (userManager.isLoggedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish(); // đóng LoginActivity
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        objEmail = findViewById(R.id.email);
        objPwd = findViewById(R.id.pwd);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        mRequestQueue = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(v -> login());

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String emailInput = objEmail.getText().toString().trim();
        String pwdInput = objPwd.getText().toString().trim();

        if (emailInput.isEmpty() || pwdInput.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URL, null,
                response -> {
                    try {
                        JSONArray users = response.getJSONArray("results");
                        boolean loginSuccess = false;

                        for (int i = 0; i < users.length(); i++) {
                            JSONObject user = users.getJSONObject(i);

                            String email = user.optString("email");
                            String password = user.optString("password");

                            String avatarUrl = "";
                            JSONArray imageArray = user.optJSONArray("image");
                            if (imageArray != null && imageArray.length() > 0) {
                                JSONObject imageObj = imageArray.getJSONObject(0);
                                JSONObject thumbnails = imageObj.getJSONObject("thumbnails");
                                avatarUrl = thumbnails.getJSONObject("small").getString("url");
                            }

                            if (emailInput.equalsIgnoreCase(email) && pwdInput.equals(password)) {
                                loginSuccess = true;

                                User userObj = new User(
                                        user.optInt("id"),
                                        user.optString("name"),
                                        email,
                                        user.optString("phone"),
                                        user.optString("birthday"),
                                        avatarUrl,
                                        user.optString("fullname"),
                                        user.optString("gender"),
                                        user.optString("address")
                                );

                                UserManager userManager = new UserManager(this);
                                userManager.saveUser(userObj);
                                break;
                            }
                        }

                        if (loginSuccess) {
                            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(() -> {
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            }, 500);
                        } else {
                            Toast.makeText(this, "Sai email hoặc mật khẩu", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("LOGIN_API", "Lỗi kết nối: " + error.toString());
                    Toast.makeText(this, "Không kết nối được đến máy chủ", Toast.LENGTH_LONG).show();
                }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", TOKEN);
                return headers;
            }
        };

        mRequestQueue.add(request);
    }
}
