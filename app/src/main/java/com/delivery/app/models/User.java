package com.delivery.app.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("idpers")
    private int id;

    @SerializedName("nompers")
    private String nom;

    @SerializedName("prenompers")
    private String prenom;

    @SerializedName("login")
    private String login;

    @SerializedName("telpers")
    private String telephone;

    @SerializedName("codeposte")
    private String codePoste;

    @SerializedName("token")
    private String token;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getCodePoste() { return codePoste; }
    public void setCodePoste(String codePoste) { this.codePoste = codePoste; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getFullName() {
        return prenom + " " + nom;
    }
}
