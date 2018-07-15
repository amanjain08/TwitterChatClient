package com.juggad.twitterchatclient.api;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class TwitterRestApiClient extends TwitterApiClient {

    public TwitterRestApiClient(TwitterSession session) {
        super(session);
    }

    public DirectMessageApi directMessage() {
        return getService(DirectMessageApi.class);
    }

    public GetUserDetailApi userDetail() {
        return getService(GetUserDetailApi.class);
    }
}