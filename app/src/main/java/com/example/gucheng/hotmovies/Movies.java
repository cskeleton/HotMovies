package com.example.gucheng.hotmovies;

/**
 * Created by gucheng on 2017/2/4.
 */

class Movies {
    private String mImgUrl;
    private String mOverview;
    private String mYear;
    private String mOriTitle;
    private String mTitle;
    private String mLanguage;
    private double mRate;

    // For detail page use.
    Movies(String imgUrl, String overview, String oriTitle, String title, String year, String Language, double rate){
        mImgUrl = imgUrl;
        mOverview = overview;
        mYear = year;
        mOriTitle = oriTitle;
        mTitle = title;
        mLanguage = Language;
        mRate = rate;
    }

    //For poster page.
    Movies(String imgUrl){mImgUrl = imgUrl;}

    // Getter
    String getImgUrl(){return mImgUrl;}
    String getOverview(){return mOverview;}
    String getYear(){return mYear;}
    String getOriTitle(){return mOriTitle;}
    String getTitle(){return mTitle;}
    String getLanguage(){return mLanguage;}
    double getRate(){return mRate;}

}
