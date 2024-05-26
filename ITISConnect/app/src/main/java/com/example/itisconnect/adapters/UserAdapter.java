package com.example.itisconnect.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itisconnect.R;
import com.example.itisconnect.models.User;
import com.example.itisconnect.views.EditUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>
{
    private Context context;
    private List<User> users;

    public UserAdapter(Context context, List<User> users)
    {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position)
    {
        User user = users.get(position);
        holder.userEmail.setText(user.getEmail());
        holder.userFullName.setText(user.getFullName());
        holder.userPosition.setText(user.getPosition());
        holder.userCohort.setText(user.getCohort());

        String imageUrl = user.getAvatar();

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fAuth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (currentUser != null)
        {
            db.collection("Users").whereEqualTo("email", currentUser.getEmail()).get()
                    .addOnSuccessListener(queryDocumentSnapshots ->
                    {
                        if (!queryDocumentSnapshots.isEmpty())
                        {
                            boolean isAdmin = Boolean.TRUE.equals(queryDocumentSnapshots.getDocuments().get(0).getBoolean("roleAdmin"));
                            if (isAdmin && !currentUser.getEmail().equals(user.getEmail()))
                            {
                                holder.editButton.setVisibility(View.VISIBLE);
                                holder.deleteButton.setVisibility(View.VISIBLE);
                            }
                            else if (currentUser.getEmail().equals(user.getEmail()))
                            {
                                holder.editButton.setVisibility(View.VISIBLE);
                                holder.deleteButton.setVisibility(View.GONE);
                            }
                        }
                    });
        }

        holder.editButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(context, EditUserActivity.class);
            intent.putExtra("email", user.getEmail());
            intent.putExtra("avatar", user.getAvatar());
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v ->
        {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa thành viên")
                    .setMessage("Bạn có chắc chắn muốn xóa thành viên này không?")
                    .setPositiveButton("Xóa", (dialog, which) ->
                    {
                        db.collection("Users").whereEqualTo("email", user.getEmail()).get()
                                .addOnSuccessListener(queryDocumentSnapshots ->
                                {
                                    if (!queryDocumentSnapshots.isEmpty())
                                    {
                                        db.collection("Users").document(queryDocumentSnapshots.getDocuments().get(0).getId()).delete()
                                                .addOnSuccessListener(aVoid ->
                                                {
                                                    Toast.makeText(context, "Xóa người dùng thành công!", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e ->
                                                {
                                                    Toast.makeText(context, "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });
                        users.remove(user);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        Glide.with(context).load(imageUrl).circleCrop().fitCenter().into(holder.userAvatar);
    }

    @Override
    public int getItemCount()
    {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userEmail, userFullName, userPosition, userCohort;
        public ImageView editButton, deleteButton;
        public CircleImageView userAvatar;


        public UserViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userEmail = itemView.findViewById(R.id.email_user_view);
            userFullName = itemView.findViewById(R.id.username_user_view);
            userPosition = itemView.findViewById(R.id.position_user_view);
            userCohort = itemView.findViewById(R.id.cohort_user_view);
            userAvatar = itemView.findViewById(R.id.avatar_user_view);
            editButton = itemView.findViewById(R.id.edit_button_user_view);
            deleteButton = itemView.findViewById(R.id.delete_button_user_view);
        }
    }
}
