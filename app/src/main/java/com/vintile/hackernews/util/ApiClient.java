package com.vintile.hackernews.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vintile.hackernews.util.AppConstants.BASE_URL;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
