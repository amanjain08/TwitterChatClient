package com.juggad.twitterchatclient.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.juggad.twitterchatclient.R;
import com.juggad.twitterchatclient.ui.userslist.ChatListActivity;
import com.juggad.twitterchatclient.utils.Status;
import com.juggad.twitterchatclient.viewmodel.SplashScreenViewModel;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Created by Aman Jain on 15/07/18.
 */
public class SplashScreen extends AppCompatActivity {

    private static final String TAG = SplashScreen.class.getSimpleName();

    ProgressBar mProgressBar;

    SplashScreenViewModel mSplashScreenViewModel;

    TwitterLoginButton mTwitterLoginButton;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        mSplashScreenViewModel = ViewModelProviders.of(this)
                .get(SplashScreenViewModel.class);

        observeViewModel(mSplashScreenViewModel);
        mSplashScreenViewModel.checkLoggin(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Adding the login result back to the button
        mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        mProgressBar = findViewById(R.id.progress_bar);
        mTwitterLoginButton = findViewById(R.id.login_button);
        mTwitterLoginButton.setOnClickListener(v -> {
            mProgressBar.setVisibility(View.VISIBLE);
            mTwitterLoginButton.setVisibility(View.GONE);
        });
        mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void failure(final TwitterException exception) {
                Log.e(TAG, "failure: ", exception);
                startLoginActivity();
            }

            @Override
            public void success(final Result<TwitterSession> result) {
                Log.d(TAG, "success: " + result.toString());
                Toast.makeText(SplashScreen.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                startChatActivity();
                finish();
            }
        });
    }

    private void observeViewModel(final SplashScreenViewModel splashScreenViewModel) {
        splashScreenViewModel.getAuthenticatorObservable().observe(this,
                loginAuthenticatorResource -> {
                    Status status = loginAuthenticatorResource.getStatus();
                    if (status == Status.SUCCESS) {
                        if (loginAuthenticatorResource.getData().isLoggedIn()) {
                            startChatActivity();
                            finish();
                        } else {
                            startLoginActivity();
                        }
                    }
                });
    }

    private void startChatActivity() {
        startActivity(new Intent(this, ChatListActivity.class));
    }

    private void startLoginActivity() {
        mProgressBar.setVisibility(View.GONE);
        mTwitterLoginButton.setVisibility(View.VISIBLE);
    }
}
