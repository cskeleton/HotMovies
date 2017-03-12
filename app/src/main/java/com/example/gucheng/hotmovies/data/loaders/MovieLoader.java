package com.example.gucheng.hotmovies.data.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.gucheng.hotmovies.data.remote.QueryUtils;

/** Override the loadInBackground in CursorLoader,
 * then call the query method here.
 */
public class MovieLoader extends android.content.CursorLoader {
    private String mUrl;
    private String mLanguage;
    private Context mContext;
    private Uri mUri;
    private String[] mProjection;
    private String mSelection;
    private String[] mSelectionArgs;
    private String mSortOrder;

    public MovieLoader(String url, String language, Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context,uri,projection,selection,selectionArgs,sortOrder);
        mContext = context;
        mLanguage = language;
        mUrl = url;
        mUri = uri;
        mProjection = projection;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mSortOrder = sortOrder;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }



    /**
     * Override this method. So I can request data from Internet, then put into database.
     * After doing this, using cursor to query data from database.
     **/
    @Override
    public Cursor loadInBackground() {
        QueryUtils.fetchMovieData(mUrl,mLanguage,mContext);
        Cursor cursor = super.loadInBackground();
        return cursor;
    }

}
