package com.juggad.twitterchatclient.database.dao;

import com.juggad.twitterchatclient.database.entity.UserEntity;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */
public interface UserDetailDao {

    void addUser(UserEntity userEntity);

    void addUsers(List<UserEntity> userEntityList);

    UserEntity getUser(String userId);

    List<String> getUserIds();

    List<UserEntity> getUsers(List<String> userIds);

    void updateUser(UserEntity userEntity);

    void updateUsers(List<UserEntity> userEntityList);
}
