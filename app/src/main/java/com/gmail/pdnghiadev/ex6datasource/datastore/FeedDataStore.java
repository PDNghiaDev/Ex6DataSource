package com.gmail.pdnghiadev.ex6datasource.datastore;

import android.content.Context;

import com.gmail.pdnghiadev.ex6datasource.model.Children;
import com.gmail.pdnghiadev.ex6datasource.model.RedditPost;

import java.util.List;

/**
 * Created by PDNghiaDev on 11/2/2015.
 */
public interface FeedDataStore {
    interface OnRedditPostRetrievedListener{
        void onRedditPostRetrieved(List<Children> childrens, String after, Exception ex);
    }

    void loadData();
    void loadMore();
}
