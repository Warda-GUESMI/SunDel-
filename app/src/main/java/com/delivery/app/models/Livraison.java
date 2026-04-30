package com.delivery.app.models;

import com.google.gson.annotations.SerializedName;

public class Livraison {
    @SerializedName("nocde")
    private int numCommande;

    @SerializedName("dateliv")
    private String dateLivraison;

    @SerializedName("livreur_id")
    private int livreurId;

    @SerializedName("livreur_nom")
    private String livreurNom;

    @SerializedName("modepay")
    private String modePaiement;

    @SerializedName("etatliv")
    private String etatLivraison;

    @SerializedName("client_nom")
    private String clientNom;

    @SerializedName("client_prenom")
    private String clientPrenom;

    @SerializedName("client_tel")
    private String clientTel;

    @SerializedName("client_adresse")
    private String clientAdresse;

    @SerializedName("client_ville")
    private String clientVille;

    @SerializedName("client_code_postal")
    private String clientCodePostal;

    @SerializedName("montant_total")
    private double montantTotal;

    @SerializedName("nb_articles")
    private int nbArticles;

    @SerializedName("ordre_livraison")
    private int ordreLivraison;

    @SerializedName("remarques")
    private String remarques;

    // Getters & Setters
    public int getNumCommande() { return numCommande; }
    public void setNumCommande(int numCommande) { this.numCommande = numCommande; }

    public String getDateLivraison() { return dateLivraison; }
    public void setDateLivraison(String dateLivraison) { this.dateLivraison = dateLivraison; }

    public int getLivreurId() { return livreurId; }
    public void setLivreurId(int livreurId) { this.livreurId = livreurId; }

    public String getLivreurNom() { return livreurNom; }
    public void setLivreurNom(String livreurNom) { this.livreurNom = livreurNom; }

    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

    public String getEtatLivraison() { return etatLivraison; }
    public void setEtatLivraison(String etatLivraison) { this.etatLivraison = etatLivraison; }

    public String getClientNom() { return clientNom; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }

    public String getClientPrenom() { return clientPrenom; }
    public void setClientPrenom(String clientPrenom) { this.clientPrenom = clientPrenom; }

    public String getClientTel() { return clientTel; }
    public void setClientTel(String clientTel) { this.clientTel = clientTel; }

    public String getClientAdresse() { return clientAdresse; }
    public void setClientAdresse(String clientAdresse) { this.clientAdresse = clientAdresse; }

    public String getClientVille() { return clientVille; }
    public void setClientVille(String clientVille) { this.clientVille = clientVille; }

    public String getClientCodePostal() { return clientCodePostal; }
    public void setClientCodePostal(String clientCodePostal) { this.clientCodePostal = clientCodePostal; }

    public double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(double montantTotal) { this.montantTotal = montantTotal; }

    public int getNbArticles() { return nbArticles; }
    public void setNbArticles(int nbArticles) { this.nbArticles = nbArticles; }

    public int getOrdreLivraison() { return ordreLivraison; }
    public void setOrdreLivraison(int ordreLivraison) { this.ordreLivraison = ordreLivraison; }

    public String getRemarques() { return remarques; }
    public void setRemarques(String remarques) { this.remarques = remarques; }

    public String getClientFullName() {
        return (clientPrenom != null ? clientPrenom : "") + " " + (clientNom != null ? clientNom : "");
    }

    public String getAdresseComplete() {
        return clientAdresse + ", " + clientVille + " " + clientCodePostal;
    }

    public String getGoogleMapsUrl() {
        String adresse = getAdresseComplete().replace(" ", "+");
        return "https://www.google.com/maps/search/?api=1&query=" + adresse;
    }
    public int getEtatColor() {
        if (etatLivraison == null) return 0xFF9E9E9E;
        switch (etatLivraison.toLowerCase()) {
            case "livree": case "livrée": return 0xFF4CAF50;
            case "en_cours": case "en cours": return 0xFFFFC107;
            case "echouee": case "échouée": return 0xFFF44336;
            default: return 0xFF9E9E9E;
        }
    }
    public int getZoneOrder() {
        if (clientVille == null) return 99;
        switch (clientVille.toLowerCase()) {
            case "bina": return 1;
            case "rous tunis": return 2;
            case "ariana": return 3;
            default: return 99;
        }
    }
}
