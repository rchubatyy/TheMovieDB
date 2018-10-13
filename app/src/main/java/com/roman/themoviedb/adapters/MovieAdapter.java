package com.roman.themoviedb.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.roman.themoviedb.models.Movie;
import com.roman.themoviedb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Roman on 07/04/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{


    private ArrayList<Movie> movies;
    private Context context;
    private OnPosterClickedListener listener;

    public MovieAdapter(final Context context, ArrayList<Movie> movies, OnPosterClickedListener listener){
        this.context=context;
        this.movies=movies;
        this.listener=listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poster_layout, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        new ImageLoader(holder, movie).run();
    }

    @Override
    public int getItemCount() {
        return movies!=null ? movies.size() : 0;
    }


    protected class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView poster;

        MovieViewHolder(View view){
            super(view);
            poster=view.findViewById(R.id.poster);
            poster.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            listener.onPosterClicked(getAdapterPosition());
        }
    }

    private class ImageLoader implements Runnable{
        private MovieViewHolder holder;
        private Movie movie;

        ImageLoader(MovieViewHolder holder, Movie movie){
            this.holder = holder;
            this.movie = movie;
        }
        @Override
        public void run() {
            try {
                Picasso.with(context)
                        .load(movie.getPosterURL())
                        .placeholder(R.drawable.poster_placeholder)
                        .error(R.drawable.poster_placeholder)
                        .into(holder.poster);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public interface OnPosterClickedListener{
        void onPosterClicked(int position);
    }
}
