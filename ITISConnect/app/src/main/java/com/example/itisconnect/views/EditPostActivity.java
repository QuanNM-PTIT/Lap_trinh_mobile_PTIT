package com.example.itisconnect.views;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.itisconnect.R;
import com.example.itisconnect.models.Post;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditPostActivity extends AppCompatActivity
{
    private Button addPostButton, cancelPostButton;
    private ImageView postImage, addPhotoButton;
    private ProgressBar progressBar;
    private EditText postTitle, postDescription;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageRef;
    private CollectionReference colRef = db.collection("Posts");
    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;
    private String currentUserEmail;
    private String currentUserAvatar;
    private String currentUserUsername;
    private ActivityResultLauncher<String> mTakePhoto;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);

        progressBar = findViewById(R.id.post_progressBar);
        postTitle = findViewById(R.id.post_title_ET);
        postDescription = findViewById(R.id.post_description_ET);
        postImage = findViewById(R.id.post_image_view);
        addPostButton = findViewById(R.id.post_save_button);
        addPhotoButton = findViewById(R.id.post_camera_button);
        cancelPostButton = findViewById(R.id.post_cancel_button);
        progressBar.setVisibility(View.INVISIBLE);

        String postImageUrl = getIntent().getStringExtra("postImageUrl");

        db.collection("Posts").whereEqualTo("imageUrl", postImageUrl).get().addOnSuccessListener(queryDocumentSnapshots ->
        {
            postTitle.setText(queryDocumentSnapshots.getDocuments().get(0).getString("title"));
            postDescription.setText(queryDocumentSnapshots.getDocuments().get(0).getString("description"));

            String imageUrl = queryDocumentSnapshots.getDocuments().get(0).getString("imageUrl");
            if (imageUrl != null)
            {
                Glide.with(this).load(imageUrl).into(postImage);
            }
        });

        storageRef = FirebaseStorage.getInstance().getReference();

        addPostButton.setOnClickListener(v ->
        {
            savePost();
        });

        mTakePhoto = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>()
        {
            @Override
            public void onActivityResult(Uri o)
            {
                postImage.setImageURI(o);
                imageUri = o;
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mTakePhoto.launch("image/*");
            }
        });

        cancelPostButton.setOnClickListener(v ->
        {
            finish();
        });
    }

    private void savePost()
    {
        db.collection("Posts").whereEqualTo("imageUrl", getIntent().getStringExtra("postImageUrl")).get().addOnSuccessListener(queryDocumentSnapshots ->
        {
            String postId = queryDocumentSnapshots.getDocuments().get(0).getId();

            String title = postTitle.getText().toString().trim();
            String description = postDescription.getText().toString().trim();
            Timestamp timeAdded = queryDocumentSnapshots.getDocuments().get(0).getTimestamp("timeAdded");

            if (title.isEmpty() || description.isEmpty())
            {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            if (imageUri != null)
            {
                final StorageReference filepath = storageRef.child("post_images").child("post_image_" + System.currentTimeMillis());

                filepath.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                {
                    filepath.getDownloadUrl().addOnSuccessListener(uri ->
                    {
                        String imageUrl = uri.toString();

                        Post post = new Post(title, description, imageUrl, currentUserUsername, currentUserEmail, currentUserAvatar, timeAdded);

                        colRef.document(postId).set(post).addOnSuccessListener(aVoid ->
                        {
                            Toast.makeText(this, "Cập nhật bài đăng thành công!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();
                        }).addOnFailureListener(e ->
                        {
                            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        });
                    });
                });
            }
            else
            {
                Post post = new Post(title, description, getIntent().getStringExtra("postImageUrl"), currentUserEmail, currentUserUsername, currentUserAvatar, timeAdded);
                colRef.document(postId).set(post);
                Toast.makeText(this, "Cập nhật bài đăng thành công!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                finish();
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();

        if (currentUser != null)
        {
            currentUserEmail = currentUser.getEmail();
            if (currentUserEmail != null)
            {
                db.collection("Users").whereEqualTo("email", currentUserEmail).get().addOnSuccessListener(queryDocumentSnapshots ->
                {
                    currentUserUsername = "" + queryDocumentSnapshots.getDocuments().get(0).getString("fullName");
                    currentUserAvatar = "" + queryDocumentSnapshots.getDocuments().get(0).getString("userAvatar");
                });
            }
        }
    }
}