package com.zerostic.androiddevelopment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    List<Users> usersList;

    public SearchAdapter(List<Users> usersList) {
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = usersList.get(position);
        holder.user_name.setText(users.getUserName());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_name, followText;
        MaterialCardView followButton;
        public ViewHolder(View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            followText = itemView.findViewById(R.id.followText);
            followButton = itemView.findViewById(R.id.followButton);
        }
    }
}
