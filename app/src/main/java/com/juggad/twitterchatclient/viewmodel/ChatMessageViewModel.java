package com.juggad.twitterchatclient.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import com.juggad.twitterchatclient.data.ProjectRepository;
import com.juggad.twitterchatclient.database.BufferChatMessageDaoImpl;
import com.juggad.twitterchatclient.database.dao.BufferChatMessageDao;
import com.juggad.twitterchatclient.database.entity.ChatMessage;
import com.juggad.twitterchatclient.model.DirectMessageEvent;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class ChatMessageViewModel extends ViewModel {

    private static final String TAG = ChatListViewModel.class.getSimpleName();

    ProjectRepository mProjectRepository;

    private BufferChatMessageDao mBufferChatMessageDao;

    private LiveData<List<ChatMessage>> mChatMessages;

    public ChatMessageViewModel(@NonNull final Application application, final String param) {
        mBufferChatMessageDao = new BufferChatMessageDaoImpl(application);
        mChatMessages = mBufferChatMessageDao.getMessages(param);
        mProjectRepository = ProjectRepository.getInstance(application);
    }

    public LiveData<List<ChatMessage>> getChatMessages() {
        return mChatMessages;
    }

    public void refreshMessage() {
        mProjectRepository.refreshChats();
    }

    public void sendMessage(final String message, final String theirId) {
        DirectMessageEvent directMessageEvent = new DirectMessageEvent(message, theirId);
        mProjectRepository.sendMessage(directMessageEvent);
    }
}
