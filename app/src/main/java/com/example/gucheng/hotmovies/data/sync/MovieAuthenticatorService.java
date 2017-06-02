package com.example.gucheng.hotmovies.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by gucheng on 2017/6/1.
 */

public class MovieAuthenticatorService extends Service {
    private MovieAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new MovieAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
