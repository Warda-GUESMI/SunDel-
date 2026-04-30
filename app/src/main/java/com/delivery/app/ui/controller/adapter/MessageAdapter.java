package com.delivery.app.ui.controller.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.app.R;
import com.delivery.app.models.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<Message> messages = new ArrayList<>();
    private final Context context;
    private final int currentUserId;

    public MessageAdapter(Context context, int currentUserId) {
        this.context = context;
        this.currentUserId = currentUserId;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getExpediteurId() == currentUserId
                ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = viewType == VIEW_TYPE_SENT
                ? R.layout.item_message_sent
                : R.layout.item_message_received;
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message msg = messages.get(position);
        holder.tvContent.setText(msg.getContenu());
        holder.tvTime.setText(msg.getDateEnvoi());
        if (holder.tvSender != null) {
            holder.tvSender.setText(msg.getExpediteurNom());
        }
        if (msg.isUrgent() && holder.tvUrgent != null) {
            holder.tvUrgent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() { return messages.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent, tvTime, tvSender, tvUrgent;

        ViewHolder(View view) {
            super(view);
            tvContent = view.findViewById(R.id.tvContent);
            tvTime = view.findViewById(R.id.tvTime);
            tvSender = view.findViewById(R.id.tvSender);
            tvUrgent = view.findViewById(R.id.tvUrgent);
        }
    }
}
