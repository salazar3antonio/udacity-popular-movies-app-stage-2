package com.studentproject.popluarmoviesstage2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.studentproject.popluarmoviesstage2.database.models.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM favorite_movies WHERE isFavorite=1 ORDER BY title ASC")
    LiveData<List<Movie>> allFavoriteMovies();

    @Query("SELECT * FROM favorite_movies WHERE isPopular=1 ORDER BY title ASC")
    LiveData<List<Movie>> allPopularMovies();

    @Query("SELECT * FROM favorite_movies WHERE isTopRated=1 ORDER BY title ASC")
    LiveData<List<Movie>> allTopRatedMovies();

    @Insert
    void insertListOfMovies(List<Movie> movieList);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

}
