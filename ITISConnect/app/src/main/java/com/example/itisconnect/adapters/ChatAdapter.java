package com.example.itisconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.itisconnect.BR;
import com.example.itisconnect.R;
import com.example.itisconnect.databinding.ChatItemBinding;
import com.example.itisconnect.models.ChatMessage;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>
{
    private List<ChatMessage> chatMessages;
    private Context context;

    public ChatAdapter()
    {
    }

    public ChatAdapter(List<ChatMessage> chatMessages, Context context)
    {
        this.chatMessages = chatMessages;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewTyp)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false);
        ChatItemBinding binding = ChatItemBinding.bind(view);
        return new ChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position)
    {
        holder.getBinding().setVariable(BR.chatMessage, chatMessages.get(position));
        holder.getBinding().executePendingBindings();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users").whereEqualTo("email", chatMessages.get(position).getSenderEmail()).get()
                .addOnSuccessListener(documentSnapshot ->
                {
                    if (!documentSnapshot.isEmpty())
                    {
                        String imageUrl = documentSnapshot.getDocuments().get(0).getString("avatar");
                        Glide.with(context).load(imageUrl).into(holder.getBinding().userChatImage);
                        Glide.with(context).load(imageUrl).into(holder.getBinding().userChatImage2);
                    }
                })
                .addOnFailureListener(e ->
                {
                    Toast.makeText(context, "Error getting user image", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount()
    {
        return chatMessages.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder
    {
        private ChatItemBinding binding;

        public ChatViewHolder(ChatItemBinding binding)
        {
            super(binding.getRoot());
            setBinding(binding);
        }

        private void setBinding(ChatItemBinding binding)
        {
            this.binding = binding;
        }

        public ChatItemBinding getBinding()
        {
            return binding;
        }
    }
}
