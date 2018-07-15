package com.juggad.twitterchatclient.model;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.User;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class UserList implements Serializable {

    @SerializedName("users")
    List<User> mUserList;

    @SerializedName("next_cursor_str")
    String nextCursor;

    @SerializedName("previous_cursor_str")
    String previousCursor;

    public UserList(final List<User> userList, final String previousCursor, final String nextCursor) {
        mUserList = userList;
        this.previousCursor = previousCursor;
        this.nextCursor = nextCursor;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(final String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public String getPreviousCursor() {
        return previousCursor;
    }

    public void setPreviousCursor(final String previousCursor) {
        this.previousCursor = previousCursor;
    }

    public List<User> getUserList() {
        return mUserList;
    }

    public void setUserList(final List<User> userList) {
        mUserList = userList;
    }

    @Override
    public String toString() {
        return "UserList{" +
                "mUserList=" + mUserList +
                ", previousCursor='" + previousCursor + '\'' +
                ", nextCursor='" + nextCursor + '\'' +
                '}';
    }
}
