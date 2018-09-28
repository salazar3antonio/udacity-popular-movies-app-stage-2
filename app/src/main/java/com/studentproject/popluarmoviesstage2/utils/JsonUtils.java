package com.studentproject.popluarmoviesstage2.utils;


import com.studentproject.popluarmoviesstage2.database.models.Movie;
import com.studentproject.popluarmoviesstage2.database.models.Review;
import com.studentproject.popluarmoviesstage2.database.models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    //Movie JSON Keys
    private static final String RESULTS_KEY = "results";
    private static final String MOVIE_ID_KEY = "id";
    private static final String TITLE_KEY = "title";
    private static final String RELEASE_DATE_KEY = "release_date";
    private static final String POSTER_KEY = "poster_path";
    private static final String VOTE_AVERAGE_KEY = "vote_average";
    private static final String PLOT_SYNOPSIS_KEY = "overview";

    //Video JSON Keys
    private static final String VIDEO_KEY_KEY = "key";
    private static final String VIDEO_NAME_KEY = "name";
    private static final String VIDEO_TYPE_KEY = "type";

    //Review JSON Keys
    private static final String REVIEW_URL_KEY = "url";
    private static final String REVIEW_AUTHOR_KEY = "author";
    private static final String REVIEW_CONTENT_KEY = "content";

    public static List<Movie> getMoviesFromJson(String jsonResponse, int isPopular, int isTopRated) throws JSONException{

        List<Movie> movies = new ArrayList<>();

        JSONObject topLevelJsonObject = new JSONObject(jsonResponse);
        JSONArray resultsJsonArray = topLevelJsonObject.getJSONArray(RESULTS_KEY);

        for (int i = 0; i < resultsJsonArray.length(); i++) {

            JSONObject movieJsonObject = resultsJsonArray.getJSONObject(i);

            String movieId = movieJsonObject.optString(MOVIE_ID_KEY);
            String title = movieJsonObject.optString(TITLE_KEY);
            String releaseDate = movieJsonObject.optString(RELEASE_DATE_KEY);
            String poster = movieJsonObject.optString(POSTER_KEY);
            String voteAverage = movieJsonObject.optString(VOTE_AVERAGE_KEY);
            String plotSynopsis = movieJsonObject.optString(PLOT_SYNOPSIS_KEY);

            Movie movie = new Movie(movieId, title, releaseDate, poster, voteAverage, plotSynopsis);
            movie.setIsPopular(isPopular);
            movie.setIsTopRated(isTopRated);

            movies.add(i, movie);

        }

        return movies;

    }

    public static List<Video> getVideosFromJson(String jsonResponse) throws JSONException {

        List<Video> videos = new ArrayList<>();

        JSONObject topLevelJsonObject = new JSONObject(jsonResponse);
        JSONArray resultsJsonArray = topLevelJsonObject.getJSONArray(RESULTS_KEY);

        for (int i = 0; i < resultsJsonArray.length(); i++) {

            JSONObject videoJsonObject = resultsJsonArray.getJSONObject(i);

            String videoKey = videoJsonObject.optString(VIDEO_KEY_KEY);
            String videoName = videoJsonObject.optString(VIDEO_NAME_KEY);
            String videoType = videoJsonObject.optString(VIDEO_TYPE_KEY);

            Video video = new Video(videoKey, videoName, videoType);
            videos.add(i, video);

        }

        return videos;

    }

    public static List<Review> getReviewsFromJson(String jsonResponse) throws JSONException {

        List<Review> reviews = new ArrayList<>();

        JSONObject topLevelJsonObject = new JSONObject(jsonResponse);
        JSONArray resultsJsonArray = topLevelJsonObject.getJSONArray(RESULTS_KEY);

        for (int i = 0; i < resultsJsonArray.length(); i++) {

            JSONObject reviewJsonObject = resultsJsonArray.getJSONObject(i);

            String reviewAuthor = reviewJsonObject.optString(REVIEW_AUTHOR_KEY);
            String reviewContent = reviewJsonObject.optString(REVIEW_CONTENT_KEY);
            String reviewUrl = reviewJsonObject.optString(REVIEW_URL_KEY);

            Review review = new Review(reviewAuthor, reviewContent, reviewUrl);
            reviews.add(i, review);

        }

        return reviews;
    }

}
