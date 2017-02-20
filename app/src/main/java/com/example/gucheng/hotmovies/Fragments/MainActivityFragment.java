package com.example.gucheng.hotmovies.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.example.gucheng.hotmovies.Activities.EditorActivity;
import com.example.gucheng.hotmovies.R;
import com.example.gucheng.hotmovies.data.local.Adapter.ImageAdapter;
import com.example.gucheng.hotmovies.data.local.Loaders.MoviesLoader;
import com.example.gucheng.hotmovies.data.local.Movies;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Movies>>{

    private static final int IMAGE_LOADER = 1;

    private Toolbar toolbar;
    private TextView mTextView;
    private View rootView;
    private GridView gridView;
    private ImageAdapter mImageAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar mProgressBar;
    LoaderManager.LoaderCallbacks<List<Movies>> callback;
    List<Movies> mMovies;

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
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_refresh);  //null?
        gridView = (GridView) rootView.findViewById(R.id.grid_view);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mTextView = (TextView) rootView.findViewById(R.id.empty_text);
        final LoaderManager loaderManager = getLoaderManager();
        callback = this;

        mProgressBar.setVisibility(View.VISIBLE);
        gridView.setEmptyView(mTextView);
        mImageAdapter = new ImageAdapter(getActivity(),new ArrayList<Movies>());
        loaderManager.initLoader(IMAGE_LOADER,null,this);
        gridView.setAdapter(mImageAdapter);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Popular");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loaderManager.restartLoader(1,null,callback);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(getContext(),EditorActivity.class);
                Movies m = mMovies.get(position);
                String jsonString = new Gson().toJson(m);
                intent.putExtra("clicked",jsonString);
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
                tmdbUrl = POP_URL;
                toolbar.setTitle("Popular");
                getLoaderManager().restartLoader(1,null,this);
                break;
            case R.id.action_rates:
                tmdbUrl = HIGH_URL;
                toolbar.setTitle("Top rated");
                getLoaderManager().restartLoader(1,null,this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movies>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(tmdbUrl);
        Uri.Builder builder = baseUri.buildUpon();
        return new MoviesLoader(getContext(),builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movies>> loader, List<Movies> movies) {
        mMovies = movies;
        mImageAdapter.clear();
        List<Movies> listTemp = new ArrayList<>();
        mProgressBar.setVisibility(View.GONE);

        if(mMovies != null && !mMovies.isEmpty()){
            for (int i = 0; i < mMovies.size(); i++){
                String mImgUrl = mMovies.get(i).getPosterUrl();
                listTemp.add(new Movies(mImgUrl));
            }
            mImageAdapter.addAll(listTemp);
        }else{
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()){
                mTextView.setText(R.string.no_data);
            }else {
                mTextView.setText(R.string.no_internet);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movies>> loader) {
        mImageAdapter.clear();
    }

}
