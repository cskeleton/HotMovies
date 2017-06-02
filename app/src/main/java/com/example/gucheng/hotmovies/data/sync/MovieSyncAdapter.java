package com.example.gucheng.hotmovies.data.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.example.gucheng.hotmovies.data.remote.QueryUtils;

import static android.content.Context.ACCOUNT_SERVICE;

/**
 * Created by gucheng on 2017/6/1.
 */

public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();
    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.example.gucheng.hotmovies";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "hotmovies.example.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    Account mAccount;

    private ContentResolver mContentResolver;
    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private static String mRequestUrl;
    private static int mIsPop;
    private static int mIsHigh;
    private static int mIsFav;


    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.v(LOG_TAG,"onPerformSync start running.");
        QueryUtils.fetchMovieData(mRequestUrl,getContext(),mIsPop,mIsHigh,mIsFav);
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context, String requestUrl, int isPop, int isHigh, int isFav) {
        mRequestUrl = requestUrl;
        mIsPop = isPop;
        mIsHigh = isHigh;
        mIsFav = isFav;

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), AUTHORITY, bundle);
    }

    private static Account getSyncAccount(Context context) {
        Account newAccount = new Account(ACCOUNT,ACCOUNT_TYPE);

        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        if(!accountManager.addAccountExplicitly(newAccount,null,null)){
            return null;
        }
        return newAccount;
    }
}
