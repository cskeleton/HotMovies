package com.example.gucheng.hotmovies;

import android.text.TextUtils;
import android.util.Log;

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

    private static final String IMG_URL_FRONTER = "http://image.tmdb.org/t/p/w185";

    public static List<Movies> fetchData(String requestUrl) {
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
        String movieImgUrl;
        String movieOverview;
        String movieYear;
        String movieOriTitle;
        String movieTitle;
        String movieLanguage;
        double movieRate;

        ArrayList<Movies> movies = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(movieJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for(int i = 0; i < jsonArray.length();i++){
                JSONObject j = jsonArray.getJSONObject(i);
                movieImgUrl = IMG_URL_FRONTER + j.getString("poster_path");
                movieOverview = j.getString("overview");
                movieYear = j.getString("release_date").substring(0,4);
                movieOriTitle = j.getString("original_title");
                movieTitle = j.getString("title");
                movieLanguage = j.getString("original_language");
                movieRate = j.getDouble("vote_average");
                movies.add(new Movies(movieImgUrl,movieOverview,movieOriTitle,movieTitle,movieYear,movieLanguage,movieRate));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
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
