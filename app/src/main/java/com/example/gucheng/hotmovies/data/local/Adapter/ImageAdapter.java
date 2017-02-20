package com.example.gucheng.hotmovies.data.local.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.gucheng.hotmovies.data.local.Movies;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gucheng on 2017/2/4.
 */

public class ImageAdapter extends ArrayAdapter<Movies> {
    private Context mContext;

    public ImageAdapter(Context context, ArrayList<Movies> movies){
        super(context,0,movies);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Movies currentMovies = getItem(position);
        ImageView imageView;
        String imgUrl = null;
        if (currentMovies != null) {
            imgUrl = currentMovies.getPosterUrl();
        }
        if(convertView == null){
            imageView = new ImageView(mContext);
            //Set image size as 3:2
            int w = parent.getWidth()/2;
            imageView.setLayoutParams(new GridView.LayoutParams(w, w/2*3));

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(8,8,8,8);
        }else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(imgUrl).into(imageView);
        return imageView;
    }
}
