package com.example.gucheng.hotmovies.data.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

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
    private static Boolean isFetch = false;
    private static Cursor cursor;

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
        Log.v("onStartLoading","onStartLoading");
        forceLoad();
    }



    /**
     * Override this method. So I can request data from Internet, then put into database.
     * After doing this, using cursor to query data from database.
     **/
    @Override
    public Cursor loadInBackground() {
        Log.v("background","loading in bg");
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected() && isFetch){
            QueryUtils.fetchMovieData(mUrl,mLanguage,mContext);
            isFetch = false;
        }
        cursor = super.loadInBackground();
        return cursor;
    }

    public static boolean isFetch(Boolean b){
        isFetch = b;
        return isFetch;
    }

}
