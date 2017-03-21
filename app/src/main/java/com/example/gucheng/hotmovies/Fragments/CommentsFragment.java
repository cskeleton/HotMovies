package com.example.gucheng.hotmovies.Fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gucheng.hotmovies.R;
import com.example.gucheng.hotmovies.data.loaders.ReviewLoader;
import com.example.gucheng.hotmovies.data.local.adapter.ReviewAdapter;
import com.example.gucheng.hotmovies.data.local.beans.Reviews;
import com.example.gucheng.hotmovies.data.local.db.MovieContract.MovieEntry;

import java.util.ArrayList;
import java.util.List;

public class CommentsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Reviews>> {
    private static final int REVIEW_LOADER = 2;

    private View rootCommentsView;
    private ProgressBar mProgressbar;
    private TextView mEmptyText;
    private ReviewAdapter mAdapter;

    private String mReviewUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Intent i = getActivity().getIntent();
        mReviewUrl = i.getStringExtra(MovieEntry.COLUMN_MOVIE_REVIEW);

        rootCommentsView = inflater.inflate(R.layout.fragment_comments, container, false);

        //Toolbar
        Toolbar toolbar = (Toolbar) rootCommentsView.findViewById(R.id.toolbar_comments);
        toolbar.setTitle("Comments");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        mProgressbar = (ProgressBar) rootCommentsView.findViewById(R.id.comments_progress_bar);
        mProgressbar.setVisibility(View.VISIBLE);
        mEmptyText = (TextView) rootCommentsView.findViewById(R.id.review_empty_text);

        ListView listView = (ListView) rootCommentsView.findViewById(R.id.list_comments);
        listView.setEmptyView(mEmptyText);
        mAdapter = new ReviewAdapter(getActivity(),new ArrayList<Reviews>());
        Log.v("ReviewUrl:",mReviewUrl);
        getLoaderManager().initLoader(REVIEW_LOADER,null,this);
        listView.setAdapter(mAdapter);
        return rootCommentsView;
    }

    @Override
    public Loader<List<Reviews>> onCreateLoader(int id, Bundle args) {
        Log.v("Reviews:","Starting Loading reviews.");
        return new ReviewLoader(getActivity(),mReviewUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Reviews>> loader, List<Reviews> data) {
        mProgressbar.setVisibility(View.GONE);
        if(data != null && !data.isEmpty()) {
            mAdapter.clear();
            mAdapter.addAll(data);
        }else {
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()){
                mEmptyText.setText("No Reviews.");
            }else {
                mEmptyText.setText("No Internet connect available.");
            }
        }
        Log.v("Reviews:","load reviews finished.");
    }

    @Override
    public void onLoaderReset(Loader<List<Reviews>> loader) {
        mAdapter.clear();
    }
}
