package com.example.gucheng.hotmovies.data.remote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gucheng on 2017/3/1.
 */
public class Poster {

    private static final String LOG_TAG = "Poster";
    private static final String POSTER_PATH = "/HotMovies/poster/";
    private static final String FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + POSTER_PATH;


    public static void savePoster(String mPosterUrl, int mMovieId) throws IOException {
        HttpURLConnection urlConnection = null;
        String urlString = GetUrl.getPosterUrl(mPosterUrl);
        URL url = createUrl(urlString);
        InputStream inputStream = null;
        if(url != null) {
            try{
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);

                if(urlConnection.getResponseCode() ==200){
                    inputStream = urlConnection.getInputStream();
                }else {
                    Log.e(LOG_TAG,"Error response code: "+ urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG,"Problem retrieving the JSON results. ",e);
            }

            try {
                Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
                saveFile(mBitmap,mMovieId);
            } catch (Exception e) {
                Log.v(LOG_TAG,"Save poster to external storage error.");
            }
            if(urlConnection != null) urlConnection.disconnect();
            if(inputStream != null) inputStream.close();
        }
    }

    public static Bitmap getPoster(int mMovieId) throws IOException{
        String moviePath = FILE_DIR + mMovieId;
        File poster = new File(moviePath);
        if (poster.exists()) {
            return BitmapFactory.decodeFile(moviePath);
        }else {
            Log.v(LOG_TAG,"No poster file found.");
            return null;
        }
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

    private static void saveFile(Bitmap bm, int mMovieId) throws IOException {

        final File dirFile = new File(FILE_DIR);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File posterImg = new File(FILE_DIR + mMovieId + ".jpg");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(posterImg));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }
}
