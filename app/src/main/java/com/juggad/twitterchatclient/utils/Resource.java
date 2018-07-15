package com.juggad.twitterchatclient.utils;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class Resource<T> {

    T mData;

    Status mStatus;

    String msg;

    public Resource(final Status status, final T data, final String msg) {
        mStatus = status;
        mData = data;
        this.msg = msg;
    }

    public Resource() {
    }

    public T getData() {
        return mData;
    }

    public void setData(final T data) {
        mData = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(final Status status) {
        mStatus = status;
    }
}
