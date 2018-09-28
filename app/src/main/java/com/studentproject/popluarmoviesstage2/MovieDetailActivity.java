package com.studentproject.popluarmoviesstage2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.studentproject.popluarmoviesstage2.adapters.MovieAdapter;
import com.studentproject.popluarmoviesstage2.adapters.ReviewAdapter;
import com.studentproject.popluarmoviesstage2.adapters.VideoAdapter;
import com.studentproject.popluarmoviesstage2.database.models.Movie;
import com.studentproject.popluarmoviesstage2.database.MovieDatabase;
import com.studentproject.popluarmoviesstage2.database.models.Review;
import com.studentproject.popluarmoviesstage2.database.models.Video;
import com.studentproject.popluarmoviesstage2.utils.JsonUtils;
import com.studentproject.popluarmoviesstage2.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getName();

    private Movie mMovie;
    private URL mByVideosUrl;
    private URL mByReviewsUrl;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;
    private String mShareMovieVideoUrl;

    @BindView(R.id.rv_videos_list) RecyclerView mVideoRecyclerView;
    @BindView(R.id.rv_reviews_list) RecyclerView mReviewRecyclerView;
    @BindView(R.id.iv_detail_poster) ImageView mMoviePosterDetail;
    @BindView(R.id.tv_title_detail) TextView mTitle;
    @BindView(R.id.tv_release_date_detail) TextView mReleaseDate;
    @BindView(R.id.tv_vote_average_detail) TextView mVoteAverage;
    @BindView(R.id.tv_plot_detail) TextView mPlotSynopsis;
    @BindView(R.id.cb_favorite_detail) CheckBox mFavorite;
    @BindView(R.id.pb_video_list) ProgressBar mVideosProgressBar;
    @BindView(R.id.pb_reviews_list) ProgressBar mReviewsProgressBar;
    @BindView(R.id.tv_empty_reviews) TextView mEmptyReviewsTextView;
    @BindView(R.id.tv_empty_videos) TextView mEmptyVideosTextView;
    @BindView(R.id.btn_share_movie) Button mShareMovieButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            mMovie = bundle.getParcelable(MovieAdapter.MOVIE_DETAILS);

            //create the /videos and /reviews URLs based on the MovieID
            mByVideosUrl = NetworkUtils.buildMovieExtrasUrl(mMovie.getMovieId(), NetworkUtils.BY_VIDEOS_URL);
            mByReviewsUrl = NetworkUtils.buildMovieExtrasUrl(mMovie.getMovieId(), NetworkUtils.BY_REVIEWS_URL);
        }

        //execute the background task to fetch the MovieExtra JSON data for Videos and Reviews
        MovieExtrasAsyncTask movieExtrasAsyncTask = new MovieExtrasAsyncTask();
        //pass in both URLs to fetch JSON
        movieExtrasAsyncTask.execute(mByVideosUrl, mByReviewsUrl);

        Uri posterUri = NetworkUtils.buildMoviePosterImageUri(mMovie.getPoster());
        Picasso.get().load(posterUri).placeholder(R.drawable.poster_place_holder).into(mMoviePosterDetail);

        mTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mVoteAverage.setText(mMovie.getVoteAverage());
        mPlotSynopsis.setText(mMovie.getPlotSynopsis());

        //set check mark if movie is already a user Favorite
        if (mMovie.getIsFavorite() == 1) {
            mFavorite.setChecked(true);
        }

        mVideoAdapter = new VideoAdapter(this);
        mReviewAdapter = new ReviewAdapter(this);

        mVideoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mVideoRecyclerView.setHasFixedSize(true);

        mReviewRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mReviewRecyclerView.setHasFixedSize(true);

        //display progress loading
        showProgressBar(mVideosProgressBar, mVideoRecyclerView);
        showProgressBar(mReviewsProgressBar, mReviewRecyclerView);

        mFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mMovie.setIsFavorite(1);
                    executeUpdateToDB();
                    Log.i(TAG, "UPDATED Movie as Favorite " + mMovie.getTitle());
                } else {
                    mMovie.setIsFavorite(0);
                    executeUpdateToDB();
                    Log.i(TAG, "UPDATED Movie as NOT Favorite " + mMovie.getTitle());
                }
            }
        });

        mShareMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareMovieUrlIntent();

            }
        });

    }

    //this method executes the Update method of the DB on a runnable task
    public void executeUpdateToDB() {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                MovieDatabase movieDatabase = MovieDatabase.getsInstance(getApplicationContext());
                movieDatabase.movieDao().updateMovie(mMovie);
            }
        });

    }

    public void showProgressBar(ProgressBar progressBar, RecyclerView recyclerView) {

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

    }

    public void showRecyclerView(ProgressBar progressBar, RecyclerView recyclerView) {

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

    }

    public void showEmptyAdapterTextView(TextView textView, ProgressBar progressBar, int text) {

        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(text);

    }

    public void shareMovieUrlIntent() {

        Intent shareMovieIntent = new Intent(Intent.ACTION_SEND);
        shareMovieIntent.setType("text/plain");
        shareMovieIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_movie_intent_text, mShareMovieVideoUrl));
        startActivity(shareMovieIntent);

    }

    public class MovieExtrasAsyncTask extends AsyncTask<URL, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(URL... urls) {

            URL videoUrl = urls[0];
            URL reviewsUrl = urls[1];

            ArrayList<String> queryResults = new ArrayList<>();

            try {
                String videoResults = NetworkUtils.getResponseFromHttpUrl(videoUrl);
                String reviewResults = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);

                queryResults.add(videoResults);
                queryResults.add(reviewResults);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return queryResults;

        }

        @Override
        protected void onPostExecute(ArrayList<String> movieExtras) {
            super.onPostExecute(movieExtras);

            try {

                List<Video> movieVideos = JsonUtils.getVideosFromJson(movieExtras.get(0));
                List<Review> movieReviews = JsonUtils.getReviewsFromJson(movieExtras.get(1));

                //check to see if there are videos in the list and create the first youtube url
                if (movieVideos.size() > 0) {
                    Video firstVideo = movieVideos.get(0);
                    mShareMovieVideoUrl = NetworkUtils.YOUTUBE_BASE_URL + firstVideo.getKey();
                }

                mVideoAdapter.setVideos(movieVideos);
                mVideoRecyclerView.setAdapter(mVideoAdapter);
                if (mVideoAdapter.getItemCount() == 0) {
                    showEmptyAdapterTextView(mEmptyVideosTextView, mVideosProgressBar, R.string.no_videos_in_list);
                } else {
                    showRecyclerView(mVideosProgressBar, mVideoRecyclerView);
                }


                mReviewAdapter.setReviews(movieReviews);
                mReviewRecyclerView.setAdapter(mReviewAdapter);
                if (mReviewAdapter.getItemCount() == 0) {
                    showEmptyAdapterTextView(mEmptyReviewsTextView, mReviewsProgressBar, R.string.no_reviews_in_list);
                } else {
                    showRecyclerView(mReviewsProgressBar, mReviewRecyclerView);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
