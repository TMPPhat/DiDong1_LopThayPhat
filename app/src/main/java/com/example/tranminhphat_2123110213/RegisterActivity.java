package com.example.tranminhphat_2123110213;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private static final String API_URL = "https://api.baserow.io/api/database/rows/table/604717/?user_field_names=true";
    private static final String TOKEN = "TOKEN A1Qn1X9pYbQyNahPGDWHlEdpbMd6qDBj"; // 🔒 Thay bằng token của bạn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText fullName = findViewById(R.id.editTextFullName);
        EditText email = findViewById(R.id.editTextEmail);
        EditText password = findViewById(R.id.editTextPassword);
        EditText confirmPassword = findViewById(R.id.editTextConfirmPassword);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView textBackToLogin = findViewById(R.id.textBackToLogin);

        textBackToLogin.setOnClickListener(v -> finish());

        btnRegister.setOnClickListener(v -> {
            String name = fullName.getText().toString().trim();
            String mail = email.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String rePass = confirmPassword.getText().toString().trim();

            if (name.isEmpty() || mail.isEmpty() || pass.isEmpty() || rePass.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(rePass)) {
                Toast.makeText(this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pass.length() < 6) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Gửi dữ liệu lên API Baserow
            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("fullname", name);
                jsonBody.put("email", mail);
                jsonBody.put("password", pass);

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        API_URL,
                        jsonBody,
                        response -> {
                            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            new android.os.Handler().postDelayed(() -> {
                                startActivity(new Intent(this, LoginActivity.class));
                                finish();
                            }, 100);
                        },
                        error -> {
                            Toast.makeText(this, "Lỗi đăng ký: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                ) {
                    @Override
                    public java.util.Map<String, String> getHeaders() {
                        java.util.Map<String, String> headers = new java.util.HashMap<>();
                        headers.put("Authorization", TOKEN);
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(request);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi định dạng JSON", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
