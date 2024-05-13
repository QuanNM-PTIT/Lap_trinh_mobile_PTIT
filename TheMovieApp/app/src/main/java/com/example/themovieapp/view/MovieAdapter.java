package com.example.themovieapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.themovieapp.MainActivity;
import com.example.themovieapp.R;
import com.example.themovieapp.databinding.MovieListItemBinding;
import com.example.themovieapp.model.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>
{
    private Context context;
    private ArrayList<Movie> movies;

    public MovieAdapter(Context context, ArrayList<Movie> movies)
    {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        MovieListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.movie_list_item,
                parent,
                false
        );
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position)
    {
        Movie movie = movies.get(position);
        holder.movieListItemBinding.setMovie(movie);
    }

    @Override
    public int getItemCount()
    {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
    {
        private MovieListItemBinding movieListItemBinding;

        public MovieViewHolder(@NonNull MovieListItemBinding movieListItemBinding)
        {
            super(movieListItemBinding.getRoot());
            this.movieListItemBinding = movieListItemBinding;

            movieListItemBinding.getRoot().setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                }
            });
        }
    }

}
