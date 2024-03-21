package com.example.sportsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>
{
    List<Sport> sports;

    public CustomAdapter(List<Sport> sports)
    {
        this.sports = sports;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        Sport sport = sports.get(position);
        holder.imageView.setImageResource(sport.getSportImage());
        holder.textView.setText(sport.getSportName());
    }

    @Override
    public int getItemCount()
    {
        return sports.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView;
        public TextView textView;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewCard);
            textView = itemView.findViewById(R.id.title);
        }
    }
}
