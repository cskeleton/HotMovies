package com.example.gucheng.hotmovies.data.local.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gucheng.hotmovies.R;
import com.example.gucheng.hotmovies.data.local.beans.Reviews;

import java.util.ArrayList;

/**
 * Created by gucheng on 2017/3/12.
 */

public class ReviewAdapter extends ArrayAdapter<Reviews> {

    public ReviewAdapter(@NonNull Context context, ArrayList<Reviews> reviews) {
        super(context, 0, reviews);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_comments,parent,false);
        }

        Reviews currentComments = getItem(position);
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.comments_name);
        TextView contentTextView = (TextView) listItemView.findViewById(R.id.comments_content);

        authorTextView.setText(currentComments.getAuthor());
        contentTextView.setText(currentComments.getComment());
        return listItemView;
    }
}
