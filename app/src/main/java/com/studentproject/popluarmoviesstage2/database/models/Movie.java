package com.studentproject.popluarmoviesstage2.database.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity (tableName = "favorite_movies")
public class Movie implements Parcelable {

    @PrimaryKey (autoGenerate = true)
    private int id;

    private String movieId;
    private String title;
    private String releaseDate;
    private String poster;
    private String voteAverage;
    private String plotSynopsis;
    private int isFavorite = 0;
    private int isPopular = 0;
    private int isTopRated = 0;

    @Ignore
    public Movie(String movieId, String title, String releaseDate, String poster, String voteAverage, String plotSynopsis) {
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }

    @Ignore
    public Movie(Parcel in) {

        id = in.readInt();
        movieId = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        poster = in.readString();
        voteAverage = in.readString();
        plotSynopsis = in.readString();
        isFavorite = in.readInt();
        isPopular = in.readInt();
        isTopRated = in.readInt();

    }

    // this is the constructor that Room will use
    public Movie(int id, String movieId, String title,
                 String releaseDate, String poster, String voteAverage, String plotSynopsis,
                 int isFavorite, int isPopular, int isTopRated) {
        this.id = id;
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
        this.isFavorite = isFavorite;
        this.isPopular = isPopular;
        this.isTopRated = isTopRated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public int getIsPopular() {
        return isPopular;
    }

    public void setIsPopular(int isPopular) {
        this.isPopular = isPopular;
    }

    public int getIsTopRated() {
        return isTopRated;
    }

    public void setIsTopRated(int isTopRated) {
        this.isTopRated = isTopRated;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(movieId);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(poster);
        parcel.writeString(voteAverage);
        parcel.writeString(plotSynopsis);
        parcel.writeInt(isFavorite);
        parcel.writeInt(isPopular);
        parcel.writeInt(isTopRated);

    }
}
