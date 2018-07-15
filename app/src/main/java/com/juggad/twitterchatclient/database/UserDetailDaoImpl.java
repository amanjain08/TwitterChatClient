package com.juggad.twitterchatclient.database;

import android.content.Context;
import com.juggad.twitterchatclient.database.dao.UserDetailDao;
import com.juggad.twitterchatclient.database.dao.UserDetailDaoAccess;
import com.juggad.twitterchatclient.database.db.AppDatabase;
import com.juggad.twitterchatclient.database.entity.UserEntity;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class UserDetailDaoImpl implements UserDetailDao {

    private UserDetailDaoAccess mUserDetailDaoAccess;

    public UserDetailDaoImpl(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        mUserDetailDaoAccess = appDatabase.getUserDetailDaoAccess();
    }

    @Override
    public void addUser(final UserEntity userEntity) {
        mUserDetailDaoAccess.insert(userEntity);
    }

    @Override
    public void addUsers(final List<UserEntity> userEntityList) {
        mUserDetailDaoAccess.insertUsers(userEntityList);
    }

    @Override
    public UserEntity getUser(final String userId) {
        return mUserDetailDaoAccess.getUser(userId);
    }

    @Override
    public List<String> getUserIds() {
        return mUserDetailDaoAccess.getUserIds();
    }

    @Override
    public List<UserEntity> getUsers(final List<String> userIds) {
        return mUserDetailDaoAccess.getUsers(userIds);
    }

    @Override
    public void updateUser(final UserEntity userEntity) {
        mUserDetailDaoAccess.update(userEntity);
    }

    @Override
    public void updateUsers(final List<UserEntity> userEntityList) {
        mUserDetailDaoAccess.updateUsers(userEntityList);
    }
}
