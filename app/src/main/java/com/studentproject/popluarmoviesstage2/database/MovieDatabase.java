package com.studentproject.popluarmoviesstage2.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.studentproject.popluarmoviesstage2.database.models.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase{

    private static final String TAG = MovieDatabase.class.getName();

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movie_db";
    private static MovieDatabase sInstance;

    //singleton to get an instance of the database
    public static MovieDatabase getsInstance(Context context) {

        if (sInstance == null) {
            synchronized (LOCK) {
                Log.i(TAG, "Movie DB created");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                        .build();
            }
        }

        Log.i(TAG, "Movie Instance returned");
        return sInstance;
    }

    public abstract MovieDao movieDao();



}
