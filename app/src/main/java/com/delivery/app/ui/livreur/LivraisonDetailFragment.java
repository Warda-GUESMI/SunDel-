package com.delivery.app.ui.livreur;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.delivery.app.api.RetrofitClient;
import com.delivery.app.databinding.FragmentLivraisonDetailBinding;
import com.delivery.app.models.Livraison;
import com.delivery.app.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivraisonDetailFragment extends Fragment {

    private static final String ARG_NUM_COMMANDE = "num_commande";
    private FragmentLivraisonDetailBinding binding;
    private SessionManager sessionManager;
    private Livraison livraison;

    public static LivraisonDetailFragment newInstance(int numCommande) {
        LivraisonDetailFragment fragment = new LivraisonDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUM_COMMANDE, numCommande);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLivraisonDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());

        int numCommande = getArguments() != null ? getArguments().getInt(ARG_NUM_COMMANDE) : -1;
        if (numCommande > 0) {
            loadDetail(numCommande);
        }

        // Setup etat spinner
        String[] etats = {"en_cours", "livree", "echouee"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, etats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerEtat.setAdapter(adapter);

        binding.btnSave.setOnClickListener(v -> updateLivraison());
        binding.btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void loadDetail(int numCommande) {
        binding.progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getApiService()
                .getLivraisonDetail(sessionManager.getToken(), numCommande)
                .enqueue(new Callback<Livraison>() {
                    @Override
                    public void onResponse(Call<Livraison> call, Response<Livraison> response) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            livraison = response.body();
                            displayDetail();
                        }
                    }
                    @Override
                    public void onFailure(Call<Livraison> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void displayDetail() {
        binding.tvTitreCommande.setText("Commande #" + livraison.getNumCommande());
        binding.tvClientNom.setText(livraison.getClientFullName());
        binding.tvClientTel.setText(livraison.getClientTel());
        binding.tvAdresse.setText(livraison.getAdresseComplete());
        binding.tvNbArticles.setText(livraison.getNbArticles() + " article(s)");
        binding.tvMontant.setText(String.format("%.2f TND", livraison.getMontantTotal()));
        binding.tvModePaiement.setText(livraison.getModePaiement());
        binding.etRemarques.setText(livraison.getRemarques());

        // Set current etat in spinner
        String[] etats = {"en_cours", "livree", "echouee"};
        for (int i = 0; i < etats.length; i++) {
            if (etats[i].equals(livraison.getEtatLivraison())) {
                binding.spinnerEtat.setSelection(i);
                break;
            }
        }

        // Phone click
        binding.tvClientTel.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + livraison.getClientTel()));
            startActivity(intent);
        });

        // Maps click
        binding.btnMaps.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(livraison.getGoogleMapsUrl()));
            startActivity(intent);
        });
    }

    private void updateLivraison() {
        String newEtat = binding.spinnerEtat.getSelectedItem().toString();
        String remarques = binding.etRemarques.getText().toString().trim();

        Map<String, String> body = new HashMap<>();
        body.put("etatliv", newEtat);
        body.put("remarques", remarques);

        RetrofitClient.getInstance().getApiService()
                .updateLivraison(sessionManager.getToken(), livraison.getNumCommande(), body)
                .enqueue(new Callback<Livraison>() {
                    @Override
                    public void onResponse(Call<Livraison> call, Response<Livraison> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(), "Livraison mise à jour", Toast.LENGTH_SHORT).show();
                            requireActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(requireContext(), "Erreur mise à jour", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Livraison> call, Throwable t) {
                        Toast.makeText(requireContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
