package com.delivery.app.ui.livreur;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.delivery.app.api.RetrofitClient;
import com.delivery.app.databinding.FragmentMessagesLivreurBinding;
import com.delivery.app.models.Message;
import com.delivery.app.ui.controller.adapter.MessageAdapter;
import com.delivery.app.utils.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesLivreurFragment extends Fragment {

    private FragmentMessagesLivreurBinding binding;
    private SessionManager sessionManager;
    private MessageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMessagesLivreurBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());

        adapter = new MessageAdapter(requireContext(), sessionManager.getUser().getId());
        binding.recyclerMessages.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerMessages.setAdapter(adapter);

        loadMessages();

        binding.btnSendUrgence.setOnClickListener(v -> sendUrgentMessage());
        binding.btnRefresh.setOnClickListener(v -> loadMessages());
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

    private void sendUrgentMessage() {
        String text = binding.etMessage.getText().toString().trim();
        String numCde = binding.etNumCommande.getText().toString().trim();

        if (text.isEmpty()) {
            binding.etMessage.setError("Message vide");
            return;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("type", "urgence");
        body.put("contenu", text);
        if (!numCde.isEmpty()) {
            body.put("nocde", Integer.parseInt(numCde));
        }
        // Automatically send to controller - backend handles routing
        body.put("to_controller", true);

        RetrofitClient.getInstance().getApiService()
                .sendMessage(sessionManager.getToken(), body)
                .enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if (response.isSuccessful()) {
                            binding.etMessage.setText("");
                            binding.etNumCommande.setText("");
                            Toast.makeText(requireContext(),
                                    "Message d'urgence envoyé", Toast.LENGTH_SHORT).show();
                            loadMessages();
                        }
                    }
                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(requireContext(), "Erreur envoi", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
