package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText objEmail, objPwd;
    Button btnLogin, btnRegister;
    RequestQueue mRequestQueue;
    String apiUrl = "https://6868e32ed5933161d70cbd84.mockapi.io/api/users"; // API kiểm tra tài khoản

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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
            Intent it = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(it);
        });
    }

    private void login() {
        String emailInput = objEmail.getText().toString().trim();
        String pwdInput = objPwd.getText().toString().trim();

        if (emailInput.isEmpty() || pwdInput.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.GET, apiUrl,
                response -> {
                    try {
                        JSONArray users = new JSONArray(response);
                        boolean loginSuccess = false;

                        for (int i = 0; i < users.length(); i++) {
                            JSONObject user = users.getJSONObject(i);

                            String email = user.getString("email");
                            String password = user.getString("password");
                            String username = user.getString("username"); // nếu bạn cần dùng sau

                            if (emailInput.equalsIgnoreCase(email) && pwdInput.equals(password)) {
                                loginSuccess = true;
                                break;
                            }
                        }

                        if (loginSuccess) {
                            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(() -> {
                                startActivity(new Intent(this, HomeActivity.class));
                                finish();
                            }, 100);
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
                });

        mRequestQueue.add(request);
    }



}
