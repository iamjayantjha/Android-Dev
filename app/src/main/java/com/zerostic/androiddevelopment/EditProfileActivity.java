package com.zerostic.androiddevelopment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    TextView changeProfilePhoto;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    StorageTask uploadTask;
    StorageReference storageReference;
    CircleImageView profile_image;
    ImageView closeBtn, saveBtn;
    EditText email, fullName, userName, bio;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        changeProfilePhoto = findViewById(R.id.changeProfilePhoto);
        profile_image = findViewById(R.id.profile_image);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        closeBtn = findViewById(R.id.closeBtn);
        saveBtn = findViewById(R.id.saveBtn);
        email = findViewById(R.id.email);
        fullName = findViewById(R.id.fullName);
        userName = findViewById(R.id.userName);
        bio = findViewById(R.id.bio);
        getUserInfo();
        storageReference = FirebaseStorage.getInstance().getReference("/profile_photos/");
        changeProfilePhoto.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        });
        closeBtn.setOnClickListener(v -> finish());
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkUsername(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        saveBtn.setOnClickListener(v->{
            DatabaseReference reference = FirebaseDatabase.getInstance("https://learn-android-7193b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(firebaseUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Users user = snapshot.getValue(Users.class);
                    assert user != null;
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", firebaseUser.getUid());
                    hashMap.put("fullName", fullName.getText().toString());
                    hashMap.put("userName", userName.getText().toString());
                    hashMap.put("bio", bio.getText().toString());
                    hashMap.put("imageUrl", user.getImageUrl());
                    hashMap.put("email", email.getText().toString());
                    reference.setValue(hashMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(EditProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(EditProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });
    }

    private void getUserInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://learn-android-7193b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(firebaseUser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                assert user != null;
                email.setText(user.getEmail());
                fullName.setText(user.getFullName());
                userName.setText(user.getUserName());
                bio.setText(user.getBio());
                if (user.getImageUrl().equals("default")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUsername(String userName1) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://learn-android-7193b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isUsernameAvailable = true;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    assert users != null;
                    if (users.getUserName().equals(userName1) && !users.getId().equals(firebaseUser.getUid())){
                        isUsernameAvailable = false;
                        break;
                    }
                }
                if (isUsernameAvailable){
                    saveBtn.setEnabled(true);
                    saveBtn.setColorFilter(getResources().getColor(R.color.followCard));
                }else {
                    saveBtn.setEnabled(false);
                    saveBtn.setColorFilter(getResources().getColor(R.color.followingCard));
                    userName.setError("Username not available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profile_image.setImageURI(imageUri);
            uploadProfilePicture();
        }else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfilePicture() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        if (imageUri != null){
            StorageReference fileReference = storageReference.child(firebaseUser.getUid()+ "." + "jpg");
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    String myUri = downloadUri.toString();
                    saveData(myUri, progressDialog);
                } else {
                    progressDialog.dismiss();
                }
            });
        }
    }
// Error, Information, Warning, Debug
    private void saveData(String myUri, ProgressDialog progressDialog) {
        Log.i("EditProfileSaveData","Save Data Method Called");
        DatabaseReference reference = FirebaseDatabase.getInstance("https://learn-android-7193b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users").child(firebaseUser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.i("EditProfileSaveData","Data Retrieved Successfully");
                    Users user = snapshot.getValue(Users.class);
                    assert user != null;
                    user.setImageUrl(myUri);
                    reference.setValue(user);
                    progressDialog.dismiss();
                }else {
                    Log.w("EditProfileSaveData","Data Not Retrieved");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}