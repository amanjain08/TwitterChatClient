package com.juggad.twitterchatclient.model;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class LoginAuthenticator {

    private boolean mLoggedIn;

    public LoginAuthenticator(final boolean loggedIn) {
        mLoggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return mLoggedIn;
    }

    public void setLoggedIn(final boolean loggedIn) {
        mLoggedIn = loggedIn;
    }
}
