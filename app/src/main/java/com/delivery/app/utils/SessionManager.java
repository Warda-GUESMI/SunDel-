package com.delivery.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.delivery.app.models.User;
import com.google.gson.Gson;

public class SessionManager {

    private static final String PREF_NAME = "DeliveryAppSession";
    private static final String KEY_USER = "user";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveUser(User user) {
        Gson gson = new Gson();
        editor.putString(KEY_USER, gson.toJson(user));
        editor.putString(KEY_TOKEN, "Bearer " + user.getToken());
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public User getUser() {
        String userJson = prefs.getString(KEY_USER, null);
        if (userJson == null) return null;
        return new Gson().fromJson(userJson, User.class);
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public boolean isControleur() {
        User user = getUser();
        if (user == null) return false;
        return "controleur".equalsIgnoreCase(user.getCodePoste())
                || "ctrl".equalsIgnoreCase(user.getCodePoste());
    }

    public boolean isLivreur() {
        User user = getUser();
        if (user == null) return false;
        return "livreur".equalsIgnoreCase(user.getCodePoste())
                || "livr".equalsIgnoreCase(user.getCodePoste());
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
