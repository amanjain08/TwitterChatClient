package com.juggad.twitterchatclient.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import com.juggad.twitterchatclient.database.dao.BufferChatMessageDao;
import com.juggad.twitterchatclient.database.dao.ChatMessageDaoAccess;
import com.juggad.twitterchatclient.database.db.AppDatabase;
import com.juggad.twitterchatclient.database.entity.ChatItem;
import com.juggad.twitterchatclient.database.entity.ChatMessage;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class BufferChatMessageDaoImpl implements BufferChatMessageDao {

    private ChatMessageDaoAccess mChatMessageDaoAccess;

    private AppDatabase appDatabase;

    public BufferChatMessageDaoImpl(Context context) {
        appDatabase = AppDatabase.getInstance(context);
        mChatMessageDaoAccess = appDatabase.getChatMessageDaoAccess();
    }

    @Override
    public void addMessage(final ChatMessage chatMessage) {
        mChatMessageDaoAccess.insert(chatMessage);
    }

    @Override
    public void addMessages(final List<ChatMessage> messages) {
        mChatMessageDaoAccess.insertAll(messages);
    }

    @Override
    public LiveData<List<ChatItem>> getLatestChats(final String myUserId) {
        return mChatMessageDaoAccess.getLatestChats(myUserId);
    }

    @Override
    public LiveData<List<ChatMessage>> getMessages(final String theirId) {
        return mChatMessageDaoAccess.getMessages(theirId);
    }

    @Override
    public void updateMessage(final ChatMessage chatMessage, final String oldId) {
        mChatMessageDaoAccess
                .updateChat(oldId, chatMessage.getId(), chatMessage.getSenderId(), chatMessage.getTime(), true);
    }

    @Override
    public void deleteAllChatsAndUser() {
        io.reactivex.Observable.fromCallable(() -> {
            appDatabase.clearAllTables();
            return true;
        }).subscribeOn(Schedulers.io()).subscribe();

    }
}
