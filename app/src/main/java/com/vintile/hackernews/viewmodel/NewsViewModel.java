package com.vintile.hackernews.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.vintile.hackernews.model.HackerNews;
import com.vintile.hackernews.model.Hit;
import com.vintile.hackernews.util.Api;
import com.vintile.hackernews.util.ApiClient;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsViewModel extends AndroidViewModel {
    NewsViewModel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<List<Hit>> hit = new MutableLiveData<>();

    public LiveData<List<Hit>> getNews(String searchString, int pageCount) {
        Api apiService =
                ApiClient.getClient().create(Api.class);

        Call<HackerNews> call = apiService.getNewsList(searchString, pageCount);
        call.enqueue(new Callback<HackerNews>() {
            @Override
            public void onResponse(@NonNull Call<HackerNews> call, @NonNull Response<HackerNews> response) {
                if (response.body().getHits() != null) {
                    hit.setValue(response.body().getHits());
                }
            }

            @Override
            public void onFailure(Call<HackerNews> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });
        return hit;
    }
}
