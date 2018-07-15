package com.juggad.twitterchatclient.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.juggad.twitterchatclient.database.entity.UserEntity;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */
@Dao
public interface UserDetailDaoAccess extends BaseDao<UserEntity> {

    @Query("SELECT * FROM user where id = (:userId)")
    UserEntity getUser(String userId);

    @Query("SELECT id from user")
    List<String> getUserIds();

    @Query("SELECT * FROM user where id in (:userIds)")
    List<UserEntity> getUsers(List<String> userIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<UserEntity> userEntityList);

    @Update
    void updateUsers(List<UserEntity> userEntityList);
}
