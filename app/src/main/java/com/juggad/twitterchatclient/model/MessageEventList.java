package com.juggad.twitterchatclient.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class MessageEventList {

    @SerializedName("events")
    private List<MessageEvent> events;

    @SerializedName("next_cursor")
    private String nextCursor;

    public MessageEventList(final String nextCursor,
            final List<MessageEvent> events) {
        this.nextCursor = nextCursor;
        this.events = events;
    }

    public List<MessageEvent> getEvents() {
        return events;
    }

    public void setEvents(final List<MessageEvent> events) {
        this.events = events;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(final String nextCursor) {
        this.nextCursor = nextCursor;
    }
}
