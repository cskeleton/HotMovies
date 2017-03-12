package com.example.gucheng.hotmovies.Fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gucheng.hotmovies.R;
import com.example.gucheng.hotmovies.activities.EditorActivity;
import com.example.gucheng.hotmovies.data.loaders.MovieLoader;
import com.example.gucheng.hotmovies.data.local.adapter.PosterAdapter;
import com.example.gucheng.hotmovies.data.local.db.MovieContract.MovieEntry;
import com.example.gucheng.hotmovies.data.remote.GetUrl;


/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int IMAGE_LOADER = 1;

    private Toolbar toolbar;
    private TextView mEmptyText;
    private View rootView;
    private GridView gridView;
    private PosterAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar mProgressBar;
    private LoaderManager.LoaderCallbacks callback;
    private String mUserLanguage = "en";
    private String requestUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_main,container,false);

        /*
        Temporary set default language here.
        It should read the user's system default and set in settings.
        */
        GetUrl.setLanguage(mUserLanguage);

        //Set default url.
        requestUrl = GetUrl.getMovieUrl(GetUrl.HIGH_URL);

        //Toolbar
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Popular");

        //Progress bar init
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);


        //grid view and gridview.empty view
        gridView = (GridView) rootView.findViewById(R.id.grid_view);
        mEmptyText = (TextView) rootView.findViewById(R.id.empty_text);
        gridView.setEmptyView(mEmptyText);

        DisplayMetrics displayMetrics = rootView.getResources().getDisplayMetrics();
        int w_px = displayMetrics.widthPixels;
        float density = displayMetrics.density;
        int w_dip = (int) (w_px/density);

        //LoaderManager and LoaderCallbacks.
        final LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(IMAGE_LOADER,null,this);
        callback = this;

        mAdapter = new PosterAdapter(w_dip,density,gridView,getActivity(),null);
        gridView.setAdapter(mAdapter);


        // swipe refresh
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Clear database

                getActivity().getContentResolver().delete(MovieEntry.CONTENT_URI,null,null);
                //Get the data from Internet again.
                loaderManager.restartLoader(1,null,callback);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(getActivity(),EditorActivity.class);
                //TODO: open the detail page here.
                Uri currentMovieUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI,id);
                intent.setData(currentMovieUri);
                Log.v("Position:", String.valueOf(id));
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_popular:
                requestUrl = GetUrl.getMovieUrl(GetUrl.POP_URL);
                toolbar.setTitle("Popular");
                getLoaderManager().restartLoader(IMAGE_LOADER,null,this);
                break;
            case R.id.action_rates:
                requestUrl = GetUrl.getMovieUrl(GetUrl.HIGH_URL);
                toolbar.setTitle("Top rated");
                getLoaderManager().restartLoader(IMAGE_LOADER,null,this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = {
                MovieEntry._ID + "INTEGER PRIMARY KEY," +
                        MovieEntry.COLUNM_MOVIE_TITLE,
                        MovieEntry.COLUNM_MOVIE_ID,
                        MovieEntry.COLUNM_MOVIE_ORIGINAL_TITLE,
                        MovieEntry.COLUNM_MOVIE_OVERVIEW,
                        MovieEntry.COLUNM_MOVIE_DATE,
                        MovieEntry.COLUNM_MOVIE_LANGUAGE,
                        MovieEntry.COLUNM_MOVIE_POSTER_PATH,
                        MovieEntry.COLUNM_MOVIE_SCORE,
                        MovieEntry.COLUNM_MOVIE_RUNTIME,
                        MovieEntry.COLUNM_MOVIE_VIDEOS,
                        MovieEntry.COLUNM_MOVIE_REVIEW
        };
        return new MovieLoader(requestUrl,mUserLanguage,getActivity(), MovieEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mProgressBar.setVisibility(View.GONE);
        if(cursor != null){
            mAdapter.swapCursor(cursor);
        }else {
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()){
                mEmptyText.setText("No Movie to show.");
            }else {
                mEmptyText.setText("No Internet connect available.");
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
