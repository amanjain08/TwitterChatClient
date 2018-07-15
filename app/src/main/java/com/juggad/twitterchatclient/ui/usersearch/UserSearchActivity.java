package com.juggad.twitterchatclient.ui.usersearch;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.juggad.twitterchatclient.R;
import com.juggad.twitterchatclient.database.entity.UserEntity;
import com.juggad.twitterchatclient.ui.CircleImageView;
import com.juggad.twitterchatclient.ui.chatmessage.ChatMessageActivity;
import com.juggad.twitterchatclient.utils.Constants;
import com.juggad.twitterchatclient.viewmodel.UserSearchViewModel;
import io.reactivex.observers.DefaultObserver;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class UserSearchActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    UserSearchAdapter mUserSearchAdapter;

    UserSearchViewModel mUserSearchViewModel;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
        initUI();
        mUserSearchViewModel = ViewModelProviders.of(this).get(UserSearchViewModel.class);
        getUsers(mUserSearchViewModel);
    }

    private void initUI() {
        setToolbar();
        mRecyclerView = findViewById(R.id.recycler_view_user_search);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserSearchAdapter = new UserSearchAdapter();
        mRecyclerView.setAdapter(mUserSearchAdapter);

        mUserSearchAdapter.mPublishSubject.subscribe(new DefaultObserver<UserEntity>() {
            @Override
            public void onNext(final UserEntity userEntity) {
                Intent intent = new Intent(UserSearchActivity.this, ChatMessageActivity.class);
                intent.putExtra(Constants.USER_KEY, userEntity);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(final Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        CircleImageView circleImageView = findViewById(R.id.profile_picture);
        circleImageView.setVisibility(View.GONE);
        TextView title = findViewById(R.id.name);
        title.setText(R.string.new_message);
        setSupportActionBar(toolbar);
        //To show back button in toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getUsers(final UserSearchViewModel userSearchViewModel) {
        userSearchViewModel.mUsers.observe(this, userEntities -> {
            mUserSearchAdapter.submitList(userEntities);
        });
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
