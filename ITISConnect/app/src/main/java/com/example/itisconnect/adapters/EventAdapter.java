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

import com.example.itisconnect.R;
import com.example.itisconnect.models.Event;
import com.example.itisconnect.views.EventDetailViewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
{
    private Context context;
    private List<Event> events;

    public EventAdapter(Context context, List<Event> events)
    {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position)
    {
        Event event = events.get(position);

        holder.title.setText(event.getTitle());
        holder.description.setText(event.getDescription());
        holder.status.setText(event.getStatus());

        holder.itemView.setOnClickListener(v ->
        {
            Intent intent = new Intent(context, EventDetailViewActivity.class);
            intent.putExtra("eventId", event.getEventId());
            context.startActivity(intent);
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fAuth.getCurrentUser();

        holder.delete.setOnClickListener(v ->
        {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa sự kiện")
                    .setMessage("Bạn có chắc chắn muốn xóa sự kiện này không?")
                    .setPositiveButton("Xóa", (dialog, which) ->
                    {
                        db.collection("Events").whereEqualTo("eventId", event.getEventId()).get()
                                .addOnSuccessListener(queryDocumentSnapshots ->
                                {
                                    if (!queryDocumentSnapshots.isEmpty())
                                    {
                                        db.collection("Events").document(queryDocumentSnapshots.getDocuments().get(0).getId()).delete()
                                                .addOnSuccessListener(aVoid ->
                                                {
                                                    Toast.makeText(context, "Xóa sự kiện thành công!", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e ->
                                                {
                                                    Toast.makeText(context, "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });
                        events.remove(event);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
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
                                holder.delete.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }


    }

    @Override
    public int getItemCount()
    {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title, description, status;
        public ImageView delete;

        public EventViewHolder(@NonNull View itemView)
        {
            super(itemView);

            title = itemView.findViewById(R.id.title_event_view);
            description = itemView.findViewById(R.id.description_event_view);
            status = itemView.findViewById(R.id.status_event_view);
            delete = itemView.findViewById(R.id.delete_button_event_view);

        }
    }
}
