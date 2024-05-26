package com.example.itisconnect.adapters;

import static androidx.core.content.ContextCompat.startActivity;

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
import com.example.itisconnect.models.Task;
import com.example.itisconnect.views.TaskDetailViewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>
{
    private Context context;
    private List<Task> tasks;

    public TaskAdapter(Context context, List<Task> tasks)
    {
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position)
    {
        Task task = tasks.get(position);

        String email = task.getAssignedToEmail();

        holder.title.setText(task.getTitle());
        holder.status.setText(task.getStatus());

        String deadline = task.getStartDate() + " - " + task.getDueDate();

        holder.deadline.setText(deadline);

        holder.itemView.setOnClickListener(v ->
        {
            Intent intent = new Intent(context, TaskDetailViewActivity.class);
            intent.putExtra("taskId", task.getTaskId());
            startActivity(context, intent, null);
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fAuth.getCurrentUser();

        db.collection("Users").whereEqualTo("email", email).get().addOnSuccessListener(queryDocumentSnapshots ->
        {
            if (!queryDocumentSnapshots.isEmpty())
            {
                String fullname = queryDocumentSnapshots.getDocuments().get(0).getString("fullName");
                String imageUrl = queryDocumentSnapshots.getDocuments().get(0).getString("avatar");
                holder.assignedToFullname.setText(fullname);

                Glide.with(context).load(imageUrl).into(holder.avatar);
            }
        });

        if (currentUser != null)
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

        holder.deleteButton.setOnClickListener(v ->
        {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa task")
                    .setMessage("Bạn có chắc chắn muốn xóa task này không?")
                    .setPositiveButton("Xóa", (dialog, which) ->
                    {
                        db.collection("Tasks").whereEqualTo("taskId", task.getTaskId()).get()
                                .addOnSuccessListener(queryDocumentSnapshots ->
                                {
                                    if (!queryDocumentSnapshots.isEmpty())
                                    {
                                        db.collection("Tasks").document(queryDocumentSnapshots.getDocuments().get(0).getId()).delete()
                                                .addOnSuccessListener(aVoid ->
                                                {
                                                    Toast.makeText(context, "Xóa task thành công!", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e ->
                                                {
                                                    Toast.makeText(context, "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });
                        tasks.remove(task);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });


    }

    @Override
    public int getItemCount()
    {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder
    {
        public TextView assignedToFullname, title, status, deadline;
        public CircleImageView avatar;
        public ImageView deleteButton;


        public TaskViewHolder(@NonNull View itemView)
        {
            super(itemView);
            assignedToFullname = itemView.findViewById(R.id.fullname_task_view);
            title = itemView.findViewById(R.id.task_title_task_view);
            status = itemView.findViewById(R.id.status_task_view);
            deadline = itemView.findViewById(R.id.deadline_task_view);
            avatar = itemView.findViewById(R.id.avatar_user_task_view);
            deleteButton = itemView.findViewById(R.id.delete_button_task_view);
        }
    }
}
