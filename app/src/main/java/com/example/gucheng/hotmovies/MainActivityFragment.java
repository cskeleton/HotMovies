package com.example.gucheng.hotmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Movies>>{

    //Set up API-KEY of TMDB here!!!!
    static final String API_KEY = " ";


    static String popUrl = "http://api.themoviedb.org/3/movie/popular?language=zh&api_key="+API_KEY;
    static String highUrl = "http://api.themoviedb.org/3/movie/top_rated?language=zh&api_key="+API_KEY;
    static String tmdbUrl = popUrl;
    private static final int IMAGE_LOADER = 1;

    private View rootView;
    private GridView gridView;
    private ImageAdapter mImageAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar mProgressBar;
    List<Movies> mMovies;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_main,container,false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_refresh);  //null?
        gridView = (GridView) rootView.findViewById(R.id.grid_view);

        LoaderManager loaderManager = getLoaderManager();
        mImageAdapter = new ImageAdapter(getActivity(),new ArrayList<Movies>());


        loaderManager.initLoader(IMAGE_LOADER,null,this);

        gridView.setAdapter(mImageAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                mImageAdapter.notifyDataSetChanged();
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
    public Loader<List<Movies>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(popUrl);
        Uri.Builder builder = baseUri.buildUpon();
        return new MoviesLoader(getContext(),builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movies>> loader, List<Movies> movies) {
        mMovies = movies;
        mImageAdapter.clear();
        List<Movies> listTemp = new ArrayList<>();
//        mProgressBar.setVisibility(View.GONE);    //unfinished.

        if(mMovies != null && !mMovies.isEmpty()){
            for (int i = 0; i < mMovies.size(); i++){
                String mImgUrl = mMovies.get(i).getImgUrl();
                listTemp.add(new Movies(mImgUrl));
            }
        }
        mImageAdapter.addAll(listTemp);
    }

    @Override
    public void onLoaderReset(Loader<List<Movies>> loader) {
        mImageAdapter.clear();
    }

    public void setUrl(String url){
        tmdbUrl = url;
    }

}
