package com.gmail.pdnghiadev.ex6datasource.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by PDNghiaDev on 11/2/2015.
 */
public class RedditPostConverter extends EasyDeserializer<RedditPost> {

    @Override
    public RedditPost deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        RedditPost redditPost = null;
        if (json != null && json.isJsonObject()){
            JsonObject postJsonObject = json.getAsJsonObject();
            JsonElement dataElement = postJsonObject.get("data");

            if (dataElement != null && dataElement.isJsonObject()){
                JsonObject dataJsonObject = dataElement.getAsJsonObject();
                redditPost = new RedditPost();
                Children[] childrens = context.deserialize(dataJsonObject.get("children"), Children[].class);
                redditPost.setChildrens(childrens);
                redditPost.setAfter(getStringValue(dataJsonObject.get("after"), null));
                redditPost.setBefore(getStringValue(dataJsonObject.get("before"), null));
            }
        }
        return redditPost;
    }
}
