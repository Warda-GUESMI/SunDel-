package com.delivery.app.ui.controller;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.delivery.app.databinding.FragmentLivraisonsControllerBinding;
import com.delivery.app.models.Livraison;
import com.delivery.app.ui.controller.adapter.LivraisonControllerAdapter;
import com.delivery.app.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivraisonsFragment extends Fragment {

    private FragmentLivraisonsControllerBinding binding;
    private SessionManager sessionManager;
    private LivraisonControllerAdapter adapter;
    private String dateDebut, dateFin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLivraisonsControllerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());

        setupRecyclerView();
        setupFilters();
        loadLivraisons(null, null, null, null, null, null);
    }

    private void setupRecyclerView() {
        adapter = new LivraisonControllerAdapter(requireContext());
        binding.recyclerLivraisons.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerLivraisons.setAdapter(adapter);
    }

    private void setupFilters() {
        // Date pickers
        binding.etDateDebut.setOnClickListener(v -> showDatePicker(true));
        binding.etDateFin.setOnClickListener(v -> showDatePicker(false));

        // Search button
        binding.btnSearch.setOnClickListener(v -> applyFilters());

        // Reset
        binding.btnReset.setOnClickListener(v -> {
            binding.etDateDebut.setText("");
            binding.etDateFin.setText("");
            binding.etSearchClient.setText("");
            binding.etSearchCommande.setText("");
            dateDebut = null;
            dateFin = null;
            loadLivraisons(null, null, null, null, null, null);
        });

        // Etat spinner
        ArrayAdapter<CharSequence> etatAdapter = ArrayAdapter.createFromResource(
                requireContext(), com.delivery.app.R.array.etats_livraison,
                android.R.layout.simple_spinner_item);
        etatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerEtat.setAdapter(etatAdapter);
    }

    private void showDatePicker(boolean isDebut) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(requireContext(),
                (datePicker, year, month, day) -> {
                    String date = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                            year, month + 1, day);
                    String display = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                            day, month + 1, year);
                    if (isDebut) {
                        dateDebut = date;
                        binding.etDateDebut.setText(display);
                    } else {
                        dateFin = date;
                        binding.etDateFin.setText(display);
                    }
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void applyFilters() {
        String etat = binding.spinnerEtat.getSelectedItemPosition() > 0 ?
                binding.spinnerEtat.getSelectedItem().toString() : null;
        String client = binding.etSearchClient.getText().toString().trim();
        String commandeStr = binding.etSearchCommande.getText().toString().trim();
        Integer commande = commandeStr.isEmpty() ? null : Integer.parseInt(commandeStr);

        loadLivraisons(dateDebut, dateFin, etat, null,
                client.isEmpty() ? null : client, commande);
    }

    private void loadLivraisons(String dateDebut, String dateFin, String etat,
                                 Integer livreurId, String clientNom, Integer numCommande) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerLivraisons.setVisibility(View.GONE);

        RetrofitClient.getInstance().getApiService()
                .getAllLivraisons(sessionManager.getToken(), dateDebut, dateFin,
                        etat, livreurId, clientNom, numCommande)
                .enqueue(new Callback<List<Livraison>>() {
                    @Override
                    public void onResponse(Call<List<Livraison>> call, Response<List<Livraison>> response) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.recyclerLivraisons.setVisibility(View.VISIBLE);
                        if (response.isSuccessful() && response.body() != null) {
                            List<Livraison> livraisons = response.body();
                            adapter.setLivraisons(livraisons);
                            binding.tvCount.setText(livraisons.size() + " livraison(s)");
                        } else {
                            Toast.makeText(requireContext(), "Erreur chargement", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Livraison>> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
