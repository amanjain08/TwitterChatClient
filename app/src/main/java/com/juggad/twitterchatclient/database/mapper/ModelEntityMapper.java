package com.juggad.twitterchatclient.database.mapper;

/**
 * Created by Aman Jain on 15/07/18.
 */
public interface ModelEntityMapper<M, E> {

    M entityToModel(E entity);

    E modelToEntity(M model);
}
