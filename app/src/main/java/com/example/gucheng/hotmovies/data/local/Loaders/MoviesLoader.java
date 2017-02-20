package com.example.gucheng.hotmovies.data.local.Loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.gucheng.hotmovies.data.local.Movies;
import com.example.gucheng.hotmovies.data.remote.QueryUtils;

import java.util.List;

/**
 * Created by gucheng on 2017/2/4.
 */

public class MoviesLoader extends AsyncTaskLoader<List<Movies>> {

    private String mUrl;
    private String mLanguage;

    public MoviesLoader(Context context, String url, String language) {
        super(context);
        mUrl = url;
        mLanguage = language;
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
        if(mLanguage == null){
            mLanguage = "zh";   //Should be use system default.
        }
        return QueryUtils.fetchData(mUrl,mLanguage);
    }
}
