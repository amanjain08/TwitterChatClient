package com.juggad.twitterchatclient.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.twitter.sdk.android.core.TwitterCore;

/**
 * Created by Aman Jain on 15/07/18.
 */
@Entity(tableName = "chatmessage")
public class ChatMessage {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    String id;

    @ColumnInfo(name = "message")
    String message;

    @ColumnInfo(name = "receiver_id")
    String receiverId;

    @ColumnInfo(name = "sender_id")
    String senderId;

    @ColumnInfo(name = "sync")
    boolean sync;

    @ColumnInfo(name = "time")
    long time;

    public ChatMessage(@NonNull final String id, final String message, final long time, final String senderId,
            final String receiverId, boolean sync) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.sync = sync;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull final String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(final String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(final String senderId) {
        this.senderId = senderId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(final long time) {
        this.time = time;
    }

    public boolean isBelongToCurrentUser() {
        String senderId = String
                .valueOf(TwitterCore.getInstance().getSessionManager().getActiveSession().getUserId());
        return this.senderId.equals(senderId);
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(final boolean sync) {
        this.sync = sync;
    }
}
