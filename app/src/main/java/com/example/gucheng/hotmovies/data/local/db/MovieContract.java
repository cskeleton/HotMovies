package com.example.gucheng.hotmovies.data.local.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gucheng on 2017/2/17.
 */

public class MovieContract {
    private MovieContract(){}

    static final String CONTENT_AUTHORITY = "com.example.gucheng.hotmovies";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String MOVIE_PATH = "movies";

    public static class MovieEntry implements BaseColumns {
        static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_PATH;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_PATH;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,MOVIE_PATH);

        public static final String TABLE_NAME = "movies";
        public static final String COLUNM_MOVIE_ID = "movieID";
        public static final String COLUNM_MOVIE_TITLE = "title";
        public static final String COLUNM_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUNM_MOVIE_DATE = "release_date";
        public static final String COLUNM_MOVIE_OVERVIEW = "overview";
        public static final String COLUNM_MOVIE_SCORE = "user_score";
        public static final String COLUNM_MOVIE_RUNTIME = "runtime";
        public static final String COLUNM_MOVIE_REVIEW = "reviews";
        public static final String COLUNM_MOVIE_VIDEOS = "videos";
        public static final String COLUNM_MOVIE_ORIGINAL_TITLE = "original_title";
        public static final String COLUNM_MOVIE_LANGUAGE = "original_language";

    }
}
