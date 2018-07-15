package com.juggad.twitterchatclient.database.mapper;

import com.juggad.twitterchatclient.database.entity.UserEntity;
import com.twitter.sdk.android.core.models.User;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class UserToUserEntityMapper implements ModelEntityMapper<User, UserEntity> {

    @Override
    public User entityToModel(final UserEntity entity) {
        return null;
    }

    @Override
    public UserEntity modelToEntity(final User model) {
        return new UserEntity(model.idStr, model.name, model.profileImageUrl, model.url);
    }
}
