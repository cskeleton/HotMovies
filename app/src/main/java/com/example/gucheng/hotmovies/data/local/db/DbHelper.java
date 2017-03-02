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
                MovieEntry.COLUNM_MOVIE_TITLE + " TEXT, " +
                MovieEntry.COLUNM_MOVIE_ID + " INTEGER, " +
                MovieEntry.COLUNM_MOVIE_ORIGINAL_TITLE + " TEXT, " +
                MovieEntry.COLUNM_MOVIE_OVERVIEW + " TEXT, " +
                MovieEntry.COLUNM_MOVIE_DATE + " TEXT, " +
                MovieEntry.COLUNM_MOVIE_LANGUAGE + " TEXT, " +
                MovieEntry.COLUNM_MOVIE_POSTER_PATH + " TEXT, " +
                MovieEntry.COLUNM_MOVIE_SCORE + " REAL, " +
                MovieEntry.COLUNM_MOVIE_RUNTIME + " INTEGER, " +
                MovieEntry.COLUNM_MOVIE_VIDEOS + " TEXT, " +
                MovieEntry.COLUNM_MOVIE_REVIEW + " TEXT" + " );";
        db.execSQL(SQL_CREATE_MOVIEDATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}



