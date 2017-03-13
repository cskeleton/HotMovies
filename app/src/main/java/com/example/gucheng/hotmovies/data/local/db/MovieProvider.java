package com.example.gucheng.hotmovies.data.local.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.gucheng.hotmovies.data.local.db.MovieContract.CONTENT_AUTHORITY;
import static com.example.gucheng.hotmovies.data.local.db.MovieContract.MOVIE_PATH;
import static com.example.gucheng.hotmovies.data.local.db.MovieContract.MovieEntry;

/**
 * Created by gucheng on 2017/2/18.
 */

public class MovieProvider extends ContentProvider {

    public DbHelper mMovieDbHelper;

    private static final int MOVIES = 100;
    private static final int MOVIE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY,MOVIE_PATH,MOVIES);
        sUriMatcher.addURI(CONTENT_AUTHORITY,MOVIE_PATH + "/#" ,MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mMovieDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIES:
                cursor = database.query(MovieEntry.TABLE_NAME,null,selection,selectionArgs,
                        sortOrder,null,null);
                break;
            case MOVIE_ID:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MovieEntry.TABLE_NAME,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can't query unknow URI" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIES:
                return MovieEntry.CONTENT_LIST_TYPE;
            case MOVIE_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknow URI" + uri + "with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIES:
                return insertMovie(uri,contentValues);
            case MOVIE_ID:
                throw new IllegalArgumentException("Insertion is not supported for " +  uri);
        }
        return null;
    }

    private Uri insertMovie(Uri uri, ContentValues contentValues) {

        /**
         * All data comes from Internet, users cannot insert data from UI,
         * so check the input is unnecessary?
         *
         * for the moment , I disabled these code for checking the legality.
         */
         /*
         String title = contentValues.getAsString(MovieEntry.COLUNM_MOVIE_ORIGINAL_TITLE);
         if(title == null){
              throw new IllegalArgumentException("Title couldn't be null.");
         }
         */

        SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();
        long id = database.insert(MovieEntry.TABLE_NAME,null,contentValues);
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final String DELETE_ALL = "1";
        SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();
        database.delete(MovieEntry.TABLE_NAME,DELETE_ALL,null);
        Log.v("delete"," Database deleted.");
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
