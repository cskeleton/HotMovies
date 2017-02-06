package com.example.gucheng.hotmovies;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by gucheng on 2017/2/4.
 */

class MoviesLoader extends AsyncTaskLoader<List<Movies>> {

    private String mUrl;

    MoviesLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movies> loadInBackground() {
        if (mUrl == null){
            return null;
        }
        return QueryUtils.fetchData(mUrl);
    }
}
