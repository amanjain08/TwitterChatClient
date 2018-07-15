package com.juggad.twitterchatclient.model;

import android.arch.lifecycle.LiveData;
import com.juggad.twitterchatclient.database.entity.UserEntity;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class UserSearchResult {

    LiveData<List<UserEntity>> data;

    LiveData<String> networkError;

    public UserSearchResult(
            final LiveData<List<UserEntity>> data, final LiveData<String> networkError) {
        this.data = data;
        this.networkError = networkError;
    }

    public LiveData<List<UserEntity>> getData() {
        return data;
    }

    public void setData(
            final LiveData<List<UserEntity>> data) {
        this.data = data;
    }

    public LiveData<String> getNetworkError() {
        return networkError;
    }

    public void setNetworkError(final LiveData<String> networkError) {
        this.networkError = networkError;
    }
}
