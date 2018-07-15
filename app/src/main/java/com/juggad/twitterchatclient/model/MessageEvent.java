package com.juggad.twitterchatclient.model;

import com.google.gson.annotations.SerializedName;

public class MessageEvent {

    @SerializedName("created_timestamp")
    private long createdTimestamp;

    @SerializedName("id")
    private String id;

    @SerializedName("message_create")
    private MessageCreate messageCreate;

    @SerializedName("sync")
    private boolean sync = true;

    @SerializedName("type")
    private String type;

    public MessageEvent(final MessageCreate messageCreate, final long createdTimestamp, final String id,
            final String type) {
        this.messageCreate = messageCreate;
        this.createdTimestamp = createdTimestamp;
        this.id = id;
        this.type = type;
    }

    public MessageEvent(final MessageCreate messageCreate) {
        this.messageCreate = messageCreate;
        type = "message_create";
        sync = false;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(final long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public MessageCreate getMessageCreate() {
        return messageCreate;
    }

    public void setMessageCreate(final MessageCreate messageCreate) {
        this.messageCreate = messageCreate;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(final boolean sync) {
        this.sync = sync;
    }
}