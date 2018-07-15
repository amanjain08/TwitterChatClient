package com.juggad.twitterchatclient.ui.usersearch;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.juggad.twitterchatclient.R;
import com.juggad.twitterchatclient.database.entity.UserEntity;
import com.juggad.twitterchatclient.ui.usersearch.UserSearchAdapter.UserSearchView;
import com.juggad.twitterchatclient.utils.Utils;
import com.squareup.picasso.Picasso;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class UserSearchAdapter extends ListAdapter<UserEntity, UserSearchView> {


    UserSearchAdapter() {
        super(new UserEntity.TaskDiffCallback());
    }

    PublishSubject<UserEntity> mPublishSubject = PublishSubject.create();

    @NonNull
    @Override
    public UserSearchView onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_view, parent, false);
        return new UserSearchView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserSearchView holder, final int position) {
        UserEntity userEntity = getItem(position);
        holder.name.setText(Utils.toCamelCase(userEntity.getName()));
        Picasso.get().load(userEntity.getProfilePictureUrl().replaceAll("_normal", "")).into(holder.profilePicture);
        holder.itemView.setOnClickListener(v -> mPublishSubject.onNext(userEntity));
    }

    class UserSearchView extends RecyclerView.ViewHolder {

        TextView name;

        ImageView profilePicture;

        UserSearchView(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            profilePicture = itemView.findViewById(R.id.profile_picture);
            itemView.findViewById(R.id.time).setVisibility(View.GONE);
            itemView.findViewById(R.id.message_highlight).setVisibility(View.GONE);
        }
    }

}
