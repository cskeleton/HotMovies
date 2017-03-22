package com.example.gucheng.hotmovies.data.remote;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.gucheng.hotmovies.data.local.beans.Reviews;
import com.example.gucheng.hotmovies.data.local.db.MovieContract.MovieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gucheng on 2017/2/4.
 */

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getName();
    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private static int mIsPop;
    private static int mIsHigh;
    private static int mIsFav;

    private static String userLanguage;

    public static void fetchMovieData(String requestUrl, String language, Context context, int isPop, int isHigh, int isFav) {
        userLanguage = language;
        URL url = createUrl(requestUrl);
        mIsPop = isPop;
        mIsHigh = isHigh;
        mIsFav = isFav;
        String jsonResponse;
        try{
            jsonResponse = makeHTTPRequest(url);
            extractMovieDataFromJson(jsonResponse,context);
        }catch (IOException e){
            Log.e(LOG_TAG,"Error closing input steam ",e);
        }
    }

    public static List<Reviews> fetchReviewData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHTTPRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Error closing input steam ",e);
        }
        return extractReviewFromJson(jsonResponse);
    }

    private static void extractMovieDataFromJson(String movieJSON, Context context) {
        if(!TextUtils.isEmpty(movieJSON)) {
            int movieId;
            String movieTitle;
            String movieImgUrl;
            String movieYear;
            String movieOverview;
            double movieRate;
            int movieRuntime;
            String movieReviewUrl;
            String movieVideoUrl;
            String movieOriTitle;
            String movieLanguage;

            try {
                JSONObject movieListJsonObject = new JSONObject(movieJSON);
                JSONArray movieListJsonArray = movieListJsonObject.getJSONArray("results");
                ContentValues[] cvs = new ContentValues[movieListJsonArray.length()];
                for (int i = 0; i < movieListJsonArray.length(); i++) {
                    JSONObject listJ = movieListJsonArray.getJSONObject(i);
                    movieId = listJ.getInt("id");
                    movieTitle = listJ.getString("title");
                    movieImgUrl = listJ.getString("poster_path");
                    movieYear = listJ.getString("release_date").substring(0, 4);
                    movieOverview = listJ.getString("overview");
                    movieRate = listJ.getDouble("vote_average");
                    movieOriTitle = listJ.getString("original_title");
                    movieLanguage = listJ.getString("original_language");

                    String detailString = GetUrl.getMovieUrl(String.valueOf(movieId));
                    URL detailUrl  = createUrl(detailString);
                    String detailJsonResponse = null;
                    try {
                        detailJsonResponse = makeHTTPRequest(detailUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    JSONObject detailJ = new JSONObject(detailJsonResponse);

                    movieRuntime = detailJ.getInt("runtime");

                    movieVideoUrl = GetUrl.getTrailerUrl(movieId);
                    JSONObject jsonVideo;
                    try {
                        String jsonVideoResponse = makeHTTPRequest(createUrl(movieVideoUrl));
                        if(jsonVideoResponse != null){
                            jsonVideo = new JSONObject(jsonVideoResponse);
                            JSONArray jsonVideoArray = jsonVideo.getJSONArray("results");
                            if(jsonVideoArray.length()>=1){
                                JSONObject jsonVideoObject = jsonVideoArray.getJSONObject(0);
                                movieVideoUrl = YOUTUBE_URL + jsonVideoObject.getString("key");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    movieReviewUrl = GetUrl.getReviewsUrl(movieId);

                    try {
                        Poster.savePoster(movieImgUrl,movieId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ContentValues cv = new ContentValues();
                    cv.put(MovieEntry.COLUMN_MOVIE_ID, movieId);
                    cv.put(MovieEntry.COLUMN_MOVIE_TITLE, movieTitle);
                    cv.put(MovieEntry.COLUMN_MOVIE_POSTER_PATH, movieImgUrl);
                    cv.put(MovieEntry.COLUMN_MOVIE_DATE, movieYear);
                    cv.put(MovieEntry.COLUMN_MOVIE_OVERVIEW, movieOverview);
                    cv.put(MovieEntry.COLUMN_MOVIE_SCORE, movieRate);
                    cv.put(MovieEntry.COLUMN_MOVIE_RUNTIME, movieRuntime);
                    cv.put(MovieEntry.COLUMN_MOVIE_REVIEW, movieReviewUrl);
                    cv.put(MovieEntry.COLUMN_MOVIE_VIDEOS, movieVideoUrl);
                    cv.put(MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE, movieOriTitle);
                    cv.put(MovieEntry.COLUMN_MOVIE_LANGUAGE, movieLanguage);
                    cv.put(MovieEntry.COLUMN_MOVIE_POPULAR,mIsPop);
                    cv.put(MovieEntry.COLUMN_MOVIE_HIGH,mIsHigh);
                    cv.put(MovieEntry.COLUMN_MOVIE_FAVOURITE,mIsFav);
                    cvs[i] = cv;
                    String whereClause = MovieEntry.COLUMN_MOVIE_ID + "=?";
                    String[] whereArgs = {String.valueOf(movieId)};
                    context.getContentResolver().update(MovieEntry.CONTENT_URI,cv,whereClause,whereArgs);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Log.e(LOG_TAG,"Null json data");
        }
    }

    private static List<Reviews> extractReviewFromJson(String reviewJSON) {
        List<Reviews> mComments = new ArrayList<>();

        if(!TextUtils.isEmpty(reviewJSON)) {
            String mAuthor;
            String mReview;

            try {
                JSONObject reviewsListJsonObject = new JSONObject(reviewJSON);
                JSONArray reviewListJsonArray = reviewsListJsonObject.getJSONArray("results");
                for (int i = 0; i < reviewListJsonArray.length(); i++) {
                    JSONObject review = reviewListJsonArray.getJSONObject(i);
                    mAuthor = review.getString("author");
                    mReview = review.getString("content");
                    mComments.add(new Reviews(mAuthor,mReview));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Log.e(LOG_TAG,"Null json data");
        }
        return mComments;
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        if(url == null){return null;}

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG,"Error response code: "+ urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem retrieving the JSON results. ",e);
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
