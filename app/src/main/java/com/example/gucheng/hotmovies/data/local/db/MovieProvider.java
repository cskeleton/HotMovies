package com.example.gucheng.hotmovies.data.local.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
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
                cursor = database.query(MovieEntry.TABLE_NAME,projection,selection,selectionArgs,
                        sortOrder,null,null);
                break;
            case MOVIE_ID:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MovieEntry.TABLE_NAME,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can't query unknown URI" + uri);
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
                throw new IllegalArgumentException("Unknown URI" + uri + "with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIES:
                SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();
                long id = database.insert(MovieEntry.TABLE_NAME,null,contentValues);
                getContext().getContentResolver().notifyChange(uri,null);
                return ContentUris.withAppendedId(uri,id);
            case MOVIE_ID:
                throw new IllegalArgumentException("Insertion is not supported for " +  uri);
        }
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int numInserted = 0;
        int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIES:
                SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();
                database.beginTransaction();
                for(ContentValues cv : values){
                    long newID = database.insertOrThrow(MovieEntry.TABLE_NAME,null,cv);
                    if(newID <= 0){
                        throw new SQLiteAbortException("Failed when bulk insert" + uri);
                    }else {
                        numInserted++;
                    }
                }
                database.setTransactionSuccessful();
                getContext().getContentResolver().notifyChange(uri,null);
                numInserted = values.length;
                database.endTransaction();
                return numInserted;
            default:
                throw new IllegalArgumentException("BulkInsert Error");
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final String DELETE_ALL = "1";
        SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();
        database.delete(MovieEntry.TABLE_NAME,DELETE_ALL,null);
        getContext().getContentResolver().notifyChange(uri,null);
        Log.v("delete"," Database deleted.");
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int mRowsUpdated = 0;
        final SQLiteDatabase database = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIES:
                mRowsUpdated = database.update(MovieEntry.TABLE_NAME,values,selection,selectionArgs);
                Log.v("mRows", String.valueOf(mRowsUpdated));
                if(mRowsUpdated == 0){
                    database.insertWithOnConflict(MovieEntry.TABLE_NAME,null,values,SQLiteDatabase.CONFLICT_IGNORE);
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return mRowsUpdated;
            case MOVIE_ID:
                mRowsUpdated =  database.update(MovieEntry.TABLE_NAME,values,selection,selectionArgs);
                Log.v("Favourites","favourite delete");
                getContext().getContentResolver().notifyChange(uri,null);
                return mRowsUpdated;
            default:
                throw new IllegalArgumentException("Update is not supported for " +  uri);
        }
    }
}
