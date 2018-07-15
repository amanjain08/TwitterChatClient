package com.juggad.twitterchatclient.api;

import com.juggad.twitterchatclient.model.DirectMessageEvent;
import com.juggad.twitterchatclient.model.MessageEventList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Aman Jain on 15/07/18.
 */
public interface DirectMessageApi {

    @POST("/1.1/direct_messages/events/new.json")
    Call<DirectMessageEvent> createMessage(@Body DirectMessageEvent messageEvent);

    @GET("/1.1/direct_messages/events/list.json?count=200")
    Call<MessageEventList> directMessages(@Query("cursor") String cursor);
}
