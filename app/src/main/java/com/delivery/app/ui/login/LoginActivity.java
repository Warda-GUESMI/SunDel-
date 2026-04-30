package com.delivery.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.delivery.app.api.RetrofitClient;
import com.delivery.app.databinding.ActivityLoginBinding;
import com.delivery.app.models.User;
import com.delivery.app.ui.controller.ControllerActivity;
import com.delivery.app.ui.livreur.LivreurActivity;
import com.delivery.app.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        // Auto-redirect if already logged in
        if (sessionManager.isLoggedIn()) {
            redirectByRole();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String login = binding.etLogin.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (login.isEmpty()) {
            binding.etLogin.setError("Champ requis");
            binding.etLogin.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            binding.etPassword.setError("Champ requis");
            binding.etPassword.requestFocus();
            return;
        }

        setLoading(true);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("login", login);
        credentials.put("password", password);

        RetrofitClient.getInstance().getApiService()
                .login(credentials)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        setLoading(false);
                        if (response.isSuccessful() && response.body() != null) {
                            User user = response.body();
                            sessionManager.saveUser(user);
                            redirectByRole();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Identifiants incorrects", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        setLoading(false);
                        Toast.makeText(LoginActivity.this,
                                "Erreur de connexion: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void redirectByRole() {
        Intent intent;
        if (sessionManager.isControleur()) {
            intent = new Intent(this, ControllerActivity.class);
        } else {
            intent = new Intent(this, LivreurActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoading(boolean loading) {
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!loading);
        binding.etLogin.setEnabled(!loading);
        binding.etPassword.setEnabled(!loading);
    }
}
