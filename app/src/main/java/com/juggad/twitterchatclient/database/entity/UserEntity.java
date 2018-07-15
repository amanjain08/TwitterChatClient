package com.juggad.twitterchatclient.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import java.io.Serializable;

/**
 * Created by Aman Jain on 15/07/18.
 */
@Entity(tableName = "user")
public class UserEntity implements Serializable{

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    String id;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "profile_picture_url")
    String profilePictureUrl;

    @ColumnInfo(name = "profile_url")
    String profileUrl;

    public UserEntity(@NonNull final String id, final String name, final String profilePictureUrl,
            final String profileUrl) {
        this.id = id;
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
        this.profileUrl = profileUrl;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(final String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(final String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public static class TaskDiffCallback extends DiffUtil.ItemCallback<UserEntity> {

        @Override
        public boolean areItemsTheSame(final UserEntity oldItem, final UserEntity newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(final UserEntity oldItem, final UserEntity newItem) {
            return oldItem.equals(newItem);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserEntity)) {
            return false;
        }
        final UserEntity that = (UserEntity) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                profilePictureUrl.equals(that.profilePictureUrl) &&
                profileUrl.equals(that.profileUrl);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
