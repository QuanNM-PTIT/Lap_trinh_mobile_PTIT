package com.example.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.BR;
import com.example.chatapp.R;
import com.example.chatapp.databinding.RowChatBinding;
import com.example.chatapp.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>
{
    private List<ChatMessage> chatMessages;
    private Context context;

    public ChatAdapter(List<ChatMessage> chatMessages, Context context)
    {
        this.chatMessages = chatMessages;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewTyp)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chat, parent, false);
        RowChatBinding binding = RowChatBinding.bind(view);
        return new ChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position)
    {
        holder.getBinding().setVariable(BR.chatMessage, chatMessages.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount()
    {
        return chatMessages.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder
    {
        private RowChatBinding binding;

        public ChatViewHolder(RowChatBinding binding)
        {
            super(binding.getRoot());
            setBinding(binding);
        }

        private void setBinding(RowChatBinding binding)
        {
            this.binding = binding;
        }

        public RowChatBinding getBinding()
        {
            return binding;
        }
    }
}
