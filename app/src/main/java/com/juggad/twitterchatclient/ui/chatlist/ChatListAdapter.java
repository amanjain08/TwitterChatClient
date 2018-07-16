package com.juggad.twitterchatclient.ui.chatlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.juggad.twitterchatclient.R;
import com.juggad.twitterchatclient.database.entity.ChatItem;
import com.juggad.twitterchatclient.ui.CircleImageView;
import com.juggad.twitterchatclient.ui.chatlist.ChatListAdapter.ChatItemView;
import com.juggad.twitterchatclient.utils.Utils;
import com.squareup.picasso.Picasso;
import io.reactivex.subjects.PublishSubject;
import java.util.List;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatItemView> {

    Context mContext;

    private PublishSubject<ChatItem> clickSubject = PublishSubject.create();

    private List<ChatItem> mChatItems;

    ChatListAdapter(final List<ChatItem> chatItems, Context context) {
        mContext = context;
        mChatItems = chatItems;
    }

    public void addItems(List<ChatItem> chatItems) {
        mChatItems = chatItems;
        notifyDataSetChanged();
    }

    public io.reactivex.Observable<ChatItem> getClickEvent() {
        return clickSubject;
    }

    @Override
    public int getItemCount() {
        return mChatItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatItemView holder, final int position) {
        final ChatItem chatItem = mChatItems.get(position);
        holder.mName.setText(Utils.toCamelCase(chatItem.getTheirName()));
        holder.mMessage.setText(chatItem.getMessage());
        CharSequence relativeDateTimeString = DateUtils
                .getRelativeDateTimeString(mContext, chatItem.getTime() - 1000, DateUtils.SECOND_IN_MILLIS,
                        DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_SHOW_DATE);
        holder.mTime.setText(relativeDateTimeString);
        Picasso.get().load(chatItem.getProfilePictureUrl().replace("_normal", "")).into(holder.mProfilePicture);
        holder.itemView.setOnClickListener(v -> clickSubject.onNext(chatItem));
    }

    @NonNull
    @Override
    public ChatItemView onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_view, parent, false);
        return new ChatItemView(view);
    }

    class ChatItemView extends RecyclerView.ViewHolder {

        TextView mName, mMessage, mTime;

        CircleImageView mProfilePicture;

        ChatItemView(final View itemView) {
            super(itemView);
            mProfilePicture = itemView.findViewById(R.id.profile_picture);
            mName = itemView.findViewById(R.id.name);
            mMessage = itemView.findViewById(R.id.message_highlight);
            mTime = itemView.findViewById(R.id.time);
        }
    }
}
