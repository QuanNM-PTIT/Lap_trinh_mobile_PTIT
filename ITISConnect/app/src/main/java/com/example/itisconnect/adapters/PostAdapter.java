package com.example.itisconnect.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itisconnect.R;
import com.example.itisconnect.models.Post;
import com.example.itisconnect.views.EditPostActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>
{
    private Context context;
    private List<Post> posts;

    public PostAdapter(Context context, List<Post> posts)
    {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position)
    {
        Post post = posts.get(position);
        holder.postTitle.setText(post.getTitle());
        holder.postDescription.setText(post.getDescription());
        holder.username.setText(post.getUsername());

        String imageUrl = post.getImageUrl();
        String userAvatarUrl = post.getUserAvatar();
        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(post.getTimeAdded().getSeconds() * 1000);
        holder.postDate.setText(timeAgo);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fAuth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (currentUser != null && currentUser.getEmail().equals(post.getuserEmail()))
        {
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
        }
        else
        {
            db.collection("Users").whereEqualTo("email", currentUser.getEmail()).get()
                    .addOnSuccessListener(queryDocumentSnapshots ->
                    {
                        if (!queryDocumentSnapshots.isEmpty())
                        {
                            boolean isAdmin = Boolean.TRUE.equals(queryDocumentSnapshots.getDocuments().get(0).getBoolean("roleAdmin"));
                            if (isAdmin)
                            {
                                holder.deleteButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }

        holder.editButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(context, EditPostActivity.class);
            intent.putExtra("postImageUrl", post.getImageUrl());
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v ->
        {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa bài viết")
                    .setMessage("Bạn có chắc chắn muốn xóa bài viết này không?")
                    .setPositiveButton("Xóa", (dialog, which) ->
                    {
                        db.collection("Posts").whereEqualTo("timeAdded", post.getTimeAdded()).get()
                                .addOnSuccessListener(queryDocumentSnapshots ->
                                {
                                    if (!queryDocumentSnapshots.isEmpty())
                                    {
                                        db.collection("Posts").document(queryDocumentSnapshots.getDocuments().get(0).getId()).delete();
                                    }
                                });
                        posts.remove(post);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        Glide.with(context)
                .load(imageUrl)
                .fitCenter()
                .into(holder.postImage);

        Glide.with(context)
                .load(userAvatarUrl)
                .circleCrop()
                .fitCenter()
                .into(holder.userAvatar);
    }

    @Override
    public int getItemCount()
    {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        public TextView postTitle, postDescription, postDate, username;
        public ImageView postImage, editButton, deleteButton;
        public CircleImageView userAvatar;


        public PostViewHolder(@NonNull View itemView)
        {
            super(itemView);

            postTitle = itemView.findViewById(R.id.post_title_list);
            postDescription = itemView.findViewById(R.id.post_description_list);
            postDate = itemView.findViewById(R.id.post_date_list);
            username = itemView.findViewById(R.id.post_row_username);
            postImage = itemView.findViewById(R.id.post_image_list);
            userAvatar = itemView.findViewById(R.id.avatar_image_view);
            editButton = itemView.findViewById(R.id.edit_post_button);
            deleteButton = itemView.findViewById(R.id.delete_post_button);
        }
    }
}
