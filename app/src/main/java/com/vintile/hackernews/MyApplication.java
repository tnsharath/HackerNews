package com.vintile.hackernews;

import android.app.Application;

/**
 * Created by Sharath TN on 2019-07-23.
 */
public class MyApplication extends Application {

    private static MyApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static MyApplication getContext() {
        return mContext;
    }
}