package com.mal.movieapp.database;

/**
 * Created by Souidan on 9/26/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MovieOperations {
    public static final String LOGTAG = "MVE_MNGMNT_SYS";
    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            MovieDBHandler.COLUMN_ID,
            MovieDBHandler.COLUMN_TITLE,
            MovieDBHandler.COLUMN_MOVIE_ID,
            MovieDBHandler.COLUMN_BACKDROP,
            MovieDBHandler.COLUMN_POSTER_PATH,
            MovieDBHandler.COLUMN_DATE,
            MovieDBHandler.COLUMN_OVERVIEW,
            MovieDBHandler.COLUMN_RATING
    };

    public MovieOperations(Context context) {
        dbhandler = new MovieDBHandler(context);
    }

    public void open() {
        Log.i(LOGTAG, "Database Opened");
        database = dbhandler.getWritableDatabase();


    }

    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();

    }
   /* MovieDBHandler.COLUMN_ID,
    MovieDBHandler.COLUMN_TITLE,
    MovieDBHandler.COLUMN_MOVIE_ID,
    MovieDBHandler.COLUMN_BACKDROP,
    MovieDBHandler.COLUMN_POSTER_PATH,
    MovieDBHandler.COLUMN_DATE,
    MovieDBHandler.COLUMN_OVERVIEW,
    MovieDBHandler.COLUMN_RATING*/

    public Movie addMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieDBHandler.COLUMN_TITLE, movie.getTitle());
        values.put(MovieDBHandler.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieDBHandler.COLUMN_RATING, movie.getRating());
        values.put(MovieDBHandler.COLUMN_DATE, movie.getDate());
        values.put(MovieDBHandler.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MovieDBHandler.COLUMN_BACKDROP, movie.getBackdrop());
        values.put(MovieDBHandler.COLUMN_MOVIE_ID, movie.getMovieId());

        long insertid = database.insert(MovieDBHandler.TABLE_MOVIES, null, values);
        movie.setId(insertid);
        return movie;

    }

    public Movie getMovie(long id) {

        Cursor cursor = database.query(MovieDBHandler.TABLE_MOVIES, allColumns, MovieDBHandler.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() == 0)
            return null;

        Movie movie = new Movie();
        movie.setId(cursor.getLong(cursor.getColumnIndex(MovieDBHandler.COLUMN_ID)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieDBHandler.COLUMN_TITLE)));
        movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieDBHandler.COLUMN_OVERVIEW)));
        movie.setRating(cursor.getDouble(cursor.getColumnIndex(MovieDBHandler.COLUMN_RATING)));
        movie.setMovieId(cursor.getInt(cursor.getColumnIndex(MovieDBHandler.COLUMN_MOVIE_ID)));
        movie.setDate(cursor.getString(cursor.getColumnIndex(MovieDBHandler.COLUMN_DATE)));
        movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieDBHandler.COLUMN_POSTER_PATH)));
        movie.setBackdrop(cursor.getString(cursor.getColumnIndex(MovieDBHandler.COLUMN_BACKDROP)));

        return movie;
    }

    public List<Movie> getAllMovies() {

        Cursor cursor = database.query(MovieDBHandler.TABLE_MOVIES, allColumns, null, null, null, null, null);

        List<Movie> movies = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setId(cursor.getLong(cursor.getColumnIndex(MovieDBHandler.COLUMN_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieDBHandler.COLUMN_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieDBHandler.COLUMN_OVERVIEW)));
                movie.setRating(cursor.getDouble(cursor.getColumnIndex(MovieDBHandler.COLUMN_RATING)));
                movie.setMovieId(cursor.getInt(cursor.getColumnIndex(MovieDBHandler.COLUMN_MOVIE_ID)));
                movie.setDate(cursor.getString(cursor.getColumnIndex(MovieDBHandler.COLUMN_DATE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieDBHandler.COLUMN_POSTER_PATH)));
                movie.setBackdrop(cursor.getString(cursor.getColumnIndex(MovieDBHandler.COLUMN_BACKDROP)));
                movies.add(movie);
            }
        }
        // return All Employees
        return movies;


    }


    public void removeMovie(Movie movie) {

        database.delete(MovieDBHandler.TABLE_MOVIES, MovieDBHandler.COLUMN_MOVIE_ID + "=" + movie.getMovieId(), null);
    }
}



