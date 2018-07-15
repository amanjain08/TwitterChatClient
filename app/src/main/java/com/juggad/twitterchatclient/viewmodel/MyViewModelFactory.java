package com.juggad.twitterchatclient.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class MyViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;

    private String mParam;


    public MyViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        return (T) new ChatMessageViewModel(mApplication, mParam);
    }
}