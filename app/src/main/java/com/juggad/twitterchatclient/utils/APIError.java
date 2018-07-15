package com.juggad.twitterchatclient.utils;

public class APIError {

    private String message;

    private int statusCode;

    public APIError() {
    }

    public String message() {
        return message;
    }

    public int status() {
        return statusCode;
    }
}