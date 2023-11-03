package com.zerostic.androiddevelopment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyAdapter extends ArrayAdapter {
    String[] countries;
    String[] currencies;
    public MyAdapter(@NonNull Context context, String[] countries, String[] currencies) {
        super(context, R.layout.custom_list_item, R.id.countryName, countries);
        this.countries = countries;
        this.currencies = currencies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_list_item, parent, false);
        ImageView mapImg = view.findViewById(R.id.mapImg);
        TextView countryName = view.findViewById(R.id.countryName);
        TextView countryCurrency = view.findViewById(R.id.countryCurrency);
        mapImg.setImageResource(R.drawable.location);
        countryName.setText(countries[position]);
        countryCurrency.setText(currencies[position]);
        return view;
    }
}
