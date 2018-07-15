package com.juggad.twitterchatclient.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Aman Jain on 15/07/18.
 */
@Entity()
public class ChatItem {

    @ColumnInfo(name = "message")
    public String message;

    @ColumnInfo(name = "message_id")
    public String messageId;

    @ColumnInfo(name = "profile_picture")
    public String profilePictureUrl;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "their_id")
    public String theirId;

    @ColumnInfo(name = "their_name")
    public String theirName;

    @ColumnInfo(name = "time")
    public long time;

    public ChatItem(final String theirId, final String theirName, final String profilePictureUrl,
            final String messageId,
            final String message, final long time) {
        this.theirId = theirId;
        this.theirName = theirName;
        this.profilePictureUrl = profilePictureUrl;
        this.messageId = messageId;
        this.message = message;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(final String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getTheirId() {
        return theirId;
    }

    public void setTheirId(final String theirId) {
        this.theirId = theirId;
    }

    public String getTheirName() {
        return theirName;
    }

    public void setTheirName(final String theirName) {
        this.theirName = theirName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(final long time) {
        this.time = time;
    }
}
