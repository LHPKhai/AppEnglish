package com.example.learningenglishapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learningenglishapp.api.ApiService;
import com.example.learningenglishapp.api.RetrofitClient;
import com.example.learningenglishapp.model.LoginRequest;
import com.example.learningenglishapp.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private CheckBox rememberMeCheckBox;
    private ImageButton showPasswordButton;
    private TextView forgotPasswordText, signUpText;
    private boolean isPasswordVisible = false;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo các view
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.btnLogin);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        showPasswordButton = findViewById(R.id.btn_show_pwd);
        forgotPasswordText = findViewById(R.id.btnForgot_pass);
        signUpText = findViewById(R.id.txt_SignUp);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Kiểm tra Remember Me
        checkRememberMe();

        // Sự kiện nút Show/Hide Password
        showPasswordButton.setOnClickListener(v -> togglePasswordVisibility());

        // Sự kiện nút Login
        loginButton.setOnClickListener(v -> performLogin());

        // Sự kiện Forgot Password
        forgotPasswordText.setOnClickListener(v -> handleForgotPassword());

        // Sự kiện Sign Up
//        signUpText.setOnClickListener(v -> {
//            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
//            startActivity(intent);
//        });
    }

    private void checkRememberMe() {
        boolean remember = sharedPreferences.getBoolean(KEY_REMEMBER, false);
        if (remember) {
            usernameEditText.setText(sharedPreferences.getString(KEY_USERNAME, ""));
            passwordEditText.setText(sharedPreferences.getString(KEY_PASSWORD, ""));
            rememberMeCheckBox.setChecked(true);
        }
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            showPasswordButton.setImageResource(R.drawable.baseline_visibility_off_24);
        } else {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            //showPasswordButton.setImageResource(R.drawable.baseline_visibility_24);
        }
        isPasswordVisible = !isPasswordVisible;
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    private void performLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu thông tin nếu Remember Me được chọn
        if (rememberMeCheckBox.isChecked()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_REMEMBER, true);
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_PASSWORD, password);
            editor.apply();
        } else {
            sharedPreferences.edit().clear().apply();
        }

        // Gửi yêu cầu đăng nhập
        LoginRequest loginRequest = new LoginRequest(username, password);
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<LoginResponse> call = apiService.login(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isResult() && loginResponse.getData() != null && loginResponse.getData().isResult()) {
                        String token = loginResponse.getData().getToken();
                        // Lưu token
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_TOKEN, token);
                        editor.apply();

                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();

                        // Chuyển đến HomeActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: Kiểm tra thông tin", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleForgotPassword() {
        Toast.makeText(this, "Chức năng quên mật khẩu chưa được triển khai", Toast.LENGTH_SHORT).show();
    }
}