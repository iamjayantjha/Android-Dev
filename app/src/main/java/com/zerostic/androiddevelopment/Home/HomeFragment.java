package com.zerostic.androiddevelopment.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zerostic.androiddevelopment.Posts;
import com.zerostic.androiddevelopment.PostsAdapter;
import com.zerostic.androiddevelopment.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    List<Posts> postsList;
    List<String> followingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        postsList = new ArrayList<>();
        followingList = new ArrayList<>();
        getFollowers();
        return view;
    }

    private void getFollowers() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://learn-android-7193b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    followingList.add(dataSnapshot.getKey());
                }
                followingList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                getPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPosts() {
        Log.d("HomeFragmentData", "getPosts: " + followingList.size());
        DatabaseReference reference = FirebaseDatabase.getInstance("https://learn-android-7193b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Posts posts = dataSnapshot.getValue(Posts.class);
                    for (String id : followingList) {
                        if (posts.getPublisher().equals(id)) {
                            postsList.add(posts);
                        }
                    }
                }
                PostsAdapter postsAdapter = new PostsAdapter(getContext(), postsList);
                recyclerView.setAdapter(postsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}