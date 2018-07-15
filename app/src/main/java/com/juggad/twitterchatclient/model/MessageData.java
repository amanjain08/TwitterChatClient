package com.juggad.twitterchatclient.model;

import com.google.gson.annotations.SerializedName;

public class MessageData {

    @SerializedName("text")
    private String text;

    public MessageData(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }
}