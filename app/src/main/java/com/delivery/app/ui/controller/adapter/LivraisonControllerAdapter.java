package com.delivery.app.ui.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.app.R;
import com.delivery.app.models.Livraison;

import java.util.ArrayList;
import java.util.List;

public class LivraisonControllerAdapter extends RecyclerView.Adapter<LivraisonControllerAdapter.ViewHolder> {

    private List<Livraison> livraisons = new ArrayList<>();
    private final Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Livraison livraison);
    }

    public LivraisonControllerAdapter(Context context) {
        this.context = context;
    }

    public void setLivraisons(List<Livraison> livraisons) {
        this.livraisons = livraisons;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_livraison_controller, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Livraison l = livraisons.get(position);
        holder.tvCommande.setText("Commande #" + l.getNumCommande());
        holder.tvClient.setText(l.getClientFullName());
        holder.tvLivreur.setText(l.getLivreurNom());
        holder.tvDate.setText(l.getDateLivraison());
        holder.tvMontant.setText(String.format("%.2f TND", l.getMontantTotal()));
        holder.tvEtat.setText(l.getEtatLivraison());
        holder.tvEtat.setBackgroundColor(l.getEtatColor());

        holder.card.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(l);
        });
    }

    @Override
    public int getItemCount() {
        return livraisons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView tvCommande, tvClient, tvLivreur, tvDate, tvMontant, tvEtat;

        ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            tvCommande = itemView.findViewById(R.id.tvCommande);
            tvClient = itemView.findViewById(R.id.tvClient);
            tvLivreur = itemView.findViewById(R.id.tvLivreur);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMontant = itemView.findViewById(R.id.tvMontant);
            tvEtat = itemView.findViewById(R.id.tvEtat);
        }
    }
}
