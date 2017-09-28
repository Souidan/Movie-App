package com.mal.movieapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Souidan on 9/26/2017.
 */

public class MovieDBHandler extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_MOVIES = "movies";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_POSTER_PATH = "posterPath";
    public static final String COLUMN_BACKDROP = "backDrop";
    public static final String COLUMN_MOVIE_ID = "movieId";
    public static final String[] ALL_COLUMNS =
            {COLUMN_ID ,
                    COLUMN_TITLE ,
                    COLUMN_OVERVIEW ,
                    COLUMN_RATING ,
                    COLUMN_MOVIE_ID ,
                    COLUMN_DATE ,
                    COLUMN_POSTER_PATH ,
                    COLUMN_BACKDROP };

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_MOVIES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_OVERVIEW + " TEXT, " +
                    COLUMN_RATING + " NUMERIC, " +
                    COLUMN_MOVIE_ID + " NUMERIC, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_POSTER_PATH + " TEXT ," +
                    COLUMN_BACKDROP + " TEXT " +
                    ")";


    public MovieDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        sqLiteDatabase.execSQL(TABLE_CREATE);

    }
}
