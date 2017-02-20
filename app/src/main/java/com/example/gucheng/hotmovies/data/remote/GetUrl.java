package com.example.gucheng.hotmovies.data.remote;

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
    private static final String POP_URL = "popular";
    private static final String HIGH_URL = "top_rated";
    private static final String REVIEWS = "/reviews?";
    private static final String VIDEOS = "/videos?";

    public static String getDetailUrl(int movieId, String language){
        String mLanguage = LANGUAGE_BASE + language;
        return BASE_URL + movieId + "?" + API_KEY + mLanguage;
    }

    public static String getTrailerUrl(int mMovieId) {
        if(Objects.equals(mMovieId, POP_URL) && Objects.equals(mMovieId, HIGH_URL)){
            return null;
        }else {
            return BASE_URL + mMovieId + VIDEOS + API_KEY;
        }
    }

    public static String getReviewsUrl(int movieId){
        if(Objects.equals(movieId, POP_URL) && Objects.equals(movieId, HIGH_URL)){
            return null;
        }else {
            return BASE_URL + movieId + REVIEWS + API_KEY;
        }
    }
}
