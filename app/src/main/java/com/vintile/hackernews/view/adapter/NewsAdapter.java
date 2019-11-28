package com.vintile.hackernews.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vintile.hackernews.R;
import com.vintile.hackernews.databinding.ItemNewsBinding;
import com.vintile.hackernews.model.Hit;
import com.vintile.hackernews.util.ClickInterface;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Hit> hitList;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    private ClickInterface clickInterface;
    public NewsAdapter(ClickInterface clickInterface) {
        this.hitList = new ArrayList<>();
        this.clickInterface = clickInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemNewsBinding itemNewsBinding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_news, parent, false);

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = new NewsViewHolder(itemNewsBinding);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Hit hit = hitList.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                final NewsViewHolder newsVH = (NewsViewHolder) holder;
                newsVH.itemNewsBinding.setHitsModel(hit);
                newsVH.itemNewsBinding.idTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickInterface.onClickNews(hit.getUrl());
                    }
                });
                break;

            case LOADING:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return hitList == null ? 0 : hitList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == hitList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    private class NewsViewHolder extends RecyclerView.ViewHolder {

        private ItemNewsBinding itemNewsBinding;

        private NewsViewHolder(ItemNewsBinding itemView) {
            super(itemView.getRoot());
            itemNewsBinding = itemView;

        }
    }

    private class LoadingVH extends RecyclerView.ViewHolder {

        private LoadingVH(View itemView) {
            super(itemView);
        }
    }

    private void add(Hit r) {
        hitList.add(r);
        notifyItemInserted(hitList.size() - 1);
    }

    public void addAll(List<Hit> moveResults) {
        hitList.addAll(moveResults);
        notifyDataSetChanged();
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Hit());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = hitList.size() - 1;
        Hit result = getItem(position);

        if (result != null) {
            hitList.remove(position);
            notifyItemRemoved(position);
        }
    }

    private Hit getItem(int position) {
        return hitList.get(position);
    }


}
