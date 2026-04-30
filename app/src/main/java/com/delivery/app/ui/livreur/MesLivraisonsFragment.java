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
import com.delivery.app.database.DatabaseHelper;
import com.delivery.app.databinding.FragmentMesLivraisonsBinding;
import com.delivery.app.models.Livraison;
import com.delivery.app.ui.livreur.adapter.LivraisonLivreurAdapter;
import com.delivery.app.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesLivraisonsFragment extends Fragment {

    private FragmentMesLivraisonsBinding binding;
    private SessionManager sessionManager;
    private LivraisonLivreurAdapter adapter;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMesLivraisonsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        dbHelper = new DatabaseHelper(requireContext());

        String today = new SimpleDateFormat("dd/MM/yyyy",
                Locale.getDefault()).format(new Date());
        binding.tvDate.setText("Livraisons du " + today);

        setupRecyclerView();

        // Premier chargement : sync depuis le serveur
        syncFromServer();

        // Pull-to-refresh = re-sync depuis le serveur
        binding.swipeRefresh.setOnRefreshListener(this::syncFromServer);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFromSQLite();
    }

    private void setupRecyclerView() {
        adapter = new LivraisonLivreurAdapter(requireContext());
        adapter.setOnItemClickListener(livraison -> {
            LivraisonDetailFragment detailFragment =
                    LivraisonDetailFragment.newInstance(livraison.getNumCommande());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(com.delivery.app.R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
        binding.recyclerLivraisons.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        binding.recyclerLivraisons.setAdapter(adapter);
    }

    // Sync depuis le serveur → sauvegarde SQLite → affiche trié par zone
    private void syncFromServer() {
        binding.progressBar.setVisibility(View.VISIBLE);
        int livreurId = sessionManager.getUser().getId();
        String today = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());

        RetrofitClient.getInstance().getApiService()
                .getLivraisonsLivreurToday(sessionManager.getToken(), livreurId)
                .enqueue(new Callback<List<Livraison>>() {
                    @Override
                    public void onResponse(Call<List<Livraison>> call,
                                           Response<List<Livraison>> response) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);

                        if (response.isSuccessful() && response.body() != null) {
                            // Sauvegarde dans SQLite
                            dbHelper.clearAll();
                            for (Livraison l : response.body()) {
                                dbHelper.saveFromServer(l);
                            }
                            // Affiche depuis SQLite (trié par zone)
                            loadFromSQLite();
                            Toast.makeText(requireContext(),
                                    "Synchronisé ✓", Toast.LENGTH_SHORT).show();
                        } else {
                            // Pas de réseau → affiche depuis SQLite
                            loadFromSQLite();
                            Toast.makeText(requireContext(),
                                    "Mode hors ligne", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Livraison>> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        // Pas de réseau → affiche depuis SQLite
                        loadFromSQLite();
                        Toast.makeText(requireContext(),
                                "Hors ligne : données locales", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Charge depuis SQLite (déjà trié par zone : Bina → Rous Tunis → Ariana)
    private void loadFromSQLite() {
        int livreurId = sessionManager.getUser().getId();
        List<Livraison> livraisons = dbHelper.getLivraisonsAujourdhui(livreurId);
        adapter.setLivraisons(livraisons);

        if (livraisons.isEmpty()) {
            binding.tvCount.setText("Aucune livraison — tirez pour synchroniser");
        } else {
            binding.tvCount.setText(livraisons.size() + " livraison(s) — triées par zone");
        }
    }
}