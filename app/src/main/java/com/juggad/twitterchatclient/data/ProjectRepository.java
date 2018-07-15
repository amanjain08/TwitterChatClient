package com.juggad.twitterchatclient.data;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;
import android.util.Pair;
import com.juggad.twitterchatclient.api.TwitterRestApiClient;
import com.juggad.twitterchatclient.database.BufferChatMessageDaoImpl;
import com.juggad.twitterchatclient.database.UserDetailDaoImpl;
import com.juggad.twitterchatclient.database.dao.BufferChatMessageDao;
import com.juggad.twitterchatclient.database.dao.UserDetailDao;
import com.juggad.twitterchatclient.database.entity.ChatMessage;
import com.juggad.twitterchatclient.database.entity.UserEntity;
import com.juggad.twitterchatclient.database.mapper.MessageEventToChatMessageMapper;
import com.juggad.twitterchatclient.database.mapper.UserToUserEntityMapper;
import com.juggad.twitterchatclient.model.DirectMessageEvent;
import com.juggad.twitterchatclient.model.MessageEvent;
import com.juggad.twitterchatclient.model.MessageEventList;
import com.juggad.twitterchatclient.utils.Resource;
import com.juggad.twitterchatclient.utils.Status;
import com.juggad.twitterchatclient.utils.Utils;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.single.SingleToObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class ProjectRepository {

    private static ProjectRepository sInstance;

    private static final String TAG = ProjectRepository.class.getSimpleName();

    private String currentUserId;

    private HashSet<String> cachedUserIds = new HashSet<>();

    private BufferChatMessageDao mBufferChatMessageDao;

    private TwitterRestApiClient mTwitterApiClient;

    private UserDetailDao mUserDetailDao;

    private String mCursor = null;

    public MutableLiveData<Resource<String>> mStatusLiveData = new MutableLiveData<>();

    Context mContext;

    public static ProjectRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ProjectRepository.class) {
                if (sInstance == null) {
                    sInstance = new ProjectRepository(context);
                }
            }
        }
        return sInstance;
    }

    private ProjectRepository(Context context) {
        mContext = context;
        mBufferChatMessageDao = new BufferChatMessageDaoImpl(context);
        final TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        currentUserId = String.valueOf(twitterSession.getUserId());
        mTwitterApiClient = new TwitterRestApiClient(twitterSession);
        mUserDetailDao = new UserDetailDaoImpl(context);
    }

    public void fetchMoreChats() {
        getChats(false, mCursor);
    }

    public void loadFirstChats() {
        getChats(true, null);
    }

    public void refreshChats() {
        getChats(false, null);
    }

    /**
     * This method fetches the message event from twitter for given user
     */
    private void getChats(boolean showStatus, String cursor) {
        Callback<MessageEventList> messageEventListCallback = new Callback<MessageEventList>() {
            @Override
            public void failure(final TwitterException exception) {
                Log.e(TAG, "onFailure: ", exception);
                mStatusLiveData.postValue(
                        new Resource<>(Status.ERROR, null, exception.getMessage()));

            }

            @Override
            public void success(final Result<MessageEventList> result) {
                mStatusLiveData.postValue(new Resource<>(Status.SUCCESS, null, null));
                if (result.data != null && result.data.getEvents() != null && !result.data.getEvents().isEmpty()) {
                    saveToDB(showStatus, result.data.getEvents());
                    mCursor = result.data.getNextCursor();
                }
            }
        };
        mTwitterApiClient.directMessage().directMessages(cursor).enqueue(messageEventListCallback);
    }

    /**
     * This method retrieves unique userIds from the messages event and then fetch user model from twitter for these userIds
     * then update user table with these user models then store messages to chat message DB
     */
    private void saveToDB(final boolean showStatus, final List<MessageEvent> messageEvents) {
        Observable<List<User>> userList = fetchUserDetails(messageEvents);

        userList
                .flatMap(getFlatMapIterable())
                .map(getMapper())
                .toList()
                .subscribeOn(Schedulers.io())
                .flatMap(storeUsersToDB())
                .observeOn(Schedulers.io())
                .subscribe(saveMessagesToDB(showStatus, messageEvents));
    }

    @NonNull
    private Function<List<User>, ObservableSource<User>> getFlatMapIterable() {
        return users -> Observable.fromIterable(users);
    }

    @NonNull
    private Function<User, UserEntity> getMapper() {
        return user -> new UserToUserEntityMapper().modelToEntity(user);
    }

    /**
     * This method first store message that need to be sent to chatmessage DB and
     * then make POST API call to send the message to recipient user and when success response receives
     * then update the entry on that chat message table for this message
     */
    public void sendMessage(final DirectMessageEvent directMessageEvent) {
        directMessageEvent.getMessageEvent().getMessageCreate().setSenderId(currentUserId);
        SingleToObservable
                .create((ObservableOnSubscribe<Pair<String, DirectMessageEvent>>) emitter -> {
                    String randomId = UUID.randomUUID().toString();
                    directMessageEvent.getMessageEvent().getMessageCreate().setSenderId(currentUserId);
                    directMessageEvent.getMessageEvent().setId(randomId);
                    emitter.onNext(new Pair<>(randomId, directMessageEvent));
                })
                .flatMap((Function<Pair<String, DirectMessageEvent>, ObservableSource<?>>) pair -> {
                    mBufferChatMessageDao.addMessage(new MessageEventToChatMessageMapper()
                            .modelToEntity(pair.second.getMessageEvent()));
                    return sendMessageToAPI(pair).subscribeOn(Schedulers.io());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Object>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(final Throwable e) {

                    }

                    @Override
                    public void onNext(final Object o) {
                        refreshChats();
                    }
                });
    }

    /**
     * Method fetches user Model from twitter API for uniques user Ids
     */
    private Observable<List<User>> fetchUserDetails(final List<MessageEvent> events) {
        return Observable.create(emitter -> {
            final HashSet hashSet = getUniqueUserIds(events);
            if (!hashSet.isEmpty()) {
                Callback<List<User>> callback = new Callback<List<User>>() {
                    @Override
                    public void failure(final TwitterException exception) {
                        Log.e(TAG, "failure: ", exception);
                    }

                    @Override
                    public void success(final Result<List<User>> result) {
                        Log.i(TAG, "success: Fetched Users");
                        emitter.onNext(result.data);
                        cachedUserIds.addAll(hashSet);
                        emitter.onComplete();
                    }
                };
                Iterator<String> iterator = hashSet.iterator();
                StringBuilder stringBuilder = new StringBuilder();
                while (iterator.hasNext()) {
                    stringBuilder.append(iterator.next());
                    stringBuilder.append(",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                mTwitterApiClient.userDetail().getUsers(stringBuilder.toString()).enqueue(callback);
            } else {
                emitter.onComplete();
            }
        });
    }

    /**
     * This method makes an API call to send message to the recipient
     */
    private Observable<Pair<String, DirectMessageEvent>> sendMessageToAPI(
            final Pair<String, DirectMessageEvent> pair) {
        return Observable.create(emitter -> {
            if (!Utils.isInternetConnected(mContext)) {
                emitter.onError(new Throwable("No internet connection"));
                return;
            }
            Callback<DirectMessageEvent> directMessageEventCallback = new Callback<DirectMessageEvent>() {
                @Override
                public void failure(final TwitterException exception) {
                    emitter.onError(exception);
                }

                @Override
                public void success(final Result<DirectMessageEvent> result) {
                    Log.i(TAG, "success: MessageSent");
                    Observable.fromCallable(() -> {
                        mBufferChatMessageDao.updateMessage(new MessageEventToChatMessageMapper()
                                .modelToEntity(pair.second.getMessageEvent()), pair.first);
                        return result.data;
                    }).subscribeOn(Schedulers.io()).subscribe();
                    emitter.onNext(new Pair(pair.first, result.data));
                    emitter.onComplete();
                }
            };
            mTwitterApiClient.directMessage().createMessage(pair.second).enqueue(directMessageEventCallback);
        });
    }

    /**
     * Get Unique userIds for given message events
     */
    private HashSet<String> getUniqueUserIds(List<MessageEvent> messageEvents) {
        HashSet<String> userIds = new HashSet<>();
        for (MessageEvent messageEvent : messageEvents) {
            userIds.add(messageEvent.getMessageCreate().getSenderId());
            userIds.add(messageEvent.getMessageCreate().getTarget().getRecipientId());
        }
        userIds.removeAll(cachedUserIds);
        return userIds;
    }

    @NonNull
    private DisposableSingleObserver<Boolean> saveMessagesToDB(final boolean showStatus,
            final List<MessageEvent> messageEvents) {
        return new DisposableSingleObserver<Boolean>() {
            @Override
            public void onSuccess(final Boolean aBoolean) {
                final MessageEventToChatMessageMapper messageEventToChatMessageMapper
                        = new MessageEventToChatMessageMapper();
                saveMessagesToDB(messageEvents, messageEventToChatMessageMapper);
            }

            @Override
            public void onError(final Throwable e) {
                Log.e(TAG, "onError: ", e);
                if (showStatus) {
                    mStatusLiveData.postValue(new Resource<>(Status.ERROR, null, e.getLocalizedMessage()));
                }
            }
        };
    }

    /**
     * Save messages to DB
     */
    private void saveMessagesToDB(final List<MessageEvent> messageEvents,
            final MessageEventToChatMessageMapper messageEventToChatMessageMapper) {
        getObservable(messageEvents)
                .map(messageEvent -> messageEventToChatMessageMapper.modelToEntity(messageEvent))
                .toList()
                .subscribeOn(Schedulers.io())
                .flatMap(
                        (Function<List<ChatMessage>, SingleSource<List<ChatMessage>>>) chatMessages -> Single.create(
                                (SingleOnSubscribe<List<ChatMessage>>) emitter -> {
                                    mBufferChatMessageDao.addMessages(chatMessages);
                                    emitter.onSuccess(chatMessages);
                                }).subscribeOn(Schedulers.io())).subscribe();

    }

    private io.reactivex.Observable<MessageEvent> getObservable(List<MessageEvent> messageEvents) {
        return io.reactivex.Observable.fromIterable(messageEvents);
    }

    @NonNull
    private Function<List<UserEntity>, SingleSource<Boolean>> storeUsersToDB() {
        return userEntityList -> Single.create((SingleOnSubscribe<Boolean>) emitter -> {
            mUserDetailDao.addUsers(userEntityList);
            emitter.onSuccess(true);
        }).subscribeOn(Schedulers.io());
    }


}
