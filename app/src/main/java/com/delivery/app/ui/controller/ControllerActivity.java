package com.delivery.app.ui.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.delivery.app.R;
import com.delivery.app.databinding.ActivityControllerBinding;
import com.delivery.app.ui.login.LoginActivity;
import com.delivery.app.utils.SessionManager;
import com.google.android.material.navigation.NavigationBarView;

public class ControllerActivity extends AppCompatActivity {

    private ActivityControllerBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityControllerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);

        // Set welcome text
        String name = sessionManager.getUser() != null ?
                sessionManager.getUser().getFullName() : "";
        binding.tvWelcome.setText("Bonjour, " + name);

        setupBottomNav();

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new LivraisonsFragment());
        }
    }

    private void setupBottomNav() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();
            if (id == R.id.nav_livraisons) {
                fragment = new LivraisonsFragment();
            } else if (id == R.id.nav_dashboard) {
                fragment = new DashboardFragment();
            } else if (id == R.id.nav_messages) {
                fragment = new MessagesFragment();
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
