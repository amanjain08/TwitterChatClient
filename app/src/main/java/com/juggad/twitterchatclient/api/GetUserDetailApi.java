package com.juggad.twitterchatclient.api;

import com.juggad.twitterchatclient.model.UserList;
import com.twitter.sdk.android.core.models.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Aman Jain on 15/07/18.
 */
public interface GetUserDetailApi {

    @GET("1.1/users/lookup.json")
    Call<List<User>> getUsers(@Query("user_id") String userIds);

    @GET("1.1/followers/list.json?skip_status=true&include_user_entities=false")
    Call<UserList> getFollowers(@Query("cursor") String cursor, @Query("count") int count);

}