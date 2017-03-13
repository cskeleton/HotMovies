package com.example.gucheng.hotmovies.data.local.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
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
    private Context mContext;
    private GridView mGridview;
    private Bitmap bitmap;
    private int mWide;
    private float mDensity;

    public PosterAdapter(int wide, float density, GridView gridView, Context context, Cursor c) {
        super(context,c,0);
        mContext = context;
        mGridview = gridView;
        mWide = wide;
        mDensity = density;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.list_poster,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView posterIV = (ImageView) view.findViewById(R.id.poster_image);
        TextView titleTV = (TextView) view.findViewById(R.id.title);


        String title = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUNM_MOVIE_TITLE));
        titleTV.setText(title);

        int movieId = cursor.getInt(cursor.getColumnIndexOrThrow(MovieEntry.COLUNM_MOVIE_ID));
        try {
            bitmap = Poster.getPoster(movieId);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Set poster to bitmap error");
        }

        //Set ImageView as 3:2
        posterIV.setMinimumHeight((int) (mWide/2*1.5*mDensity));
        posterIV.setScaleType(ImageView.ScaleType.FIT_XY);
        posterIV.setImageBitmap(bitmap);
    }
}
