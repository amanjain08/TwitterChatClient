package com.juggad.twitterchatclient.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.juggad.twitterchatclient.model.LoginAuthenticator;
import com.juggad.twitterchatclient.utils.Resource;
import com.juggad.twitterchatclient.utils.Status;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;


/**
 * Created by Aman Jain on 15/07/18.
 */
public class SplashScreenViewModel extends ViewModel {

    private MutableLiveData<Resource<LoginAuthenticator>> mLiveData = new MutableLiveData<>();

    public void checkLogin() {
        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        boolean loggedIn = twitterSession != null;
        mLiveData.postValue(new Resource<>(Status.SUCCESS, new LoginAuthenticator(loggedIn), null));
    }

    public MutableLiveData<Resource<LoginAuthenticator>> getAuthenticatorObservable() {
        return mLiveData;
    }

}
