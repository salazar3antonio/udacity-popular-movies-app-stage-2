package com.studentproject.popluarmoviesstage2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.studentproject.popluarmoviesstage2.adapters.MovieAdapter;
import com.studentproject.popluarmoviesstage2.database.models.Movie;
import com.studentproject.popluarmoviesstage2.database.MovieDatabase;
import com.studentproject.popluarmoviesstage2.utils.JsonUtils;
import com.studentproject.popluarmoviesstage2.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();

    private static final String PREF_SORT_BY_KEY = "pref_sort_by_key";
    private static final int PREF_POPULAR_VALUE = 1;
    private static final int PREF_TOP_RATED_VALUE = 2;
    private static final int PREF_FAVORITE_VALUE = 3;

    private static final String PREF_JSON_FETCH_SUCCESS_KEY = "pref_json_fetch_key";

    @BindView(R.id.rv_movies_list) RecyclerView mRecyclerView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    MovieAdapter mMovieAdapter;
    SharedPreferences mSharedPreferences;
    ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mActionBar = getSupportActionBar();
        mSharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        URL byPopularUrl = NetworkUtils.buildMovieUrl(NetworkUtils.BY_POPULAR_URL);
        URL byTopRatedUrl = NetworkUtils.buildMovieUrl(NetworkUtils.BY_TOP_RATED_URL);

        showProgressBar();

        if (NetworkUtils.isNetworkAvailableAndConnected(this)) {
            if (mSharedPreferences.getBoolean(PREF_JSON_FETCH_SUCCESS_KEY, false)) {
                Log.i(TAG, "JSON fetch successful.");
                //set RecyclerView depending on user preferences
                switch (mSharedPreferences.getInt(PREF_SORT_BY_KEY, 1)) {
                    case 1:
                        setupPopularMovieViewModel();
                        showRecyclerView();
                        updateActionBarTitle(1);
                        break;
                    case 2:
                        setupTopRatedMovieViewModel();
                        showRecyclerView();
                        updateActionBarTitle(2);
                        break;
                    case 3:
                        setupFavoriteMovieViewModel();
                        showRecyclerView();
                        updateActionBarTitle(3);
                }
            } else {
                //execute the MovieAsyncTask to fetch and parse the JSON Movie data.
                //this should only be called once. Unless the user wants to manually refresh the data.
                MovieAsyncTask movieAsyncTask = new MovieAsyncTask();
                movieAsyncTask.execute(byPopularUrl, byTopRatedUrl);
            }
        } else {
            Toast.makeText(this, "No internet connection found.", Toast.LENGTH_LONG).show();
        }

        //init the Adapter
        mMovieAdapter = new MovieAdapter();

        //setup the RecyclerView layout
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);

    }

    public class MovieAsyncTask extends AsyncTask<URL, Void, ArrayList<String>> {


        @Override
        protected ArrayList<String> doInBackground(URL... urls) {

            //pass in two URLs, on for poplar and topRated movies
            URL popularUrl = urls[0];
            URL topRatedUrl = urls[1];

            String popularResults;
            String topRatedResults;

            ArrayList<String> queryResults = new ArrayList<>();

            try {

                popularResults = NetworkUtils.getResponseFromHttpUrl(popularUrl);
                topRatedResults = NetworkUtils.getResponseFromHttpUrl(topRatedUrl);

                queryResults.add(popularResults);
                queryResults.add(topRatedResults);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return queryResults;
        }

        @Override
        protected void onPostExecute(ArrayList<String> movieJsonResults) {

            try {
                if (movieJsonResults.size() == 0) {
                    Toast.makeText(MainActivity.this, "No User Api Key found. " +
                            "\n Enter User Api Key in NetworkUtils.class", Toast.LENGTH_LONG).show();
                } else {
                    //get List of Movie Objects from the JSON data
                    //set which movies are Popular and Top Rated. This way we can query DB by isPopular or isTopRated
                    List<Movie> popularJsonMovies = JsonUtils.getMoviesFromJson(movieJsonResults.get(0), 1, 0);
                    List<Movie> topRatedJsonMovies = JsonUtils.getMoviesFromJson(movieJsonResults.get(1), 0, 1);

                    //combine both Popular and Top Rated Movies so they can all be Inserted into the DB at once
                    final List<Movie> allJsonMovies = new ArrayList<>(popularJsonMovies);
                    allJsonMovies.addAll(topRatedJsonMovies);

                    //execute background task to insert all Movies to DB
                    executeInsertMoviesToDb(allJsonMovies);

                    //update user preference upon successful JSON fetch and parse
                    mSharedPreferences.edit().putBoolean(PREF_JSON_FETCH_SUCCESS_KEY, true).apply();

                    //set the ViewModel depending on the user Preference
                    switch (mSharedPreferences.getInt(PREF_SORT_BY_KEY, 1)) {
                        case 1:
                            setupPopularMovieViewModel();
                            break;
                        case 2:
                            setupTopRatedMovieViewModel();
                            break;
                        case 3:
                            setupFavoriteMovieViewModel();
                    }

                    //remove the Progress Bar and show the RecyclerView
                    showRecyclerView();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.popular_menu_item:
                mSharedPreferences.edit().putInt(PREF_SORT_BY_KEY, PREF_POPULAR_VALUE).apply();
                setupPopularMovieViewModel();
                updateActionBarTitle(1);
                return true;
            case R.id.top_rated_menu_item:
                mSharedPreferences.edit().putInt(PREF_SORT_BY_KEY, PREF_TOP_RATED_VALUE).apply();
                setupTopRatedMovieViewModel();
                updateActionBarTitle(2);
                return true;
            case R.id.favorites_menu_item:
                mSharedPreferences.edit().putInt(PREF_SORT_BY_KEY, PREF_FAVORITE_VALUE).apply();
                setupFavoriteMovieViewModel();
                updateActionBarTitle(3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    public void showRecyclerView() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void updateActionBarTitle(int userPreference) {

        switch (userPreference) {
            case 1:
                mActionBar.setTitle(getString(R.string.popular_actionbar_title));
                break;
            case 2:
                mActionBar.setTitle(getString(R.string.top_rated_actionbar_title));
                break;
            case 3:
                mActionBar.setTitle(getString(R.string.favorites_actionbar_title));
                break;
        }


    }

    public void setupFavoriteMovieViewModel() {

        MovieViewModel favMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        favMovieViewModel.getAllFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMovieAdapter.setMovies(movies);
                mRecyclerView.setAdapter(mMovieAdapter);
            }
        });

    }

    public void setupPopularMovieViewModel() {

        MovieViewModel popMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        popMovieViewModel.getAllPopularMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMovieAdapter.setMovies(movies);
                mRecyclerView.setAdapter(mMovieAdapter);
            }
        });

    }

    public void setupTopRatedMovieViewModel() {

        MovieViewModel topMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        topMovieViewModel.getAllTopRatedMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMovieAdapter.setMovies(movies);
                mRecyclerView.setAdapter(mMovieAdapter);
            }
        });

    }

    public void executeInsertMoviesToDb(final List<Movie> listOfMovies) {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                MovieDatabase movieDatabase = MovieDatabase.getsInstance(getApplicationContext());
                movieDatabase.movieDao().insertListOfMovies(listOfMovies);
            }
        });


    }


}
