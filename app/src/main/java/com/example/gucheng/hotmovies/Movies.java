package com.example.gucheng.hotmovies;

/**
 * Created by gucheng on 2017/2/4.
 */

class Movies {
    private String mTitle;
    private String mYear;
    private String mCountry;
    private String mImgUrl;

    // For detail page use.
    Movies(String title, String year, String country, String imgUrl){
        mTitle = title;
        mYear = year;
        mCountry = country;
        mImgUrl = imgUrl;
    }

    Movies(String imgUrl){
        mImgUrl = imgUrl;
    }

    // Getter
    String getTitle(){return mTitle;}
    String getYear(){return mYear;}
    String getCountry(){return mCountry;}
    String getImgUrl(){return mImgUrl;}
}
