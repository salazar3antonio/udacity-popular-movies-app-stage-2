package com.studentproject.popluarmoviesstage2.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.studentproject.popluarmoviesstage2.MovieDetailActivity;
import com.studentproject.popluarmoviesstage2.R;
import com.studentproject.popluarmoviesstage2.database.models.Movie;
import com.studentproject.popluarmoviesstage2.utils.NetworkUtils;
import com.studentproject.popluarmoviesstage2.view_holders.MovieViewHolder;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    public static final String MOVIE_DETAILS = "movie_details";

    private List<Movie> mMovies;

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster_list_item, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {

        final Movie movie = mMovies.get(position);

        Uri posterUri = NetworkUtils.buildMoviePosterImageUri(movie.getPoster());
        Picasso.get().load(posterUri).placeholder(R.drawable.poster_place_holder).into(movieViewHolder.mMoviePosterImage);

        movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent to launch detail Activity
                Intent intent = new Intent(view.getContext(), MovieDetailActivity.class);
                intent.putExtra(MOVIE_DETAILS, movie);
                view.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

}



