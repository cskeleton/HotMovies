package com.example.gucheng.hotmovies.data.local.beans;

/**
 * Created by gucheng on 2017/3/12.
 */

public class Reviews {
    private String mAuthor;
    private String mContent;

    public Reviews(String author, String content) {
        mAuthor = author;
        mContent = content;
    }

    public String getAuthor(){return mAuthor;}
    public String getComment(){return mContent;}
}
