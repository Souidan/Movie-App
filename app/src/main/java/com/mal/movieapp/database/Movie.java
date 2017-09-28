package com.mal.movieapp.database;


import java.io.Serializable;

/**
 * Created by souidan on 8/27/16.
 */


public class Movie implements Serializable {

    public long id;

    public String title;

    public String overview;

    public double rating;

    public int movieId;

    public String date;

    public String posterPath;

    public String backdrop;


    public Movie() {
        super();
    }

    public Movie(Long id, String title, String overview, double rating, int movieId, String date, String posterPath, String backdrop) {
        super();
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.rating = rating;
        this.movieId = movieId;
        this.date = date;
        this.posterPath = posterPath;
        this.backdrop = backdrop;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}

