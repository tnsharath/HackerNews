package com.vintile.hackernews.util;

import com.vintile.hackernews.model.HackerNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


@SuppressWarnings("WeakerAccess")
public interface Api {


    @GET("search/")
    Call<HackerNews> getNewsList(@Query("query") String cat, @Query("page") int pageCount);

}
