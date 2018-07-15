package com.juggad.twitterchatclient.model;

import com.google.gson.annotations.SerializedName;

public class Target {

    @SerializedName("recipient_id")
    private String recipientId;

    public Target(final String recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(final String recipientId) {
        this.recipientId = recipientId;
    }
}