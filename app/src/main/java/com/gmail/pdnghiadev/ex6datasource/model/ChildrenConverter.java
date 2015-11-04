package com.gmail.pdnghiadev.ex6datasource.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by PDNghiaDev on 11/2/2015.
 */
public class ChildrenConverter extends EasyDeserializer<Children> {

    @Override
    public Children deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Children children = null;
        if (json != null && json.isJsonObject()){
            JsonObject childrenJsonObject = json.getAsJsonObject();
            JsonElement dataElement = childrenJsonObject.get("data");

            if (dataElement != null && dataElement.isJsonObject()){
                JsonObject dataJsonObject = dataElement.getAsJsonObject();
                children = new Children();
                children.setId(getStringValue(dataJsonObject.get("id"), null));
                children.setTitle(getStringValue(dataJsonObject.get("title"), null));
                children.setScore(getIntValue(dataJsonObject.get("score"), 0));
                children.setCommentCount(getIntValue(dataJsonObject.get("num_comments"), 0));
                children.setUrl(getStringValue(dataJsonObject.get("url"), null));
                children.setIsStickyPost(getBooleanValue(dataJsonObject.get("stickied"), false));
                children.setAuthor(getStringValue(dataJsonObject.get("author"), null));
                children.setSubreddit(getStringValue(dataJsonObject.get("subreddit"), null));
                children.setDomain(getStringValue(dataJsonObject.get("domain"), null));
                children.setCreateUTC(getLongValue(dataJsonObject.get("created_utc"), 0));
            }
        }
        return children;
    }
}
