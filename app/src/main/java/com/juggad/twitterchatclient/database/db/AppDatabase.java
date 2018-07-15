package com.juggad.twitterchatclient.database.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.juggad.twitterchatclient.database.dao.ChatMessageDaoAccess;
import com.juggad.twitterchatclient.database.dao.UserDetailDaoAccess;
import com.juggad.twitterchatclient.database.entity.ChatItem;
import com.juggad.twitterchatclient.database.entity.ChatMessage;
import com.juggad.twitterchatclient.database.entity.UserEntity;

@Database(entities = {UserEntity.class, ChatMessage.class, ChatItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static final String TAG = AppDatabase.class.getSimpleName();

    private static final String DB_NAME = "com.juggad.twitterchatclient.db";

    public static AppDatabase getInstance(Context context) {
        AppDatabase tmp = INSTANCE;
        if (tmp == null) {
            synchronized (AppDatabase.class) {
                tmp = INSTANCE;
                if (tmp == null) {
                    tmp = INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull final SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Log.d(TAG, "onCreate: ");
                                }

                                @Override
                                public void onOpen(@NonNull final SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    Log.d(TAG, "onOpen: ");
                                }
                            })
                            .build();
                }
                return tmp;
            }
        }
        return tmp;
    }


    public abstract ChatMessageDaoAccess getChatMessageDaoAccess();

    public abstract UserDetailDaoAccess getUserDetailDaoAccess();
}
