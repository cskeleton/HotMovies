package com.example.gucheng.hotmovies.data.remote;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by gucheng on 2017/3/16.
 */

public class QueryMovies extends AsyncTask<String,Void,Void> {
    private Context mContext;
    private int mIsPopular;
    private int mIsHigh;
    private int mIsFavourite;

    public QueryMovies(Context context, int isPopular, int isHigh, int isFavourite){
        mContext = context;
        mIsPopular = isPopular;
        mIsHigh = isHigh;
        mIsFavourite = isFavourite;
    }

    @Override
    protected Void doInBackground(String... urls) {
        QueryUtils.fetchMovieData(urls[0],mContext,mIsPopular,mIsHigh, mIsFavourite);
        return null;
    }
}
