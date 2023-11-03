package com.zerostic.androiddevelopment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        if (message.getSentBy().equals(Message.SENT_BY_BOT)){
            holder.botMessage.setVisibility(View.VISIBLE);
            holder.userMessage.setVisibility(View.GONE);
            holder.botMessage.setText(message.getMessage());
        }else{
            holder.botMessage.setVisibility(View.GONE);
            holder.userMessage.setVisibility(View.VISIBLE);
            holder.userMessage.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView botMessage, userMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            botMessage = itemView.findViewById(R.id.botMessage);
            userMessage = itemView.findViewById(R.id.userMessage);
        }
    }
}
