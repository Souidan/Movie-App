package com.mal.movieapp.database;

/**
 * Created by Souidan on 9/26/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MovieOperations {
    public static final String LOGTAG = "MVE_MNGMNT_SYS";
    SQLiteOpenHelper dbhandler;

    private static final String AUTHORITY = "com.mal.movieapp";
    private static final String BASE_PATH = "movies";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );
    private static final int MOVIES = 100;
    private static final int MOVIE_ID = 101;
    Context context;


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
        this.context=context;
        dbhandler = new MovieDBHandler(context);
    }


   /* MovieDBHandler.COLUMN_ID,
    MovieDBHandler.COLUMN_TITLE,
    MovieDBHandler.COLUMN_MOVIE_ID,
    MovieDBHandler.COLUMN_BACKDROP,
    MovieDBHandler.COLUMN_POSTER_PATH,
    MovieDBHandler.COLUMN_DATE,
    MovieDBHandler.COLUMN_OVERVIEW,
    MovieDBHandler.COLUMN_RATING*/

    public Uri addMovie(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieDBHandler.COLUMN_TITLE, movie.getTitle());
        values.put(MovieDBHandler.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieDBHandler.COLUMN_RATING, movie.getRating());
        values.put(MovieDBHandler.COLUMN_DATE, movie.getDate());
        values.put(MovieDBHandler.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(MovieDBHandler.COLUMN_BACKDROP, movie.getBackdrop());
        values.put(MovieDBHandler.COLUMN_MOVIE_ID, movie.getMovieId());

       Uri uri= context.getContentResolver().insert(CONTENT_URI,values);



        return uri;

    }

    public Movie getMovie(long id) {

        Uri uri = Uri.withAppendedPath(CONTENT_URI,""+id);
        Cursor cursor = context.getContentResolver().query(uri, allColumns, null, null,  null);
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

        Cursor cursor = context.getContentResolver().query(CONTENT_URI, allColumns, null, null,  null);

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
        Uri uri = Uri.withAppendedPath(CONTENT_URI,""+movie.getMovieId());

        context.getContentResolver().delete(uri,""+ movie.getMovieId(), null);
    }
}



