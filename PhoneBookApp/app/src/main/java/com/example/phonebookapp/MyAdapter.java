package com.example.phonebookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phonebookapp.databinding.ItemCardBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.UserViewHolder>
{
    private Context context;
    private ArrayList<User> users;

    public MyAdapter(Context context, ArrayList<User> users)
    {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        ItemCardBinding itemCardBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_card, parent, false
        );
        return new UserViewHolder(itemCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position)
    {
        User user = users.get(position);
        holder.itemCardBinding.setUser(user);
    }

    @Override
    public int getItemCount()
    {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder
    {
        private ItemCardBinding itemCardBinding;

        public UserViewHolder(ItemCardBinding itemCardBinding)
        {
            super(itemCardBinding.getRoot());
            this.itemCardBinding = itemCardBinding;

            itemCardBinding.getRoot().setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Toast.makeText(
                            view.getContext(), "You clicked " + getAdapterPosition(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }
    }
}
