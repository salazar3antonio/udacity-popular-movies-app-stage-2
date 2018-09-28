package com.studentproject.popluarmoviesstage2;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.studentproject.popluarmoviesstage2.database.models.Movie;
import com.studentproject.popluarmoviesstage2.database.MovieDatabase;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private static final String TAG = MovieViewModel.class.getName();

    private LiveData<List<Movie>> allFavoriteMovies;
    private LiveData<List<Movie>> allPopularMovies;
    private LiveData<List<Movie>> allTopRatedMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        // get instance of the DB
        MovieDatabase movieDatabase = MovieDatabase.getsInstance(application.getApplicationContext());
        Log.i(TAG, "Actively retrieving the tasks from the DataBase");
        //query the DB for all favorite movies in ASC order of Movie Title
        allFavoriteMovies = movieDatabase.movieDao().allFavoriteMovies();
        allPopularMovies = movieDatabase.movieDao().allPopularMovies();
        allTopRatedMovies = movieDatabase.movieDao().allTopRatedMovies();
    }

    public LiveData<List<Movie>> getAllFavoriteMovies() {
        return allFavoriteMovies;
    }

    public LiveData<List<Movie>> getAllPopularMovies() {
        return allPopularMovies;
    }

    public LiveData<List<Movie>> getAllTopRatedMovies() {
        return allTopRatedMovies;
    }
}
