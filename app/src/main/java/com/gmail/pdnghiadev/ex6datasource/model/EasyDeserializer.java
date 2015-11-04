package com.gmail.pdnghiadev.ex6datasource.model;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

/**
 * Created by PDNghiaDev on 11/2/2015.
 */
public abstract class EasyDeserializer<T> implements JsonDeserializer<T> {

    // Get boolean value of a json element
    protected boolean getBooleanValue(JsonElement jsonElement, boolean defaultValue){
        boolean returnValue = defaultValue;
        if (jsonElement != null && !jsonElement.isJsonNull()){
            returnValue = jsonElement.getAsBoolean();
        }

        return returnValue;
    }

    // Get int value of a json element
    protected int getIntValue(JsonElement jsonElement, int defaultValue){
        int returnValue = defaultValue;
        if (jsonElement != null && !jsonElement.isJsonNull()){
            returnValue = jsonElement.getAsInt();
        }

        return returnValue;
    }

    // Get long value of a json element
    protected long getLongValue(JsonElement jsonElement, long defaultValue){
        long returnValue = defaultValue;
        if (jsonElement != null && !jsonElement.isJsonNull()){
            returnValue = jsonElement.getAsLong();
        }

        return returnValue;
    }

    // Get String value of a json element
    protected String getStringValue(JsonElement jsonElement, String defaultValue){
        String returnValue = defaultValue;
        if (jsonElement != null && !jsonElement.isJsonNull()){
            returnValue =  jsonElement.getAsString();
        }

        return returnValue;
    }
}
