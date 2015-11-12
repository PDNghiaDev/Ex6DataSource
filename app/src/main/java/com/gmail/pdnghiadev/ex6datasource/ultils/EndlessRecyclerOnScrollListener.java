package com.gmail.pdnghiadev.ex6datasource.ultils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by PDNghiaDev on 11/7/2015.
 */
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0; // The total number of items in dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data load
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more
    int fistVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        fistVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        // Refresh
        if (fistVisibleItem == 0){
            onRefresh();
        }else {
            notRefresh();
        }

        if (loading){
            if (totalItemCount > previousTotal){
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (fistVisibleItem + visibleThreshold)){
            // End has been reached

            // Do something
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
    }

    protected abstract void notRefresh();

    protected abstract void onRefresh();

    protected abstract void onLoadMore(int current_page);
}
