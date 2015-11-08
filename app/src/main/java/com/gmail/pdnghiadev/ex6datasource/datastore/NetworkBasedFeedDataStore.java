package com.gmail.pdnghiadev.ex6datasource.datastore;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by PDNghiaDev on 11/1/2015.
 */
public class NetworkBasedFeedDataStore implements FeedDataStore{
    private String baseUrl;
    private RequestQueue requestQueue;
    private Gson gson;
    private List<Children> childrens = new ArrayList<Children>();;
    private RedditAdapter adapter;
    private String subreddit;
    private Context context;
    private String after_id;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public NetworkBasedFeedDataStore(Context context, List<Children> childrens, RedditAdapter adapter, String subreddit){
        this.context = context;
        this.childrens = childrens;
        this.adapter = adapter;
        this.subreddit = subreddit;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RedditPost.class, new RedditPostConverter());
        gsonBuilder.registerTypeAdapter(Children.class, new ChildrenConverter());
        gson = gsonBuilder.create();
    }


    @Override
    public void loadData() {
        requestQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();

        adapter.clearAdapter();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, subreddit, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                RedditPost redditPost = gson.fromJson(response.toString(), RedditPost.class);
                after_id = redditPost.getAfter();

                Collections.addAll(childrens, redditPost.getChildrens());


                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error" + error.getMessage());

            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void loadMore() {
        requestQueue = CustomVolleyRequestQueue.getInstance(context).getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, subreddit, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                RedditPost redditPost = gson.fromJson(response.toString(), RedditPost.class);
                after_id = redditPost.getAfter();
                Collections.addAll(childrens, redditPost.getChildrens());
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error" + error.getMessage());

            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public List<Children> getChildrens(){
        return childrens;
    }
}
