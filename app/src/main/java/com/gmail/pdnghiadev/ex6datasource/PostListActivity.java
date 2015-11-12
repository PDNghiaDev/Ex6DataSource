package com.gmail.pdnghiadev.ex6datasource;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gmail.pdnghiadev.ex6datasource.adapter.RedditAdapter;
import com.gmail.pdnghiadev.ex6datasource.model.Children;
import com.gmail.pdnghiadev.ex6datasource.model.ChildrenConverter;
import com.gmail.pdnghiadev.ex6datasource.model.CustomVolleyRequestQueue;
import com.gmail.pdnghiadev.ex6datasource.model.RedditPost;
import com.gmail.pdnghiadev.ex6datasource.model.RedditPostConverter;
import com.gmail.pdnghiadev.ex6datasource.ultils.EndlessRecyclerOnScrollListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostListActivity extends AppCompatActivity {

    private List<Children> mListChildren = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private RecyclerView mRecyclerView;
    private RedditAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Gson mGson;
    private NetworkInfo mNetworkInfo;
    private RelativeLayout mRelativeLayout;
    private LinearLayoutManager mLinearLayoutManager;

    private int counter = 0;
    private String afterId;
    private static final String ANDROIDDEV = "androiddev/new";
    private static final String SUBREDDIT_URL = "http://www.reddit.com/r/";
    private static final String JSON_END = "/.json";
    private static final String COUNT = "?count=";
    private static final String AFTER = "&after=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_list);

        loadComponents();

        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RedditPost.class, new RedditPostConverter());
        gsonBuilder.registerTypeAdapter(Children.class, new ChildrenConverter());
        mGson = gsonBuilder.create();

        // Check for network
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = connMgr.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isConnected()) {// Connected
            load(null);
        } else {//Not connect
            mRecyclerView.setVisibility(View.INVISIBLE);
            mRelativeLayout.setVisibility(View.VISIBLE);
        }
        mSwipeRefreshLayout.setEnabled(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setLoadMore();
    }

    // Set Load More for RecyclerView
    private void setLoadMore() {
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            protected void onLoadMore(int current_page) {
                int lastFistVisiblePosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPosition(lastFistVisiblePosition);

                Void[] pa = null;
                new BackgroundTask().execute(pa);

            }

            @Override
            protected void onRefresh() {
                mSwipeRefreshLayout.setEnabled(true);

                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mSwipeRefreshLayout.setRefreshing(true);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
                                    load(null);
                                }
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }, 3000);
                    }
                });
            }

            @Override
            protected void notRefresh() {
                mSwipeRefreshLayout.setEnabled(false);
            }
        });
    }

    // Load components of UI
    private void loadComponents() {
        mRecyclerView = (RecyclerView) findViewById(R.id.post_list);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.layout_not_connect);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
    }

    public void load(String after) {
        String subreddit = ANDROIDDEV;

        if (after == null) { //LoadData
            counter = 0;
            subreddit = SUBREDDIT_URL + subreddit + JSON_END;
            mAdapter = new RedditAdapter(mListChildren, getResources().getColor(R.color.colorStickyPost), getResources().getColor(android.R.color.black));
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.clearAdapter();
        } else { //LoadMore
            counter = counter + 25;
            subreddit = SUBREDDIT_URL + subreddit + JSON_END + COUNT + counter + AFTER + afterId;
        }

        mRequestQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, subreddit, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                RedditPost redditPost = mGson.fromJson(response.toString(), RedditPost.class);
                afterId = redditPost.getAfter();
                Collections.addAll(mListChildren, redditPost.getChildrens());
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error" + error.getMessage());
            }
        });

        mRequestQueue.add(jsonObjectRequest);

    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                mListChildren.add(null);
                Thread.sleep(3000);
                mListChildren.remove(null);
                mAdapter.notifyItemRemoved(mListChildren.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            load(afterId);
        }
    }

}
