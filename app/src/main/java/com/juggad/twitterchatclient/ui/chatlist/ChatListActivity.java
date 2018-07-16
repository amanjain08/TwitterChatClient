package com.juggad.twitterchatclient.ui.chatlist;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.TextView;
import android.widget.Toast;
import com.juggad.twitterchatclient.R;
import com.juggad.twitterchatclient.database.entity.ChatItem;
import com.juggad.twitterchatclient.database.entity.UserEntity;
import com.juggad.twitterchatclient.ui.CircleImageView;
import com.juggad.twitterchatclient.ui.chatmessage.ChatMessageActivity;
import com.juggad.twitterchatclient.ui.usersearch.UserSearchActivity;
import com.juggad.twitterchatclient.utils.Constants;
import com.juggad.twitterchatclient.utils.Status;
import com.juggad.twitterchatclient.utils.Utils;
import com.juggad.twitterchatclient.viewmodel.ChatListViewModel;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.TwitterCore;
import io.reactivex.observers.DefaultObserver;
import java.util.ArrayList;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class ChatListActivity extends AppCompatActivity implements OnRefreshListener {

    private ChatListAdapter mChatListAdapter;

    private ChatListViewModel mChatListViewModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        initView();
        mChatListViewModel = ViewModelProviders.of(this).get(ChatListViewModel.class);
        observeViewModel(mChatListViewModel);
        if (Utils.isInternetConnected(this)) {
            mSwipeRefreshLayout.setRefreshing(true);
            mChatListViewModel.loadFirstChats();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSwipeRefreshLayout.isRefreshing()) {
            return;
        }
        if (Utils.isInternetConnected(this)) {
            mChatListViewModel.refreshChatList();
        }
    }

    @NonNull
    private DividerItemDecoration getItemDecoration() {
        int[] attrs = new int[]{android.R.attr.listDivider};
        TypedArray a = obtainStyledAttributes(attrs);
        Drawable divider = a.getDrawable(0);
        int inset = getResources().getDimensionPixelSize(R.dimen.divider_margin);
        InsetDrawable insetDivider = new InsetDrawable(divider, 2 * inset, 0, inset, 0);
        a.recycle();
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(insetDivider);
        return itemDecorator;
    }

    private void initView() {
        setToolbar();
        final FloatingActionButton createMessageButton = findViewById(R.id.create_message);
        createMessageButton.setOnClickListener(v -> {
            if (!Utils.isInternetConnected(ChatListActivity.this)) {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(ChatListActivity.this, UserSearchActivity.class));
        });

        final RecyclerView chatItemsRecyclerView = findViewById(R.id.recycler_view_chat);
        chatItemsRecyclerView.addItemDecoration(getItemDecoration());
        chatItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatListAdapter = new ChatListAdapter(new ArrayList<>(), this);
        mChatListAdapter.getClickEvent().subscribe(new DefaultObserver<ChatItem>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(final Throwable e) {

            }

            @Override
            public void onNext(final ChatItem chatItem) {
                UserEntity userEntity = new UserEntity(chatItem.getTheirId(), chatItem.theirName,
                        chatItem.profilePictureUrl, null);
                Intent intent = new Intent(ChatListActivity.this, ChatMessageActivity.class);
                intent.putExtra(Constants.USER_KEY, userEntity);
                startActivity(intent);
            }
        });
        chatItemsRecyclerView.setAdapter(mChatListAdapter);

        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
    }

    @Override
    public void onRefresh() {
        if (Utils.isInternetConnected(this)) {
            mChatListViewModel.refreshChatList();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void observeViewModel(final ChatListViewModel chatListViewModel) {
        chatListViewModel.getChatItemList().observe(this, chatItems -> {
            mChatListAdapter.addItems(chatItems);
            mSwipeRefreshLayout.setRefreshing(false);
        });
        chatListViewModel.getStatusLiveData().observe(this, stringResource -> {
            mSwipeRefreshLayout.setRefreshing(false);
            if (stringResource.getStatus() == Status.ERROR) {
                Toast.makeText(ChatListActivity.this, stringResource.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        CircleImageView circleImageView = findViewById(R.id.profile_picture);
        circleImageView.setBorderWidth(0);
        TextView title = findViewById(R.id.name);
        title.setText(R.string.twitter_chat);
        Picasso.get().load(R.drawable.twitter_blue_logo).into(circleImageView);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.log_out);
            //To show back button in toolbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            new Builder(this).setMessage("Log out ?").setPositiveButton("Yes", (dialog, which) -> {
                CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeSessionCookie();
                TwitterCore.getInstance().getSessionManager().clearActiveSession();
                mChatListViewModel.clearDB();
                finish();
                Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show();
            }).setNegativeButton("No", null).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
