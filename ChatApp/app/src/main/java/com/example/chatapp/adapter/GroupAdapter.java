package com.example.chatapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ItemCardBinding;
import com.example.chatapp.model.ChatGroup;
import com.example.chatapp.views.ChatActivity;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder>
{
    private ArrayList<ChatGroup> chatGroups;

    public GroupAdapter(ArrayList<ChatGroup> chatGroups)
    {
        this.chatGroups = chatGroups;
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder
    {
        private ItemCardBinding binding;

        public GroupViewHolder(@NonNull ItemCardBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    int position = getAdapterPosition();
                    ChatGroup chatGroup = chatGroups.get(position);
                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
                    intent.putExtra("GROUP_NAME", chatGroup.getGroupName());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        ItemCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_card, parent, false);
        return new GroupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position)
    {
        ChatGroup currentGroup = chatGroups.get(position);
        holder.binding.setChatGroup(currentGroup);
    }

    @Override
    public int getItemCount()
    {
        return chatGroups.size();
    }
}
