package com.juggad.twitterchatclient.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.juggad.twitterchatclient.data.ProjectRepository;
import com.juggad.twitterchatclient.database.BufferChatMessageDaoImpl;
import com.juggad.twitterchatclient.database.dao.BufferChatMessageDao;
import com.juggad.twitterchatclient.database.entity.ChatItem;
import com.juggad.twitterchatclient.utils.Resource;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class ChatListViewModel extends AndroidViewModel {

    private static final String TAG = ChatListViewModel.class.getSimpleName();

    private ProjectRepository mProjectRepository;

    private LiveData<List<ChatItem>> mChatItemList;

    private LiveData<Resource<String>> mStatusLiveData;

    private BufferChatMessageDao mChatMessageDao;

    public ChatListViewModel(@NonNull final Application application) {
        super(application);
        mChatMessageDao = new BufferChatMessageDaoImpl(application);
        final TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        mChatItemList = mChatMessageDao.getLatestChats(String.valueOf(twitterSession.getUserId()));
        mProjectRepository = ProjectRepository.getInstance(application);
        mStatusLiveData = mProjectRepository.mStatusLiveData;
    }

    public LiveData<List<ChatItem>> getChatItemList() {
        return mChatItemList;
    }

    public void loadFirstChats() {
        mProjectRepository.loadFirstChats();
    }

    public void refreshChatList() {
        mProjectRepository.refreshChats();
    }

    public LiveData<Resource<String>> getStatusLiveData() {
        return mStatusLiveData;
    }

    public void clearDB() {
        mChatMessageDao.deleteAllChatsAndUser();
    }
}
