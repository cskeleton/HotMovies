package com.example.gucheng.hotmovies.Fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gucheng.hotmovies.R;
import com.example.gucheng.hotmovies.activities.CommentsActivity;
import com.example.gucheng.hotmovies.data.remote.Poster;

import java.io.IOException;
import java.util.Objects;

import static com.example.gucheng.hotmovies.data.local.db.MovieContract.MovieEntry;

/**
 * A placeholder fragment containing a simple view.
 */
public class EditorActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String RATE_STRING = " / 10";
    private static final int EXISTING_MOVIE_LOADER = 1;

    private String imgUrl;
    private String overview;
    private String year;
    private String oriTitle;
    private String title;
    private String language;
    private String rate;
    private String runtime;
    private String trailerString;
    private String reviewString;
    private int movieId;
    private Uri currentUri;
    private Button trailerButton;
    private View detailView;
    private Toolbar toolbar;
    private ImageView imageView;
    private TextView oriTitleTextView;
    private TextView yearTextView;
    private TextView languageTextView;
    private TextView rateTextView;
    private TextView runtimeTextView;
    private TextView introTextView;

    private int w_px;
    private int w_dip;
    float density;

    public EditorActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        detailView = inflater.inflate(R.layout.fragment_editor, container, false);

        //Toolbar
        toolbar = (Toolbar) detailView.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        Intent intent = getActivity().getIntent();
        currentUri = intent.getData();

        trailerButton = (Button) detailView.findViewById(R.id.trailer);
        imageView = (ImageView) detailView.findViewById(R.id.detail_image);
        oriTitleTextView = (TextView) detailView.findViewById(R.id.ori_title_text);
        yearTextView = (TextView) detailView.findViewById(R.id.year_text);
        languageTextView = (TextView) detailView.findViewById(R.id.language_text);
        rateTextView = (TextView) detailView.findViewById(R.id.rate_text);
        runtimeTextView = (TextView) detailView.findViewById(R.id.runtime_text);
        introTextView = (TextView) detailView.findViewById(R.id.overview_text);

        DisplayMetrics displayMetrics = detailView.getResources().getDisplayMetrics();
        w_px = displayMetrics.widthPixels;
        density = displayMetrics.density;
        w_dip = (int) (w_px/density);


        trailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(trailerString));
                startActivity(i);
            }
        });
        getLoaderManager().initLoader(EXISTING_MOVIE_LOADER,null,this);
        return detailView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_comments:
                Intent i = new Intent(getActivity(), CommentsActivity.class);
                i.putExtra(MovieEntry.COLUNM_MOVIE_REVIEW,reviewString);
                startActivity(i);
                break;
            case R.id.action_favourite:
                //TODO: mark a true in database.
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
                        MovieEntry.COLUNM_MOVIE_REVIEW };
        return new CursorLoader(getActivity(),MovieEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()){
            movieId = cursor.getInt(cursor.getColumnIndexOrThrow(MovieEntry.COLUNM_MOVIE_ID));
            title = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUNM_MOVIE_TITLE));
            trailerString = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUNM_MOVIE_VIDEOS));
            reviewString = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUNM_MOVIE_REVIEW));
            oriTitle = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUNM_MOVIE_ORIGINAL_TITLE));
            year = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUNM_MOVIE_DATE));
            language = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUNM_MOVIE_LANGUAGE));
            rate = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUNM_MOVIE_SCORE));
            runtime = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUNM_MOVIE_RUNTIME));
            overview = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUNM_MOVIE_OVERVIEW));

        }

        toolbar.setTitle(title);
        oriTitleTextView.setText(oriTitle);
        yearTextView.setText(year);
        languageTextView.setText(language);
        rateTextView.setText(rate + RATE_STRING);
        introTextView.setText(overview);
        if(Objects.equals(runtime, "0")){
            runtimeTextView.setVisibility(View.GONE);
        }else {
            runtimeTextView.setText(runtime);
        }

        try {
            Bitmap bitmap = Poster.getPoster(movieId);
            imageView.setImageBitmap(bitmap);
            imageView.setMinimumHeight((int) ((w_px-32*density)*3/4));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        imageView.setImageBitmap(null);
        getActivity().setTitle(null);
        oriTitleTextView.setText(null);
        yearTextView.setText(null);
        languageTextView.setText(null);
        rateTextView.setText(null);
        runtimeTextView.setText(null);
        introTextView.setText(null);
    }
}
