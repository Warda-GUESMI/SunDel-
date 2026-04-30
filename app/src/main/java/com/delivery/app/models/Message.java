package com.delivery.app.models;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("id")
    private int id;

    @SerializedName("expediteur_id")
    private int expediteurId;

    @SerializedName("expediteur_nom")
    private String expediteurNom;

    @SerializedName("destinataire_id")
    private int destinataireId;

    @SerializedName("contenu")
    private String contenu;

    @SerializedName("date_envoi")
    private String dateEnvoi;

    @SerializedName("nocde")
    private int numCommande;

    @SerializedName("type")
    private String type; // "info" or "urgence"

    @SerializedName("lu")
    private boolean lu;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getExpediteurId() { return expediteurId; }
    public void setExpediteurId(int expediteurId) { this.expediteurId = expediteurId; }

    public String getExpediteurNom() { return expediteurNom; }
    public void setExpediteurNom(String expediteurNom) { this.expediteurNom = expediteurNom; }

    public int getDestinataireId() { return destinataireId; }
    public void setDestinataireId(int destinataireId) { this.destinataireId = destinataireId; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public String getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(String dateEnvoi) { this.dateEnvoi = dateEnvoi; }

    public int getNumCommande() { return numCommande; }
    public void setNumCommande(int numCommande) { this.numCommande = numCommande; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isLu() { return lu; }
    public void setLu(boolean lu) { this.lu = lu; }

    public boolean isUrgent() { return "urgence".equals(type); }
}
