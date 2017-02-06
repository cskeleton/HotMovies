package com.example.gucheng.hotmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Movies>>{
    static String tmdbUrl = "http://192.168.1.6:8000/f/17dce405f2/?raw=1";

    private View rootView;
    private GridView gridView;
    private ImageAdapter mImageAdapter;
    private ProgressBar mProgressBar;
    List<Movies> mMovies;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_main,container,false);

        gridView = (GridView) rootView.findViewById(R.id.grid_view);

        LoaderManager loaderManager = getLoaderManager();
        mImageAdapter = new ImageAdapter(getActivity(),new ArrayList<Movies>());

        final int IMAGE_LOADER = 1;
        loaderManager.initLoader(IMAGE_LOADER,null,this);

        gridView.setAdapter(mImageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Intent intent = new Intent(getContext(),DetailActivity.class);
            }
        });

        return rootView;
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
//        mProgressBar.setVisibility(View.GONE);
        List<Movies> listTemp = new ArrayList<>();

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
}
