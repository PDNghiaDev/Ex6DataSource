package com.gmail.pdnghiadev.ex6datasource;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gmail.pdnghiadev.ex6datasource.adapter.RedditAdapter;
import com.gmail.pdnghiadev.ex6datasource.model.Children;
import com.gmail.pdnghiadev.ex6datasource.model.ChildrenConverter;
import com.gmail.pdnghiadev.ex6datasource.model.CustomVolleyRequestQueue;
import com.gmail.pdnghiadev.ex6datasource.model.RedditPost;
import com.gmail.pdnghiadev.ex6datasource.model.RedditPostConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostListActivity extends AppCompatActivity {
    public static final String TAG = "PostListActivity";
    private List<Children> mListChildren = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RedditAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Gson mGson;
    private NetworkInfo mNetworkInfo;
    private RelativeLayout mRelativeLayout, mBottomLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;

    private int counter = 0;
    private String afterId;
    private static final String ANDROIDDEV = "androiddev/new";
    private static final String SUBREDDIT_URL = "http://www.reddit.com/r/";
    private static final String JSON_END = "/.json";
    private static final String COUNT = "?count=";
    private static final String AFTER = "&after=";
    int fistVisibleItem, visibleItemCount, totalItemCount;
    boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_list);

        loadComponents();

        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mGridLayoutManager = new GridLayoutManager(this, 3);

        int ori = getWindowManager().getDefaultDisplay().getRotation();
        if (ori == Surface.ROTATION_0 || ori == Surface.ROTATION_180) {
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            scroll(mLinearLayoutManager);
        } else if (ori == Surface.ROTATION_90) {
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            scroll(mGridLayoutManager);
        }

        mSwipeRefreshLayout.setEnabled(false);

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

    }

    // Load components of UI
    private void loadComponents() {
        mRecyclerView = (RecyclerView) findViewById(R.id.post_list);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.layout_not_connect);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mBottomLayout = (RelativeLayout) findViewById(R.id.loadMoreItem);
    }

    public void load(String after) {
        String subreddit = ANDROIDDEV;

        if (after == null) { //LoadData
            subreddit = SUBREDDIT_URL + subreddit + JSON_END;
            mAdapter = new RedditAdapter(mListChildren, getResources().getColor(R.color.colorStickyPost), getResources().getColor(R.color.colorTitle));
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.clearAdapter();

        } else { //LoadMore
            counter = counter + 25;
            subreddit = SUBREDDIT_URL + subreddit + JSON_END + COUNT + counter + AFTER + afterId;
            Log.d("TAG", subreddit);

        }

        RequestQueue mRequestQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, subreddit, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mListChildren.remove(null);
                mSwipeRefreshLayout.setRefreshing(false);
                RedditPost redditPost = mGson.fromJson(response.toString(), RedditPost.class);
                afterId = redditPost.getAfter();
                Collections.addAll(mListChildren, redditPost.getChildrens());
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error" + error.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
                handleVolleyError(error);
            }
        });

        mRequestQueue.add(jsonObjectRequest);

    }

    private void handleVolleyError(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) { // Not connection wifi
            Log.d(TAG, "TimeoutError || NoConnectionError: " + error.getMessage());
            mRecyclerView.setVisibility(View.INVISIBLE);
            mRelativeLayout.setVisibility(View.VISIBLE);
        } else if (error instanceof AuthFailureError) {
            Log.d(TAG, "AuthFailureError: " + error.getMessage());
        } else if (error instanceof ServerError) { // Error 404
            Log.d(TAG, "ServerError: " + error.getMessage());
        } else if (error instanceof NetworkError) {
            Log.d(TAG, "NetworkError: " + error.getMessage());
        } else if (error instanceof ParseError) {
            Log.d(TAG, "ParseError: " + error.getMessage());
        }
    }

    public void scroll(final LinearLayoutManager manager) {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    loading = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = manager.getChildCount();
                totalItemCount = manager.getItemCount();
                fistVisibleItem = manager.findFirstVisibleItemPosition();


                if (fistVisibleItem == 0) { // Pull to refresh
                    mSwipeRefreshLayout.setEnabled(true);
                    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            mSwipeRefreshLayout.setRefreshing(true);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
                                        counter = 0;
                                        load(null); // LoadData
                                    }
                                }
                            }, 3000);

                        }
                    });
                } else { // Scroll content
                    mSwipeRefreshLayout.setEnabled(false);
                }

                if (loading && (visibleItemCount + fistVisibleItem) == totalItemCount) { // LoadMore

                    if (totalItemCount % 25 == 0){ // Check for condition to show LoadMore
                        loading = false;

//                    mBottomLayout.setVisibility(View.VISIBLE);
                        mListChildren.add(null);
                        mAdapter.notifyItemInserted(mListChildren.size());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                load(afterId); // LoadMore

//                            mBottomLayout.setVisibility(View.GONE);
                            }
                        }, 3000);
                    }


                }
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(mGridLayoutManager);
        }
    }
}
