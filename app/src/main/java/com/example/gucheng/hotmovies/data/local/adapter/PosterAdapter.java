package com.example.gucheng.hotmovies.data.local.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gucheng.hotmovies.R;
import com.example.gucheng.hotmovies.data.local.db.MovieContract.MovieEntry;
import com.example.gucheng.hotmovies.data.remote.Poster;

import java.io.IOException;

/**
 * Created by gucheng on 2017/3/1.
 */

public class PosterAdapter extends CursorAdapter {
    private static final String LOG_TAG = "PosterAdapter Class";

    private Bitmap bitmap;


    public PosterAdapter(Context context, Cursor c) {
        super(context,c,0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.v("PosterAdapter","newView has been called.");
        return LayoutInflater.from(context).inflate(R.layout.list_poster,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView titleTV = (TextView) view.findViewById(R.id.title);
        ImageView posterIV = (ImageView) view.findViewById(R.id.poster_image);

        String title = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUNM_MOVIE_TITLE));
        titleTV.setText(title);

        int movieId = cursor.getInt(cursor.getColumnIndexOrThrow(MovieEntry.COLUNM_MOVIE_ID));
        try {
            bitmap = Poster.getPoster(movieId);
            Log.v("BitMap,", String.valueOf(bitmap));
        } catch (IOException e) {
            Log.e(LOG_TAG,"Set poster to bitmap error");
        }
        posterIV.setImageBitmap(bitmap);
    }
}
