package com.gmail.pdnghiadev.ex6datasource.datastore;

/**
 * Created by PDNghiaDev on 11/1/2015.
 */
public class NetworkBasedFeedDataStore implements FeedDataStore {
    private String baseUrl;

    @Override
    public void getPostList(String topic, String before, String after,
                            OnRedditPostRetrievedListener onRedditPostRetrievedListener) {
        // Implement network calls
    }

    public void setBaseUrl(String baseUrl){
        this.baseUrl = baseUrl;
    }


}
