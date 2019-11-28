package com.vintile.hackernews.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.vintile.hackernews.MyApplication;
import com.vintile.hackernews.R;
import com.vintile.hackernews.databinding.ActivityNewsHeadlineBinding;
import com.vintile.hackernews.model.Hit;
import com.vintile.hackernews.util.AppConstants;
import com.vintile.hackernews.util.ClickInterface;
import com.vintile.hackernews.view.adapter.NewsAdapter;
import com.vintile.hackernews.viewmodel.NewsModelFactory;
import com.vintile.hackernews.viewmodel.NewsViewModel;

import java.util.List;

public class NewsHeadlineActivity extends AppCompatActivity implements ClickInterface {

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private final int TOTAL_PAGES = 50;
    private int currentPage = PAGE_START;

    private NewsAdapter adapter;
    private NewsViewModel model;
    private final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyApplication.getContext(), RecyclerView.VERTICAL, false);
    private ItemOffsetDecoration itemDecoration;

    private ActivityNewsHeadlineBinding activityNewsHeadlineBinding;

    private String searchString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchString = getIntent().getStringExtra(AppConstants.SEARCH_STRING);
        activityNewsHeadlineBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_headline);
        adapter = new NewsAdapter(this);
        itemDecoration = new ItemOffsetDecoration(getApplicationContext());
        model = ViewModelProviders.of(this, new NewsModelFactory()).get(NewsViewModel.class);
        initRecyView();
        loadFirstPage(searchString, currentPage);
    }

    /**
     * Initializing RecyclerView.
     */
    private void initRecyView() {
        activityNewsHeadlineBinding.recyclerView.setHasFixedSize(true);
        activityNewsHeadlineBinding.recyclerView.setLayoutManager(linearLayoutManager);
        activityNewsHeadlineBinding.recyclerView.addItemDecoration(itemDecoration);
        activityNewsHeadlineBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        activityNewsHeadlineBinding.recyclerView.addOnScrollListener(paginationScrollListener);
        activityNewsHeadlineBinding.setNewsAdapter(adapter);
    }

    /**
     * Load initial data
     */
    private void loadFirstPage(String searchString, int pageCount) {

        model.getNews(searchString, pageCount).observe(this, new Observer<List<Hit>>() {
            @Override
            public void onChanged(@Nullable List<Hit> newsList) {
                if (newsList != null) {
                    activityNewsHeadlineBinding.progressBar.setVisibility(View.GONE);
                    adapter.addAll(newsList);
                }
                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }
        });
    }


    /**
     * Scroll listener
     */
    private final PaginationScrollListener paginationScrollListener = new PaginationScrollListener(linearLayoutManager) {
        @Override
        protected void loadMoreItems() {
            isLoading = true;
            currentPage += 1;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadNextPage();
                }
            }, 1000);
        }

        @Override
        public boolean isLastPage() {
            return isLastPage;
        }

        @Override
        public boolean isLoading() {
            return isLoading;
        }
    };

    /**
     * Load next set of data
     */
    private void loadNextPage() {

        adapter.removeLoadingFooter();
        isLoading = false;
        loadFirstPage(searchString, currentPage);
    }

    /**
     * Click on any News Item redirect to Detailed view of news
     * @param url String of detailed news url
     */
    @Override
    public void onClickNews(String url) {
        Intent httpIntent = new Intent(Intent.ACTION_VIEW);
        httpIntent.setData(Uri.parse(url));
        startActivity(httpIntent);
    }

    /**
     * Recyclerview item decoration
     */
    class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private final int mItemOffset;
        private ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }
        private ItemOffsetDecoration(@NonNull Context context) {
            this(context.getResources().getDimensionPixelSize(R.dimen.item_margin));
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent,
                                   @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }
}
