package com.example.gucheng.hotmovies.data.remote;

import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Log;

import com.example.gucheng.hotmovies.data.local.Movies;
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

    private static final String IMG_URL_FRONTIER = "http://image.tmdb.org/t/p/w185";
    private static final String REIVEW_URL_FRONTIER = "https://www.themoviedb.org/review/";
    private static final String TRAILER_URL_FRONTIER = "https://www.youtube.com/watch?v=";

    private static String userLanguage;

    public static List<Movies> fetchData(String requestUrl,String language) {
        userLanguage = language;
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHTTPRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Error closing input steam ",e);
        }
        return extractFeatureFromJson(jsonResponse);
    }

    private static List<Movies> extractFeatureFromJson(String movieJSON) {
        if(TextUtils.isEmpty(movieJSON)){
            return null;
        }
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


        ArrayList<Movies> movies = new ArrayList<>();

        try {
            JSONObject movieListJsonObject = new JSONObject(movieJSON);
            JSONArray movieListJsonArray = movieListJsonObject.getJSONArray("results");
            for(int i = 0; i < movieListJsonArray.length();i++){
                JSONObject listJ = movieListJsonArray.getJSONObject(i);
                movieId = listJ.getInt("id");
                movieTitle = listJ.getString("title");
                movieImgUrl = IMG_URL_FRONTIER + listJ.getString("poster_path");
                movieYear = listJ.getString("release_date").substring(0,4);
                movieOverview = listJ.getString("overview");
                movieRate = listJ.getDouble("vote_average");
                movieOriTitle = listJ.getString("original_title");
                movieLanguage = listJ.getString("original_language");

                JSONObject detailJ = new JSONObject(GetUrl.getDetailUrl(movieId,userLanguage));
                movieRuntime = detailJ.getInt("runtime");

                movieVideoUrl = GetUrl.getTrailerUrl(movieId);
                movieReviewUrl = GetUrl.getReviewsUrl(movieId);

                movies.add(new Movies(movieId,movieTitle,movieImgUrl,movieYear,movieOverview,
                        movieRate,movieRuntime,movieReviewUrl,movieVideoUrl,movieOriTitle,movieLanguage));
                saveToDatabase(movies.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    private static boolean saveToDatabase(Movies movie){
        ContentValues cv = new ContentValues();

        String title = movie.getTitle();
        String posterUrl = movie.getPosterUrl();
        String date = movie.getDate();
        String overview = movie.getOverview();
        Double rate = movie.getRate();
        int runtime = movie.getRuntime();
        String reviews = movie.getReview();     // The string format of JSON Object.
        String videos = movie.getVideo();       // The url of trailer video.
        String oriTitle = movie.getOriTitle();
        String language = movie.getLanguage();

        cv.put(MovieEntry.COLUNM_MOVIE_ORIGINAL_TITLE,title);
        cv.put(MovieEntry.COLUNM_MOVIE_POSTER_PATH,posterUrl);
        cv.put(MovieEntry.COLUNM_MOVIE_DATE,date);
        cv.put(MovieEntry.COLUNM_MOVIE_OVERVIEW,overview);
        cv.put(MovieEntry.COLUNM_MOVIE_SCORE,rate);
        cv.put(MovieEntry.COLUNM_MOVIE_RUNTIME,runtime);
        cv.put(MovieEntry.COLUNM_MOVIE_REVIEW,reviews);
        cv.put(MovieEntry.COLUNM_MOVIE_VIDEOS,videos);
        cv.put(MovieEntry.COLUNM_MOVIE_ORIGINAL_TITLE,oriTitle);
        cv.put(MovieEntry.COLUNM_MOVIE_LANGUAGE,language);

        getContentResolver().insert(
                uri,
                cv);
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

            if(urlConnection.getResponseCode() ==200){
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
