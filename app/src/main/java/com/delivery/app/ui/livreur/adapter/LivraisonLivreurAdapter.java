package com.delivery.app.ui.livreur.adapter;

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

public class LivraisonLivreurAdapter extends RecyclerView.Adapter<LivraisonLivreurAdapter.ViewHolder> {

    private List<Livraison> livraisons = new ArrayList<>();
    private final Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Livraison livraison);
    }

    public LivraisonLivreurAdapter(Context context) {
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
                .inflate(R.layout.item_livraison_livreur, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Livraison l = livraisons.get(position);
        holder.tvOrdre.setText("#" + (position + 1));
        holder.tvCommande.setText("Commande " + l.getNumCommande());
        holder.tvClient.setText(l.getClientFullName());
        holder.tvTel.setText(l.getClientTel());
        holder.tvVille.setText(l.getClientVille());
        holder.tvEtat.setText(l.getEtatLivraison());
        holder.tvEtat.setBackgroundColor(l.getEtatColor());

        holder.card.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(l);
        });
    }

    @Override
    public int getItemCount() { return livraisons.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView tvOrdre, tvCommande, tvClient, tvTel, tvVille, tvEtat;

        ViewHolder(View view) {
            super(view);
            card = view.findViewById(R.id.card);
            tvOrdre = view.findViewById(R.id.tvOrdre);
            tvCommande = view.findViewById(R.id.tvCommande);
            tvClient = view.findViewById(R.id.tvClient);
            tvTel = view.findViewById(R.id.tvTel);
            tvVille = view.findViewById(R.id.tvVille);
            tvEtat = view.findViewById(R.id.tvEtat);
        }
    }
}
