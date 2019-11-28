package com.vintile.hackernews.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.vintile.hackernews.MyApplication;


/**
 * Created by Sharath TN on 2019-07-24.
 */
public class NewsModelFactory implements ViewModelProvider.Factory {

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(NewsViewModel.class)) {
            return (T) new NewsViewModel(MyApplication.getContext());
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
