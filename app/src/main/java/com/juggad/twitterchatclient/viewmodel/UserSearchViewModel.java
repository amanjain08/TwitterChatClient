package com.juggad.twitterchatclient.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.juggad.twitterchatclient.api.TwitterRestApiClient;
import com.juggad.twitterchatclient.data.FriendsRepository;
import com.juggad.twitterchatclient.database.UserDetailDaoImpl;
import com.juggad.twitterchatclient.database.entity.UserEntity;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class UserSearchViewModel extends AndroidViewModel {

    private FriendsRepository mFriendsRepository;

    public LiveData<List<UserEntity>> mUsers;

    private LiveData<String> networkErrors;

    public UserSearchViewModel(@NonNull final Application application) {
        super(application);
        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        mFriendsRepository = new FriendsRepository(new TwitterRestApiClient(twitterSession),
                new UserDetailDaoImpl(application));
        mUsers = mFriendsRepository.getUsers().getData();
        networkErrors = mFriendsRepository.getUsers().getNetworkError();
        mFriendsRepository.fetchUserListFromServer();
    }
}
