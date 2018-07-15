package com.juggad.twitterchatclient.ui.chatmessage;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.juggad.twitterchatclient.R;
import com.juggad.twitterchatclient.database.entity.UserEntity;
import com.juggad.twitterchatclient.ui.CircleImageView;
import com.juggad.twitterchatclient.utils.Constants;
import com.juggad.twitterchatclient.utils.Utils;
import com.juggad.twitterchatclient.viewmodel.ChatMessageViewModel;
import com.juggad.twitterchatclient.viewmodel.MyViewModelFactory;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class ChatMessageActivity extends AppCompatActivity {

    UserEntity mUserEntity;

    ChatMessageAdapter mChatMessageAdapter;

    RecyclerView mChatMessageRecyclerView;

    ChatMessageViewModel mChatMessageViewModel;

    EditText message;

    ImageView sendButton;

    boolean firstTime = true;

    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);
        if (getIntent() == null || !getIntent().hasExtra(Constants.USER_KEY)) {
            finish();
            return;
        }
        mUserEntity = (UserEntity) getIntent().getSerializableExtra(Constants.USER_KEY);
        initView();
        mChatMessageViewModel = ViewModelProviders
                .of(this, new MyViewModelFactory(this.getApplication(), mUserEntity.getId()))
                .get(ChatMessageViewModel.class);
        setObserver(mChatMessageViewModel);
    }

    private void initView() {
        message = findViewById(R.id.message);
        sendButton = findViewById(R.id.send_button);
        setToolbar();

        sendButton.setOnClickListener(v -> {
            if (message.getText().toString().isEmpty()) {
                return;
            }
            mChatMessageViewModel.sendMessage(message.getText().toString(), mUserEntity.getId());
            message.getEditableText().clear();
            layoutManager.scrollToPosition(0);
        });
        mChatMessageRecyclerView = findViewById(R.id.recycler_view_chat_message);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setReverseLayout(true);
        layoutManager.scrollToPosition(0);
        mChatMessageRecyclerView.setLayoutManager(layoutManager);
        mChatMessageAdapter = new ChatMessageAdapter(new ArrayList<>(),this);
        mChatMessageRecyclerView.setAdapter(mChatMessageAdapter);
        mChatMessageRecyclerView.addOnLayoutChangeListener(
                (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> layoutManager
                        .scrollToPosition(0));
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        CircleImageView circleImageView = findViewById(R.id.profile_picture);
        Picasso.get().load(mUserEntity.getProfilePictureUrl().replaceAll("_normal", "")).into(circleImageView);
        TextView textView = findViewById(R.id.name);
        textView.setText(Utils.toCamelCase(mUserEntity.getName()));
        setSupportActionBar(toolbar);

        //To show back button in toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setObserver(final ChatMessageViewModel chatMessageViewModel) {
        chatMessageViewModel.getChatMessages().observe(this,
                chatMessages -> {
                    mChatMessageAdapter.addMessages(chatMessages);
                    if (firstTime) {
                        firstTime = false;
                        layoutManager.scrollToPosition(0);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isInternetConnected(this)) {
            mChatMessageViewModel.refreshMessage();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}