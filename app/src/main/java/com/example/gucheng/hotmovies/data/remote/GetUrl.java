package com.example.gucheng.hotmovies.data.remote;

import com.example.gucheng.hotmovies.BuildConfig;

/**
 * Created by gucheng on 2017/2/19.
 */

public class GetUrl {
    //Set up API-KEY of TMDB here!!!!
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String LANGUAGE_BASE = "&language=";
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String IMG_BASE_URL = "http://image.tmdb.org/t/p/w185";
    private static final String API_KEY_TAG = "?api_key=";
    private static final String REVIEWS = "/reviews";
    private static final String VIDEOS = "/videos";

    public static final String POP_URL = "popular";
    public static final String HIGH_URL = "top_rated";

    private static String mLanguage = "zh";

    public static void setLanguage(String language){
        if(language != null) mLanguage = language;
    }

    public static String getMovieUrl(String movieId){
        return BASE_URL + movieId + API_KEY_TAG + API_KEY + LANGUAGE_BASE + mLanguage;
    }

    // Check the value of movie id is unnecessary? (if error, server will return CODE 34).
    // And in case of error, check before parsing JSON which get from server.
    public static String getTrailerUrl(int mMovieId) {
        return BASE_URL + mMovieId + VIDEOS + API_KEY_TAG + API_KEY;
    }


    public static String getReviewsUrl(int mMovieId){
        return BASE_URL + mMovieId + REVIEWS + API_KEY_TAG + API_KEY;
    }

    public static String getPosterUrl(String mPosterUrl){
        return IMG_BASE_URL + mPosterUrl;
    }
}
