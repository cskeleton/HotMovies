package com.example.gucheng.hotmovies.data.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gucheng on 2017/2/17.
 */

public class DbHelper extends SQLiteOpenHelper{
    private final static int TABLE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, MovieContract.MovieEntry.TABLE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_MOVIEDATA_TABLE = "CREATE TABLE" + MovieContract.MovieEntry.TABLE_NAME + "(" +
                MovieContract.MovieEntry._ID + "INTEGER PRIMARY KEY," +
                MovieContract.MovieEntry.COLUNM_MOVIE_TITLE + "TEXT NOT NULL," +
                MovieContract.MovieEntry.COLUNM_MOVIE_ID + "INTEGER NOT NULL," +
                MovieContract.MovieEntry.COLUNM_MOVIE_ORIGINAL_TITLE + "TEXT," +
                MovieContract.MovieEntry.COLUNM_MOVIE_OVERVIEW + "TEXT," +
                MovieContract.MovieEntry.COLUNM_MOVIE_DATE + "TEXT," +
                MovieContract.MovieEntry.COLUNM_MOVIE_LANGUAGE + "TEXT," +
                MovieContract.MovieEntry.COLUNM_MOVIE_POSTER_PATH + "TEXT," +
                MovieContract.MovieEntry.COLUNM_MOVIE_SCORE + "INTEGER, " +
                MovieContract.MovieEntry.COLUNM_MOVIE_RUNTIME + "INTEGER," +
                MovieContract.MovieEntry.COLUNM_MOVIE_VIDEOS + "TEXT," +
                MovieContract.MovieEntry.COLUNM_MOVIE_REVIEW + "TEXT," + ");";
        db.execSQL(SQL_CREATE_MOVIEDATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}



