package com.juggad.twitterchatclient.data;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import com.juggad.twitterchatclient.api.TwitterRestApiClient;
import com.juggad.twitterchatclient.database.dao.UserDetailDao;
import com.juggad.twitterchatclient.database.entity.UserEntity;
import com.juggad.twitterchatclient.database.mapper.UserToUserEntityMapper;
import com.juggad.twitterchatclient.model.UserList;
import com.juggad.twitterchatclient.model.UserSearchResult;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class FriendsRepository {

    private static final String TAG = FriendsRepository.class.getSimpleName();

    private static final int RESULT_COUNT = 200;

    private TwitterRestApiClient mRestApiClient;

    String cursor = "-1";

    MutableLiveData<List<UserEntity>> mListLiveData = new MutableLiveData<>();

    MutableLiveData<String> networkError = new MutableLiveData<>();

    private UserSearchResult mUserSearchResult = new UserSearchResult(mListLiveData, networkError);

    public FriendsRepository(final TwitterRestApiClient restApiClient, final UserDetailDao userDetailDao) {
        mRestApiClient = restApiClient;
    }

    //Fetch list of followers of current user
    public void fetchUserListFromServer() {
        mRestApiClient.userDetail().getFollowers(cursor, RESULT_COUNT).enqueue(new Callback<UserList>() {
            @Override
            public void success(final Result<UserList> result) {
                cursor = result.data.getNextCursor();
                io.reactivex.Observable
                        .fromIterable(result.data.getUserList())
                        .map(new UserToUserEntityMapper()::modelToEntity)
                        .toSortedList((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
                        .subscribeOn(Schedulers.io())
                        .subscribe(new DisposableSingleObserver<List<UserEntity>>() {
                            @Override
                            public void onSuccess(final List<UserEntity> userEntities) {
                                mListLiveData.postValue(userEntities);
                            }

                            @Override
                            public void onError(final Throwable e) {
                                Log.e(TAG, "onError: ", e);
                                networkError.postValue(e.getLocalizedMessage());
                            }
                        });
            }

            @Override
            public void failure(final TwitterException exception) {
                Log.e(TAG, "failure: ", exception);
                networkError.postValue(exception.getLocalizedMessage());
            }
        });
    }

    public UserSearchResult getUsers() {
        return mUserSearchResult;
    }
}
