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
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.vintile.hackernews.MyApplication;
import com.vintile.hackernews.R;
import com.vintile.hackernews.databinding.ActivityNewsHeadlineBinding;
import com.vintile.hackernews.model.Hit;
import com.vintile.hackernews.util.AppConstants;
import com.vintile.hackernews.view.adapter.NewsAdapter;
import com.vintile.hackernews.viewmodel.NewsModelFactory;
import com.vintile.hackernews.viewmodel.NewsViewModel;

import java.util.List;
import java.util.Objects;

import static com.vintile.hackernews.MyApplication.getContext;

public class NewsHeadlineActivity extends AppCompatActivity {


    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private final int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    private NewsAdapter adapter;
    private NewsViewModel model;
    private final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyApplication.getContext(), RecyclerView.VERTICAL, false);
    private ItemOffsetDecoration itemDecoration;

    private ActivityNewsHeadlineBinding activityNewsHeadlineBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String searchString = getIntent().getStringExtra(AppConstants.SEARCH_STRING);
        Toast.makeText(this, searchString, Toast.LENGTH_LONG).show();

        activityNewsHeadlineBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_headline);

        adapter = new NewsAdapter();
        NewsModelFactory newsModelFactory = new NewsModelFactory();
//        itemDecoration = new ItemOffsetDecoration(Objects.requireNonNull(getContext()));
        model = ViewModelProviders.of(this, new NewsModelFactory()).get(NewsViewModel.class);

        initRecyView();
        loadFirstPage();
    }

    /**
     * Initializing RecyclerView.
     */
    private void initRecyView() {
        activityNewsHeadlineBinding.recyclerView.setHasFixedSize(true);
        activityNewsHeadlineBinding.recyclerView.setLayoutManager(linearLayoutManager);
       // activityNewsHeadlineBinding.recyclerView.addItemDecoration(itemDecoration);
        activityNewsHeadlineBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        activityNewsHeadlineBinding.recyclerView.addOnScrollListener(paginationScrollListener);
        activityNewsHeadlineBinding.setNewsAdapter(adapter);
    }

    /**
     * Load initial data
     */
    private void loadFirstPage() {

        model.getNews().observe(this, new Observer<List<Hit>>() {
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
                    loadFirstPage();
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
