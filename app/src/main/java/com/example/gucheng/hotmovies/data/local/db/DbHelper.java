package com.example.gucheng.hotmovies.data.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gucheng.hotmovies.data.local.db.MovieContract.MovieEntry;

/**
 * Created by gucheng on 2017/2/17.
 */

public class DbHelper extends SQLiteOpenHelper{
    private final static int TABLE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, MovieEntry.TABLE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_MOVIEDATA_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " ( " +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COLUMN_MOVIE_POPULAR + " INTEGER DEFAULT 0 CHECK (" +
                MovieEntry.COLUMN_MOVIE_POPULAR + "<2)," +
                MovieEntry.COLUMN_MOVIE_HIGH + " INTEGER DEFAULT 0 CHECK (" +
                MovieEntry.COLUMN_MOVIE_HIGH + "<2)," +
                MovieEntry.COLUMN_MOVIE_FAVOURITE + " INTEGER DEFAULT 0 CHECK (" +
                MovieEntry.COLUMN_MOVIE_FAVOURITE + "<2)," +
                MovieEntry.COLUMN_MOVIE_TITLE + " TEXT, " +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE, " +
                MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE + " TEXT, " +
                MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                MovieEntry.COLUMN_MOVIE_DATE + " TEXT, " +
                MovieEntry.COLUMN_MOVIE_LANGUAGE + " TEXT, " +
                MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT, " +
                MovieEntry.COLUMN_MOVIE_SCORE + " REAL, " +
                MovieEntry.COLUMN_MOVIE_RUNTIME + " INTEGER, " +
                MovieEntry.COLUMN_MOVIE_VIDEOS + " TEXT, " +
                MovieEntry.COLUMN_MOVIE_REVIEW + " TEXT" + " );";
        db.execSQL(SQL_CREATE_MOVIEDATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}



