package com.juggad.twitterchatclient.model;

import com.google.gson.annotations.SerializedName;

public class DirectMessageEvent {

    @SerializedName("event")
    private MessageEvent mMessageEvent;

    public DirectMessageEvent(final String message, final String recipientId) {
        Target target = new Target(recipientId);
        MessageData messageData = new MessageData(message);
        MessageCreate messageCreate = new MessageCreate(messageData, target);
        mMessageEvent = new MessageEvent(messageCreate);
        mMessageEvent.setCreatedTimestamp(System.currentTimeMillis());
    }

    public DirectMessageEvent(final MessageEvent messageEvent) {
        mMessageEvent = messageEvent;
    }

    public MessageEvent getMessageEvent() {
        return mMessageEvent;
    }

    public void setMessageEvent(final MessageEvent messageEvent) {
        mMessageEvent = messageEvent;
    }
}