package com.gmail.pdnghiadev.ex6datasource.datastore;

import com.gmail.pdnghiadev.ex6datasource.model.RedditPost;

import java.util.List;

/**
 * Created by PDNghiaDev on 11/2/2015.
 */
public interface FeedDataStore {
    interface OnRedditPostRetrievedListener{
        void onRedditPostRetrieved(List<RedditPost> postList, String after, Exception ex);
    }

    void getPostList(String topic, String before, String after,
                     OnRedditPostRetrievedListener onRedditPostRetrievedListener);
}
