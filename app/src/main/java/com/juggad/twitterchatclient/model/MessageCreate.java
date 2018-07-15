package com.juggad.twitterchatclient.model;

import com.google.gson.annotations.SerializedName;

public class MessageCreate {

    @SerializedName("message_data")
    private MessageData messageData;

    @SerializedName("sender_id")
    private String senderId;

    @SerializedName("source_app_id")
    private String sourceAppId;

    @SerializedName("target")
    private Target target;

    public MessageCreate(final String sourceAppId, final MessageData messageData, final String senderId,
            final Target target) {
        this.sourceAppId = sourceAppId;
        this.messageData = messageData;
        this.senderId = senderId;
        this.target = target;
    }

    public MessageCreate(final MessageData messageData, final Target target) {
        this.messageData = messageData;
        this.target = target;
    }

    public MessageData getMessageData() {
        return messageData;
    }

    public void setMessageData(final MessageData messageData) {
        this.messageData = messageData;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(final String senderId) {
        this.senderId = senderId;
    }

    public String getSourceAppId() {
        return sourceAppId;
    }

    public void setSourceAppId(final String sourceAppId) {
        this.sourceAppId = sourceAppId;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(final Target target) {
        this.target = target;
    }
}