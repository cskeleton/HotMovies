package com.example.gucheng.hotmovies.Fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
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
import android.widget.Toast;

import com.example.gucheng.hotmovies.R;
import com.example.gucheng.hotmovies.activities.EditorActivity;
import com.example.gucheng.hotmovies.data.local.adapter.PosterAdapter;
import com.example.gucheng.hotmovies.data.local.db.MovieContract.MovieEntry;
import com.example.gucheng.hotmovies.data.remote.GetUrl;
import com.example.gucheng.hotmovies.data.remote.QueryMovies;


/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int IMAGE_LOADER = 1;
    private static final String POPULAR = "Popular";
    private static final String TOP_RATED = "Top Rated";
    private static final String FAVOURITE = "Favourite";


    private Toolbar toolbar;
    private TextView mEmptyText;
    private View rootView;
    private GridView gridView;
    private PosterAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar mProgressBar;
    private String mUserLanguage = "en";
    private String requestUrl;
    private String selection = MovieEntry.COLUMN_MOVIE_POPULAR + "=? AND " + MovieEntry.COLUMN_MOVIE_HIGH + "=? " ;
    private static String[] selectionArgs;
    private static LoaderManager.LoaderCallbacks<Cursor>  callback;
    private static int mIsPop;
    private static int mIsHigh;
    private static int mIsFavourite;

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
        requestUrl = GetUrl.getMovieUrl(GetUrl.POP_URL);

        //Toolbar
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(POPULAR);
        callback = this;
        mIsPop = 1;
        mIsHigh = 0;
        mIsFavourite = 0;

        //Progress bar init
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);


        //grid view and gridView.empty view
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

        mAdapter = new PosterAdapter(w_dip,density,gridView,getActivity(),null);
        gridView.setAdapter(mAdapter);


        // swipe refresh
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(mIsFavourite == 1){
                    mIsFavourite = 0;
                }

                ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()){
                    QueryMovies queryMovies = new QueryMovies(getActivity(),mUserLanguage,mIsPop,mIsHigh,mIsFavourite);
                    queryMovies.execute(requestUrl);
                }else {
                    Toast.makeText(getActivity(),"No Internet connect available.",Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(getActivity(),EditorActivity.class);
                Uri currentMovieUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI,id);
                Log.v("Clicked uri", String.valueOf(currentMovieUri));
                intent.setData(currentMovieUri);
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
                toolbar.setTitle(POPULAR);
                mIsPop = 1;
                mIsHigh = 0;
                mIsFavourite = 0;
                selection = MovieEntry.COLUMN_MOVIE_POPULAR + "=? AND " + MovieEntry.COLUMN_MOVIE_HIGH + "=? " ;
                queryDatabase();
                break;
            case R.id.action_rates:
                requestUrl = GetUrl.getMovieUrl(GetUrl.HIGH_URL);
                toolbar.setTitle(TOP_RATED);
                mIsHigh = 1;
                mIsPop = 0;
                mIsFavourite = 0;
                selection = MovieEntry.COLUMN_MOVIE_POPULAR + "=? AND " + MovieEntry.COLUMN_MOVIE_HIGH + "=? " ;
                queryDatabase();
                break;
            case R.id.action_favourite:
                toolbar.setTitle(FAVOURITE);
                mIsFavourite = 1;
                selection = MovieEntry.COLUMN_MOVIE_FAVOURITE + "=?";
                queryDatabase();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void queryDatabase(){

        int i;
        if(toolbar.getTitle().equals(FAVOURITE) ){ i = 2;}
        else if (toolbar.getTitle().equals(TOP_RATED)) { i = 1;}
        else {i = 0;}

        switch (i){
            case 0:
                selectionArgs = new String[]{String.valueOf(1),String.valueOf(0)};
                break;
            case 1:
                selectionArgs = new String[]{String.valueOf(0),String.valueOf(1)};
                break;
            case 2:
                selectionArgs = new String[]{String.valueOf(1)};
                break;
            default:
                selectionArgs = null;
                Log.e("MainFragment 211","Wrong query selection:" + i);
        }
        getLoaderManager().restartLoader(IMAGE_LOADER,null,callback);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = {
                MovieEntry._ID,
                MovieEntry.COLUMN_MOVIE_POPULAR,
                MovieEntry.COLUMN_MOVIE_HIGH,
                MovieEntry.COLUMN_MOVIE_FAVOURITE,
                MovieEntry.COLUMN_MOVIE_TITLE,
                MovieEntry.COLUMN_MOVIE_ID,
                MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE,
                MovieEntry.COLUMN_MOVIE_OVERVIEW,
                MovieEntry.COLUMN_MOVIE_DATE,
                MovieEntry.COLUMN_MOVIE_LANGUAGE,
                MovieEntry.COLUMN_MOVIE_POSTER_PATH,
                MovieEntry.COLUMN_MOVIE_SCORE,
                MovieEntry.COLUMN_MOVIE_RUNTIME,
                MovieEntry.COLUMN_MOVIE_VIDEOS,
                MovieEntry.COLUMN_MOVIE_REVIEW
        };

        return new CursorLoader(getActivity(),MovieEntry.CONTENT_URI,projection,selection,selectionArgs,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mProgressBar.setVisibility(View.GONE);
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(cursor != null){
            mAdapter.swapCursor(cursor);
        }else {
            if(networkInfo != null && networkInfo.isConnected()){
                mEmptyText.setText("No Movie to show.");
            }else {
                mEmptyText.setText("No Internet connect available.");
            }
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
