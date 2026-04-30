package com.delivery.app.ui.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.delivery.app.api.RetrofitClient;
import com.delivery.app.databinding.FragmentDashboardBinding;
import com.delivery.app.models.DashboardStats;
import com.delivery.app.utils.SessionManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        loadDashboard();
    }

    private void loadDashboard() {
        binding.progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance().getApiService()
                .getDashboard(sessionManager.getToken(), null, null)
                .enqueue(new Callback<DashboardStats>() {
                    @Override
                    public void onResponse(Call<DashboardStats> call, Response<DashboardStats> response) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            updateUI(response.body());
                        } else {
                            Toast.makeText(requireContext(), "Erreur chargement", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DashboardStats> call, Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(DashboardStats stats) {
        // Summary cards
        binding.tvTotalLivraisons.setText(String.valueOf(stats.getTotalLivraisons()));
        binding.tvLivrees.setText(String.valueOf(stats.getLivrees()));
        binding.tvEnCours.setText(String.valueOf(stats.getEnCours()));
        binding.tvEchouees.setText(String.valueOf(stats.getEchouees()));

        // Pie chart - état global
        setupPieChart(stats);

        // Bar chart - par livreur
        if (stats.getParLivreur() != null) {
            setupBarChart(binding.barChartLivreur, stats.getParLivreur(), "Par livreur");
        }

        // Bar chart - par client
        if (stats.getParClient() != null) {
            setupBarChart(binding.barChartClient, stats.getParClient(), "Par client");
        }
    }

    private void setupPieChart(DashboardStats stats) {
        List<PieEntry> entries = new ArrayList<>();
        if (stats.getLivrees() > 0)
            entries.add(new PieEntry(stats.getLivrees(), "Livrées"));
        if (stats.getEnCours() > 0)
            entries.add(new PieEntry(stats.getEnCours(), "En cours"));
        if (stats.getEchouees() > 0)
            entries.add(new PieEntry(stats.getEchouees(), "Échouées"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{0xFF4CAF50, 0xFFFFC107, 0xFFF44336});
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        binding.pieChart.setData(data);
        binding.pieChart.setHoleRadius(40f);
        binding.pieChart.setTransparentCircleRadius(45f);
        binding.pieChart.getDescription().setEnabled(false);
        binding.pieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        binding.pieChart.animateY(800);
        binding.pieChart.invalidate();
    }

    private void setupBarChart(BarChart chart, List<DashboardStats.StatItem> items, String label) {
        List<BarEntry> livreesEntries = new ArrayList<>();
        List<BarEntry> enCoursEntries = new ArrayList<>();
        List<BarEntry> echEntries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < Math.min(items.size(), 8); i++) {
            DashboardStats.StatItem item = items.get(i);
            livreesEntries.add(new BarEntry(i, item.getLivrees()));
            enCoursEntries.add(new BarEntry(i, item.getEnCours()));
            echEntries.add(new BarEntry(i, item.getEchouees()));
            labels.add(item.getNom());
        }

        BarDataSet dsLivrees = new BarDataSet(livreesEntries, "Livrées");
        dsLivrees.setColor(0xFF4CAF50);
        BarDataSet dsEnCours = new BarDataSet(enCoursEntries, "En cours");
        dsEnCours.setColor(0xFFFFC107);
        BarDataSet dsEch = new BarDataSet(echEntries, "Échouées");
        dsEch.setColor(0xFFF44336);

        BarData barData = new BarData(dsLivrees, dsEnCours, dsEch);
        barData.setBarWidth(0.25f);

        chart.setData(barData);
        chart.groupBars(0f, 0.1f, 0.05f);
        chart.getDescription().setEnabled(false);
        chart.animateY(800);
        chart.invalidate();
    }
}
