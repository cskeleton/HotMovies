package com.example.gucheng.hotmovies.data.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.gucheng.hotmovies.data.local.beans.Reviews;
import com.example.gucheng.hotmovies.data.remote.QueryUtils;

import java.util.List;

/**
 * Created by gucheng on 2017/3/12.
 */

public class ReviewLoader extends AsyncTaskLoader<List<Reviews>> {
    private String mUrl;

    public ReviewLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Reviews> loadInBackground() {
        if(mUrl == null){return null;}
        return QueryUtils.fetchReviewData(mUrl);
    }
}
