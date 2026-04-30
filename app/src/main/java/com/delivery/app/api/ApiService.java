package com.delivery.app.api;

import com.delivery.app.models.DashboardStats;
import com.delivery.app.models.Livraison;
import com.delivery.app.models.Message;
import com.delivery.app.models.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // ==================== AUTH ====================
    @POST("auth/login")
    Call<User> login(@Body Map<String, String> credentials);

    // ==================== LIVRAISONS ====================
    @GET("livraisons")
    Call<List<Livraison>> getAllLivraisons(
            @Header("Authorization") String token,
            @Query("date_debut") String dateDebut,
            @Query("date_fin") String dateFin,
            @Query("etat") String etat,
            @Query("livreur_id") Integer livreurId,
            @Query("client_nom") String clientNom,
            @Query("nocde") Integer numCommande
    );

    @GET("livraisons/today")
    Call<List<Livraison>> getLivraisonsToday(
            @Header("Authorization") String token
    );

    @GET("livraisons/livreur/{id}/today")
    Call<List<Livraison>> getLivraisonsLivreurToday(
            @Header("Authorization") String token,
            @Path("id") int livreurId
    );

    @GET("livraisons/{nocde}")
    Call<Livraison> getLivraisonDetail(
            @Header("Authorization") String token,
            @Path("nocde") int numCommande
    );

    @PUT("livraisons/{nocde}")
    Call<Livraison> updateLivraison(
            @Header("Authorization") String token,
            @Path("nocde") int numCommande,
            @Body Map<String, String> body
    );

    // ==================== DASHBOARD ====================
    @GET("dashboard")
    Call<DashboardStats> getDashboard(
            @Header("Authorization") String token,
            @Query("date_debut") String dateDebut,
            @Query("date_fin") String dateFin
    );

    // ==================== MESSAGES ====================
    @GET("messages")
    Call<List<Message>> getMessages(
            @Header("Authorization") String token
    );

    @POST("messages")
    Call<Message> sendMessage(
            @Header("Authorization") String token,
            @Body Map<String, Object> body
    );

    @PUT("messages/{id}/lu")
    Call<Void> markAsRead(
            @Header("Authorization") String token,
            @Path("id") int messageId
    );

    // ==================== LIVREURS ====================
    @GET("livreurs")
    Call<List<User>> getLivreurs(
            @Header("Authorization") String token
    );
}
