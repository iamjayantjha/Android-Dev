package com.zerostic.androiddevelopment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    List<Country> countryList;

    public RecyclerViewAdapter(List<Country> countryList) {
        this.countryList = countryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.countryName.setText(country.getCountryName());
        holder.countryCurrency.setText(country.getCountryCurrency());
        holder.mapImg.setImageResource(R.drawable.location);
        holder.mapImg.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), country.getCountryName()+"'s Map", Toast.LENGTH_SHORT).show();
        });
        holder.countryName.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), country.getCountryName(), Toast.LENGTH_SHORT).show();
        });
        holder.countryCurrency.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), country.getCountryCurrency(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView countryName, countryCurrency;
        ImageView mapImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.countryName);
            countryCurrency = itemView.findViewById(R.id.countryCurrency);
            mapImg = itemView.findViewById(R.id.mapImg);
        }
    }
}
