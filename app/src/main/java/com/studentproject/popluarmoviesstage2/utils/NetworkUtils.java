package com.studentproject.popluarmoviesstage2.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.content.Context.CONNECTIVITY_SERVICE;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    // TODO: 9/27/2018 Place in User API Key
    private static final String USER_API_KEY = "";

    //Base URLs
    public static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie";
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    //URL appends to the Movie base url
    public static final String BY_POPULAR_URL = "popular";
    public static final String BY_TOP_RATED_URL = "top_rated";
    public static final String BY_VIDEOS_URL = "videos";
    public static final String BY_REVIEWS_URL = "reviews";

    //Movie API parameters
    private static final String API_KEY_PARAM = "api_key";
    private static final String LANGUAGE_PARAM = "language";
    private static final String PAGE_PARAM = "page";

    //Optional API parameters
    private static final int pageNum = 1;
    private static final String languageType = "en-US";

    //Image API parameters
    private static final String IMAGE_SIZE = "w185";

    public static URL buildMovieUrl(String sortBy) {

        URL url = null;

        Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(API_KEY_PARAM, USER_API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, languageType)
                .appendQueryParameter(PAGE_PARAM, Integer.toString(pageNum))
                .build();

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "buildMovieUrl URL ERROR: " + e.getMessage());
        }

        return url;

    }

    public static URL buildMovieExtrasUrl(String movieId, String byExtra) {

        URL url = null;

        Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(byExtra)
                .appendQueryParameter(API_KEY_PARAM, USER_API_KEY)
                .build();

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "buildMovieExtras URL ERROR: " + e.getMessage());
        }

        return url;

    }

    public static Uri buildMoviePosterImageUri(String imagePath) {

        String editedImagePath = imagePath.substring(1);

        return Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendPath(editedImagePath)
                .build();

    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isNetworkAvailableAndConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = connectivityManager.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && connectivityManager.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }


}
