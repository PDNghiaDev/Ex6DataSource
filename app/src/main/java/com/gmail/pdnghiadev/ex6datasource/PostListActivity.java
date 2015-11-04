package com.gmail.pdnghiadev.ex6datasource;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

public class PostListActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    private List<Children> dataReddit = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private RecyclerView recyclerView;
    private RedditAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_list);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);

        recyclerView = (RecyclerView) findViewById(R.id.post_list);
        recyclerView.setHasFixedSize(true);

        // Check for network
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){// Connected

            mRequestQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                    .getRequestQueue();

            String url = "https://www.reddit.com/r/androiddev/new6456.json";
            JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), this, this);
            mRequestQueue.add(json);
        }else {//Not connect
            recyclerView.setVisibility(View.INVISIBLE);

            RelativeLayout re = (RelativeLayout) findViewById(R.id.layout_not_connect);
            re.setVisibility(View.VISIBLE);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RedditPost.class, new RedditPostConverter());
        gsonBuilder.registerTypeAdapter(Children.class, new ChildrenConverter());
        Gson gson = gsonBuilder.create();
        RedditPost redditPost = gson.fromJson(response.toString(), RedditPost.class);
        Collections.addAll(dataReddit, redditPost.getChildrens());
        adapter = new RedditAdapter(dataReddit, getResources().getColor(R.color.colorStickyPost));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

}
