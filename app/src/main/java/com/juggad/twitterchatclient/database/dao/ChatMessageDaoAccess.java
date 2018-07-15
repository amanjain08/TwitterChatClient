package com.juggad.twitterchatclient.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.juggad.twitterchatclient.database.entity.ChatItem;
import com.juggad.twitterchatclient.database.entity.ChatMessage;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */

@Dao
public interface ChatMessageDaoAccess extends BaseDao<ChatMessage> {

    /**
     * Query fetches list of latest chat message between current and all other users.
     */
    @Query("SELECT user.id as their_id, user.name as their_name, user.profile_picture_url as profile_picture, messages.id as message_id, messages.message as message ,messages.time as time "
            +
            "FROM (" +
            " select chatmessage.id, chatmessage.sender_id, chatmessage.receiver_id, chatmessage.message,chatmessage.time "
            +
            " from chatmessage " +
            " JOIN " +
            " ( " +
            "       select min(sender_id, receiver_id) as x, max(sender_id, receiver_id) as y, max(time) as time " +
            "       from chatmessage " +
            "       group by x, y " +
            " ) uniquemessage " +
            " ON " +
            " uniquemessage.x = min(chatmessage.sender_id, chatmessage.receiver_id) " +
            " AND uniquemessage.y = max(chatmessage.sender_id, chatmessage.receiver_id) " +
            " AND uniquemessage.time = chatmessage.time " +
            ") as messages " +
            "JOIN USER " +
            "on user.id = messages.sender_id OR user.id = messages.receiver_id " +
            "WHERE user.id <> (:myUserId) " +
            "ORDER BY time DESC")
    LiveData<List<ChatItem>> getLatestChats(String myUserId);

    @Query("SELECT * FROM chatmessage WHERE sender_id = (:senderId) OR receiver_id = (:senderId) ORDER BY time DESC ")
    LiveData<List<ChatMessage>> getMessages(String senderId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ChatMessage> chatMessages);

    @Query("UPDATE chatmessage SET id = (:id), sender_id = (:senderId), time = (:time), sync = (:sync) WHERE id=(:oldId)")
    void updateChat(String oldId, String id, String senderId, long time, boolean sync);
}