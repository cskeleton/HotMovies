package com.example.gucheng.hotmovies.data.local;

/**
 * Created by gucheng on 2017/2/4.
 */

public class Movies {
    private int mId;
    private String mTitle;
    private String mPosterUrl;
    private String mDate;
    private String mOverview;
    private double mRate;
    private int mRuntime;
    private String mReview;
    private String mVideo;
    private String mOriTitle;
    private String mLanguage;


    /**
    * Constructors
    **/
    //For poster page.
    public Movies(String posterUrl){
        mPosterUrl = posterUrl;}

    // All info.
    public Movies(int mId, String mTitle, String mPosterUrl, String mDate, String mOverview, double mRate, int mRuntime, String mReview, String mVideo, String mOriTitle, String mLanguage) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mPosterUrl = mPosterUrl;
        this.mDate = mDate;
        this.mOverview = mOverview;
        this.mRate = mRate;
        this.mRuntime = mRuntime;
        this.mReview = mReview;
        this.mVideo = mVideo;
        this.mOriTitle = mOriTitle;
        this.mLanguage = mLanguage;
    }


    /**
     * Getter
     **/
    public int getId(){
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public String getDate() {
        return mDate;
    }

    public String getOverview() {
        return mOverview;
    }

    public double getRate() {
        return mRate;
    }

    public int getRuntime() {
        return mRuntime;
    }

    public String getReview() {
        return mReview;
    }

    public String getVideo() {
        return mVideo;
    }

    public String getOriTitle() {
        return mOriTitle;
    }

    public String getLanguage() {
        return mLanguage;
    }


    /**
     * Setter
     **/
    public void setId(int mId){
        this.mId = mId;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setPosterUrl(String mPosterUrl) {
        this.mPosterUrl = mPosterUrl;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public void setOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public void setRate(double mRate) {
        this.mRate = mRate;
    }

    public void setRuntime(int mRuntime) {
        this.mRuntime = mRuntime;
    }

    public void setReview(String mReview){
        this.mReview = mReview;
    }

    public void setVideo(String mVideo) {
        this.mVideo = mVideo;
    }

    public void setOriTitle(String mOriTitle) {
        this.mOriTitle = mOriTitle;
    }

    public void setLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

}
