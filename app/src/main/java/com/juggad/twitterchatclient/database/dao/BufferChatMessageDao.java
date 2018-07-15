package com.juggad.twitterchatclient.database.dao;

import android.arch.lifecycle.LiveData;
import com.juggad.twitterchatclient.database.entity.ChatItem;
import com.juggad.twitterchatclient.database.entity.ChatMessage;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */
public interface BufferChatMessageDao {

    void addMessage(ChatMessage chatMessage);

    void addMessages(List<ChatMessage> messages);

    LiveData<List<ChatItem>> getLatestChats(String myUserId);

    LiveData<List<ChatMessage>> getMessages(String theirId);

    void updateMessage(ChatMessage chatMessage, String oldId);

    void deleteAllChatsAndUser();

}
