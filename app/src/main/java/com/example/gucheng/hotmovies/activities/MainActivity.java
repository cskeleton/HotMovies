package com.example.gucheng.hotmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.gucheng.hotmovies.R;
import com.example.gucheng.hotmovies.data.sync.MovieSyncAdapter;

import static com.example.gucheng.hotmovies.BuildConfig.API_KEY;

public class MainActivity extends AppCompatActivity {

    private static final String REQUEST_URL = "http://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
    private static final int IS_POPULAR = 1;
    private static final int IS_MOSTRATED = 0;
    private static final int IS_FAVOURITE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieSyncAdapter.syncImmediately(this,REQUEST_URL,IS_POPULAR,IS_MOSTRATED,IS_FAVOURITE);
    }
}
