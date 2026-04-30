package com.delivery.app.ui.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.delivery.app.api.RetrofitClient;
import com.delivery.app.databinding.FragmentMessagesBinding;
import com.delivery.app.models.Message;
import com.delivery.app.models.User;
import com.delivery.app.ui.controller.adapter.MessageAdapter;
import com.delivery.app.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesFragment extends Fragment {

    private FragmentMessagesBinding binding;
    private SessionManager sessionManager;
    private MessageAdapter adapter;
    private List<User> livreurs = new ArrayList<>();
    private int selectedLivreurId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMessagesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());

        setupRecyclerView();
        loadMessages();
        loadLivreurs();
        setupSendMessage();
    }

    private void setupRecyclerView() {
        adapter = new MessageAdapter(requireContext(), sessionManager.getUser().getId());
        binding.recyclerMessages.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerMessages.setAdapter(adapter);
    }

    private void loadMessages() {
        RetrofitClient.getInstance().getApiService()
                .getMessages(sessionManager.getToken())
                .enqueue(new Callback<List<Message>>() {
                    @Override
                    public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            adapter.setMessages(response.body());
                            binding.recyclerMessages.scrollToPosition(adapter.getItemCount() - 1);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Message>> call, Throwable t) {}
                });
    }

    private void loadLivreurs() {
        RetrofitClient.getInstance().getApiService()
                .getLivreurs(sessionManager.getToken())
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            livreurs = response.body();
                            List<String> names = new ArrayList<>();
                            names.add("Sélectionner livreur");
                            for (User u : livreurs) names.add(u.getFullName());
                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                                    requireContext(),
                                    android.R.layout.simple_spinner_item, names);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spinnerLivreur.setAdapter(spinnerAdapter);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {}
                });
    }

    private void setupSendMessage() {
        binding.btnSend.setOnClickListener(v -> {
            String text = binding.etMessage.getText().toString().trim();
            int pos = binding.spinnerLivreur.getSelectedItemPosition();
            if (pos <= 0) {
                Toast.makeText(requireContext(), "Sélectionnez un livreur", Toast.LENGTH_SHORT).show();
                return;
            }
            if (text.isEmpty()) {
                binding.etMessage.setError("Message vide");
                return;
            }
            User livreur = livreurs.get(pos - 1);

            Map<String, Object> body = new HashMap<>();
            body.put("destinataire_id", livreur.getId());
            body.put("contenu", text);
            body.put("type", "info");

            RetrofitClient.getInstance().getApiService()
                    .sendMessage(sessionManager.getToken(), body)
                    .enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if (response.isSuccessful()) {
                                binding.etMessage.setText("");
                                loadMessages();
                            }
                        }
                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(requireContext(), "Erreur envoi", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Refresh button
        binding.btnRefresh.setOnClickListener(v -> loadMessages());
    }
}
