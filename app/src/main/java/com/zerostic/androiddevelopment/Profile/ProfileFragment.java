package com.zerostic.androiddevelopment.Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.zerostic.androiddevelopment.LoginActivity;
import com.zerostic.androiddevelopment.R;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    Button logout;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        logout = view.findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();
        logout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            Objects.requireNonNull(requireActivity()).finish();
        });
        return view;
    }
}