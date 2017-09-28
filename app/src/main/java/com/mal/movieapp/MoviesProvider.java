package com.mal.movieapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import com.mal.movieapp.database.MovieDBHandler;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Souidan on 9/28/2017.
 */

public class MoviesProvider extends ContentProvider {

    private static final String AUTHORITY = "com.mal.movieapp";
    private static final String BASE_PATH = "movies";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );
    private static final int MOVIES = 100;
    private static final int MOVIE_ID = 101;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY,BASE_PATH, MOVIES);
        uriMatcher.addURI(AUTHORITY,BASE_PATH + "/#",MOVIE_ID);

    }

    private SQLiteDatabase database;



    @Override
    public boolean onCreate() {
        MovieDBHandler helper = new MovieDBHandler(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        System.out.println(uri);
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                cursor =  database.query(MovieDBHandler.TABLE_MOVIES,MovieDBHandler.ALL_COLUMNS,
                        s,null,null,null,MovieDBHandler.COLUMN_MOVIE_ID +" ASC");
                break;
            case MOVIE_ID:
                final String path = uri.getPath();
                String id =path.substring(path.lastIndexOf('/') + 1);
                cursor = database.query(MovieDBHandler.TABLE_MOVIES,MovieDBHandler.ALL_COLUMNS,MovieDBHandler.COLUMN_MOVIE_ID+"=?"
                ,new String[]{id},null,null,null,null);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {


        long id = database.insert(MovieDBHandler.TABLE_MOVIES,null,contentValues);

        if (id > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Insertion Failed for URI :" + uri);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int delCount = 0;
        switch (uriMatcher.match(uri)) {
            case MOVIE_ID:
                final String path = uri.getPath();
                String id =path.substring(path.lastIndexOf('/') + 1);
                delCount =  database.delete(MovieDBHandler.TABLE_MOVIES,MovieDBHandler.COLUMN_MOVIE_ID+"="+id,null);;
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int updCount = 0;
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                updCount =  database.update(MovieDBHandler.TABLE_MOVIES,contentValues,s,strings);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updCount;
    }
    }

