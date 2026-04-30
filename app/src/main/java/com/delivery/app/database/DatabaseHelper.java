package com.delivery.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.delivery.app.models.Livraison;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "delivery.db";
    private static final int DB_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE livraisons (" +
                "nocde INTEGER PRIMARY KEY, " +
                "client TEXT, " +
                "adresse TEXT, " +
                "ville TEXT, " +
                "etatliv TEXT, " +
                "remarques TEXT, " +
                "is_synced INTEGER DEFAULT 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int next) {
        db.execSQL("DROP TABLE IF EXISTS livraisons");
        onCreate(db);
    }

    public void saveFromServer(Livraison l) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("nocde", l.getNumCommande());
        v.put("client", l.getClientNom());
        v.put("adresse", l.getClientAdresse());
        v.put("ville", l.getClientVille());
        v.put("etatliv", l.getEtatLivraison());
        v.put("remarques", l.getRemarques());
        v.put("is_synced", 1);
        db.insertWithOnConflict("livraisons", null, v, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void updateOffline(Integer nocde, String etat, String rem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("etatliv", etat);
        v.put("remarques", rem);
        v.put("is_synced", 0);
        db.update("livraisons", v, "nocde = ?", new String[]{String.valueOf(nocde)});
        db.close();
    }

    public List<Livraison> getUnsynced() {
        List<Livraison> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT nocde, etatliv, remarques FROM livraisons WHERE is_synced = 0", null);
        if (c.moveToFirst()) {
            do {
                Livraison l = new Livraison();
                l.setNumCommande(c.getInt(0));
                l.setEtatLivraison(c.getString(1));
                l.setRemarques(c.getString(2));
                list.add(l);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public void markSynced() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE livraisons SET is_synced = 1");
        db.close();
    }

    public List<Livraison> getLivraisonsAujourdhui(int livreurId) {
        List<Livraison> list = new ArrayList<>();
        String query = "SELECT * FROM livraisons ORDER BY " +
                "CASE LOWER(ville) " +
                "WHEN 'tunis' THEN 1 " +
                "WHEN 'ariana' THEN 2 " +
                "ELSE 99 END ASC";
        Cursor c = getReadableDatabase().rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                Livraison l = new Livraison();
                l.setNumCommande(c.getInt(c.getColumnIndexOrThrow("nocde")));
                l.setClientNom(c.getString(c.getColumnIndexOrThrow("client")));
                l.setClientAdresse(c.getString(c.getColumnIndexOrThrow("adresse")));
                l.setClientVille(c.getString(c.getColumnIndexOrThrow("ville")));
                l.setEtatLivraison(c.getString(c.getColumnIndexOrThrow("etatliv")));
                l.setRemarques(c.getString(c.getColumnIndexOrThrow("remarques")));
                list.add(l);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public void clearAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM livraisons");
        db.close();
    }
}