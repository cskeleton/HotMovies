package com.example.gucheng.hotmovies.data.remote;

import android.util.Log;

import com.example.gucheng.hotmovies.BuildConfig;

import java.util.Objects;

/**
 * Created by gucheng on 2017/2/19.
 */

public class GetUrl {
    //Set up API-KEY of TMDB here!!!!
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String LANGUAGE_BASE = "&language=";
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String IMG_BASE_URL = "http://image.tmdb.org/t/p/w185";
    private static final String REVIEWS = "/reviews?";
    private static final String VIDEOS = "/videos?";

    public static final String POP_URL = "popular";
    public static final String HIGH_URL = "top_rated";

    private static String mLanguage = "zh";

    public static void setLanguage(String language){
        if(language != null) mLanguage = language;
    }

    public static String getMovieUrl(String movieId){
        return BASE_URL + movieId + "?" + API_KEY + LANGUAGE_BASE + mLanguage;
    }

    public static String getTrailerUrl(int mMovieId) {
        if(Objects.equals(mMovieId, POP_URL) && Objects.equals(mMovieId, HIGH_URL)){
            return null;
        }else {
            return BASE_URL + mMovieId + VIDEOS + API_KEY;
        }
    }

    public static String getReviewsUrl(int mMovieId){
        if(Objects.equals(mMovieId, POP_URL) && Objects.equals(mMovieId, HIGH_URL)){
            return null;
        }else {
            return BASE_URL + mMovieId + REVIEWS + API_KEY;
        }
    }

    public static String getPosterUrl(String mPosterUrl){
        Log.v("posterUrl,", IMG_BASE_URL + mPosterUrl);
        return IMG_BASE_URL + mPosterUrl;
    }
}
