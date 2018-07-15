package com.juggad.twitterchatclient.ui.chatmessage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.juggad.twitterchatclient.R;
import com.juggad.twitterchatclient.database.entity.ChatMessage;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int ITEM_MY_MESSAGE = 0;

    private static final int ITEM_THIER_MESSAGE = 1;

    private List<ChatMessage> mChatMessages;

    Context mContext;

    ChatMessageAdapter(final List<ChatMessage> chatMessages, Context context) {
        mContext = context;
        mChatMessages = chatMessages;
    }

    public void addMessages(List<ChatMessage> chatMessages) {
        mChatMessages = chatMessages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mChatMessages.size();
    }

    @Override
    public int getItemViewType(final int position) {
        ChatMessage message = mChatMessages.get(position);
        if (message.isBelongToCurrentUser()) {
            return ITEM_MY_MESSAGE;
        } else {
            return ITEM_THIER_MESSAGE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ChatMessage chatMessage = mChatMessages.get(position);
        CharSequence relativeDateTimeString = DateUtils
                .getRelativeDateTimeString(mContext, chatMessage.getTime() - 1000, DateUtils.SECOND_IN_MILLIS,
                        DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);
        if (holder instanceof ChatMyMessageViewHolder) {
            ((ChatMyMessageViewHolder) holder).mMessage.setText(chatMessage.getMessage());
            ((ChatMyMessageViewHolder) holder).mTime.setText(relativeDateTimeString);
        } else {
            ((ChatTheirMessageViewHolder) holder).mMessage.setText(chatMessage.getMessage());
            ((ChatTheirMessageViewHolder) holder).mTime.setText(relativeDateTimeString);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view;
        if (viewType == ITEM_MY_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_my_message, parent, false);
            return new ChatMyMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_their_message, parent, false);
        }
        return new ChatTheirMessageViewHolder(view);
    }

    class ChatMyMessageViewHolder extends RecyclerView.ViewHolder {

        TextView mMessage, mTime;

        ChatMyMessageViewHolder(final View itemView) {
            super(itemView);
            mMessage = itemView.findViewById(R.id.message_body);
            mTime = itemView.findViewById(R.id.time);
        }
    }

    class ChatTheirMessageViewHolder extends RecyclerView.ViewHolder {

        TextView mMessage, mTime;

        ChatTheirMessageViewHolder(final View itemView) {
            super(itemView);
            mMessage = itemView.findViewById(R.id.message_body);
            mTime = itemView.findViewById(R.id.time);
        }
    }
}
