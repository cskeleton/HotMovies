package com.example.gucheng.hotmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class EditorActivityFragment extends Fragment {

    private static final String RATE_STRING = " / 10";

    private String imgUrl;
    private String overview;
    private String year;
    private String oriTitle;
    private String title;
    private String language;
    private String rate;

    public EditorActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detailRootView = inflater.inflate(R.layout.fragment_editor, container, false);

        Intent intent = getActivity().getIntent();
        String jsonString = intent.getStringExtra("clicked");

        Movies movies = new Gson().fromJson(jsonString,Movies.class);
        imgUrl = movies.getImgUrl();
        overview = movies.getOverview();
        year = movies.getYear();
        oriTitle = movies.getOriTitle();
        title = movies.getTitle();
        language = movies.getLanguage();
        double rateDouble = movies.getRate();
        rate = String.format("%.1f",rateDouble);

        getActivity().setTitle(title);

        ImageView imageView = (ImageView) detailRootView.findViewById(R.id.detail_image);
        TextView oriTitleTextView = (TextView) detailRootView.findViewById(R.id.ori_title_text);
        TextView yearTextView = (TextView) detailRootView.findViewById(R.id.year_text);
        TextView languageTextView = (TextView) detailRootView.findViewById(R.id.language_text);
        TextView rateTextView = (TextView) detailRootView.findViewById(R.id.rate_text);
        TextView introTextView = (TextView) detailRootView.findViewById(R.id.overview_text);


        Picasso.with(getContext()).load(imgUrl).into(imageView);
        oriTitleTextView.setText(oriTitle);
        yearTextView.setText(year);
        languageTextView.setText(language);
        rateTextView.setText(rate + RATE_STRING);
        introTextView.setText(overview);
        return detailRootView;
    }
}
