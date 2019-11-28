package com.vintile.hackernews.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vintile.hackernews.R;
import com.vintile.hackernews.model.HackerNews;
import com.vintile.hackernews.model.Hit;
import com.vintile.hackernews.util.Api;
import com.vintile.hackernews.util.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsViewModel extends AndroidViewModel {
    public NewsViewModel(@NonNull Application application) {
        super(application);
    }
    MutableLiveData<List<Hit>> hit = new MutableLiveData<List<Hit>>();
    public LiveData<List<Hit>> getNews() {
        Api apiService =
                ApiClient.getClient().create(Api.class);

        Call<HackerNews> call = apiService.getNewsList("Sport", 0);
        call.enqueue(new Callback<HackerNews>() {
            @Override
            public void onResponse(Call<HackerNews> call, Response<HackerNews> response) {
               hit.setValue(response.body().getHits());
            }

            @Override
            public void onFailure(Call<HackerNews> call, Throwable t) {
                // Log error here since request failed
                Log.e("Error", t.toString());
            }
        });
        return hit;
    }
}
