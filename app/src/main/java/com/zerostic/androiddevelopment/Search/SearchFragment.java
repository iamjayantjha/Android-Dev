package com.zerostic.androiddevelopment.Search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerostic.androiddevelopment.R;
import com.zerostic.androiddevelopment.SearchAdapter;
import com.zerostic.androiddevelopment.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {
    RecyclerView users;
    List<Users> usersList;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    SearchAdapter searchAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        users = view.findViewById(R.id.usersList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        users.setLayoutManager(linearLayoutManager);
        usersList = new ArrayList<>();
        getUsers();
        return view;
    }

    private void getUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://learn-android-7193b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                   if (dataSnapshot.exists()){
                       Users users = dataSnapshot.getValue(Users.class);
                       assert users != null;
                       if (!users.getId().equals(currentUser.getUid())){
                           usersList.add(users);
                       }
                      // Log.d("USER DATA", Objects.requireNonNull(users).getId());
                   //    Log.d("USER DATA", Objects.requireNonNull(dataSnapshot.getKey()));
                   }
                }
                searchAdapter = new SearchAdapter(usersList);
                users.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}