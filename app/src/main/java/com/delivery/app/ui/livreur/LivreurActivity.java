package com.delivery.app.ui.livreur;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.delivery.app.R;
import com.delivery.app.databinding.ActivityLivreurBinding;
import com.delivery.app.ui.login.LoginActivity;
import com.delivery.app.utils.SessionManager;

public class LivreurActivity extends AppCompatActivity {

    private ActivityLivreurBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLivreurBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);

        String name = sessionManager.getUser() != null ?
                sessionManager.getUser().getFullName() : "";
        binding.tvWelcome.setText("Bonjour, " + name);

        setupBottomNav();

        if (savedInstanceState == null) {
            loadFragment(new MesLivraisonsFragment());
        }
    }

    private void setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();
            if (id == R.id.nav_mes_livraisons) {
                fragment = new MesLivraisonsFragment();
            } else if (id == R.id.nav_messages_livreur) {
                fragment = new MessagesLivreurFragment();
            } else if (id == R.id.nav_logout) {
                sessionManager.logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            }
            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
