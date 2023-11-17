package com.zerostic.androiddevelopment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    ImageView closeBtn, postImg, postBtn;
    EditText caption;
    Uri imgUri;
    StorageTask uploadTask;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        closeBtn = findViewById(R.id.closeBtn);
        postImg = findViewById(R.id.postImg);
        postBtn = findViewById(R.id.postBtn);
        caption = findViewById(R.id.caption);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CropImage.activity()
                .setAspectRatio(1,1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
        closeBtn.setOnClickListener(v -> {
            finish();
        });

        postBtn.setOnClickListener(v -> {
            addPost();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            imgUri = result.getUri();
            postImg.setImageURI(result.getUri());
        }
        else {
            finish();
        }
    }

    private void addPost() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");
        pd.setCancelable(false);
        pd.show();
        if (imgUri != null){
            StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis()+".jpg");
            uploadTask = filePath.putFile(imgUri);
            uploadTask.continueWithTask(task -> {
                if (!task.isComplete()){
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    String myUri = downloadUri.toString();
                    saveData(myUri, pd);
                } else {
                    pd.dismiss();
                }
            });
        }
    }

    private void saveData(String myUri, ProgressDialog pd) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://learn-android-7193b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Posts");
        String postId = reference.push().getKey();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("postId", postId);
        hashMap.put("postImage", myUri);
        hashMap.put("caption", caption.getText().toString());
        hashMap.put("publisher", firebaseUser.getUid());
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        reference.child(postId).setValue(hashMap).addOnCompleteListener(task -> {
              if (task.isSuccessful()){
                    pd.dismiss();
                    finish();
                } else {
                    pd.dismiss();
                }
        });
    }
}