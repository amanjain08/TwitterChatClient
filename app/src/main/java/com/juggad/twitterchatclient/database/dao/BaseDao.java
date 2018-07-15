package com.juggad.twitterchatclient.database.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

/**
 * Created by Aman Jain on 15/07/18.
 */
public interface BaseDao<T> {

    @Delete
    void delete(T object);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T object);

    @Update
    void update(T object);
}
