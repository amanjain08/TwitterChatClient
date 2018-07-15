package com.juggad.twitterchatclient.database.mapper;

import com.juggad.twitterchatclient.database.entity.ChatMessage;
import com.juggad.twitterchatclient.model.MessageEvent;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class MessageEventToChatMessageMapper implements ModelEntityMapper<MessageEvent, ChatMessage> {

    @Override
    public MessageEvent entityToModel(final ChatMessage entity) {
        return null;
    }

    @Override
    public ChatMessage modelToEntity(final MessageEvent model) {
        return new ChatMessage(model.getId(), model.getMessageCreate().getMessageData().getText(),
                model.getCreatedTimestamp(), model.getMessageCreate().getSenderId(),
                model.getMessageCreate().getTarget().getRecipientId(), model.isSync());
    }
}
