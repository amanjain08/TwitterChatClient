package com.juggad.twitterchatclient;

import android.app.Application;
import com.twitter.sdk.android.core.Twitter;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Twitter.initialize(this);
    }
}
