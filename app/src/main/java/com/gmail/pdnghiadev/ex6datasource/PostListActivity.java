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
import android.util.Log;
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

    private List<Children> listChildren = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private RecyclerView mRecyclerView;
    private RedditAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Gson gson;
    private NetworkInfo networkInfo;
    private RelativeLayout mRelativeLayout;
    private LinearLayoutManager mLinearLayoutManager;

    private int counter = 0;
    private String after_id;
    private static final String androidNew = "androiddev/new";
    private static final String subredditUrl = "http://www.reddit.com/r/";
    private static final String jsonEnd = "/.json";
    private static final String qCount = "?count=";
    private static final String after = "&after=";

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
        gson = gsonBuilder.create();


        // Check for network
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {// Connected
            loadData(androidNew);
        } else {//Not connect
            mRecyclerView.setVisibility(View.INVISIBLE);
            mRelativeLayout.setVisibility(View.VISIBLE);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (networkInfo != null && networkInfo.isConnected()) {
                            loadData(androidNew);
                            setLoadMore();
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);

            }
        });

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
        });
    }

    // Load components of UI
    private void loadComponents() {
        mRecyclerView = (RecyclerView) findViewById(R.id.post_list);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.layout_not_connect);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
    }

    // Load data from server and parse JSON
    public void loadData(String subreddit) {
        counter = 0;

        subreddit = subredditUrl + subreddit + jsonEnd;

        adapter = new RedditAdapter(listChildren, getResources().getColor(R.color.colorStickyPost));
        mRecyclerView.setAdapter(adapter);

        mRequestQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        adapter.clearAdapter();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, subreddit, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                RedditPost redditPost = gson.fromJson(response.toString(), RedditPost.class);
                after_id = redditPost.getAfter();

                Collections.addAll(listChildren, redditPost.getChildrens());


                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error" + error.getMessage());

            }
        });

        mRequestQueue.add(jsonObjectRequest);
    }

    // LoadMore data from server and parse JSON
    public void loadMore(String subreddit) {
        counter = counter + 25;
        String count = String.valueOf(counter);
        subreddit = androidNew;

        subreddit = subredditUrl + subreddit + jsonEnd + qCount + count + after + after_id;

        Log.i("TAG", subreddit);

        mRequestQueue = CustomVolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, subreddit, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                RedditPost redditPost = gson.fromJson(response.toString(), RedditPost.class);
                after_id = redditPost.getAfter();
                Collections.addAll(listChildren, redditPost.getChildrens());
                adapter.notifyDataSetChanged();
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
                listChildren.add(null);

                Thread.sleep(3000);

                listChildren.remove(null);
                adapter.notifyItemRemoved(listChildren.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadMore(androidNew);
        }
    }

}
