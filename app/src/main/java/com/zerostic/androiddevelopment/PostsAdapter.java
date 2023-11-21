package com.zerostic.androiddevelopment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder>{
    List<Posts> postsList;
    Context context;

    public PostsAdapter(Context context, List<Posts> postsList) {
        this.postsList = postsList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        Posts posts = postsList.get(position);
        holder.caption.setText(posts.getCaption());
        Glide.with(context).load(posts.getPostImage()).into(holder.userPost);
        getUserData(holder, posts.getPublisher());
    }

    private void getUserData(PostsViewHolder holder, String publisher) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://learn-android-7193b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(publisher);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                Glide.with(context).load(users.getImageUrl()).into(holder.userProfilePicture);
                holder.userName.setText(users.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    static class PostsViewHolder extends RecyclerView.ViewHolder {
        ImageView userPost;
        TextView caption, userName;
        CircleImageView userProfilePicture;
        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            userPost = itemView.findViewById(R.id.userPost);
            caption = itemView.findViewById(R.id.caption);
            userProfilePicture = itemView.findViewById(R.id.userProfilePicture);
            userName = itemView.findViewById(R.id.userName);
        }

    }
}