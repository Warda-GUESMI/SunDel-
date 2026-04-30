package com.delivery.app.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DashboardStats {

    @SerializedName("total_livraisons")
    private int totalLivraisons;

    @SerializedName("livrees")
    private int livrees;

    @SerializedName("en_cours")
    private int enCours;

    @SerializedName("echouees")
    private int echouees;

    @SerializedName("par_livreur")
    private List<StatItem> parLivreur;

    @SerializedName("par_client")
    private List<StatItem> parClient;

    public int getTotalLivraisons() { return totalLivraisons; }
    public void setTotalLivraisons(int totalLivraisons) { this.totalLivraisons = totalLivraisons; }

    public int getLivrees() { return livrees; }
    public void setLivrees(int livrees) { this.livrees = livrees; }

    public int getEnCours() { return enCours; }
    public void setEnCours(int enCours) { this.enCours = enCours; }

    public int getEchouees() { return echouees; }
    public void setEchouees(int echouees) { this.echouees = echouees; }

    public List<StatItem> getParLivreur() { return parLivreur; }
    public void setParLivreur(List<StatItem> parLivreur) { this.parLivreur = parLivreur; }

    public List<StatItem> getParClient() { return parClient; }
    public void setParClient(List<StatItem> parClient) { this.parClient = parClient; }

    public static class StatItem {
        @SerializedName("nom")
        private String nom;

        @SerializedName("total")
        private int total;

        @SerializedName("livrees")
        private int livrees;

        @SerializedName("en_cours")
        private int enCours;

        @SerializedName("echouees")
        private int echouees;

        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }

        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }

        public int getLivrees() { return livrees; }
        public void setLivrees(int livrees) { this.livrees = livrees; }

        public int getEnCours() { return enCours; }
        public void setEnCours(int enCours) { this.enCours = enCours; }

        public int getEchouees() { return echouees; }
        public void setEchouees(int echouees) { this.echouees = echouees; }
    }
}
