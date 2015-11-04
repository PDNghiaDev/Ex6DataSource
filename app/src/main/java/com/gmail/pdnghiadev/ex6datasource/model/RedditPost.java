package com.gmail.pdnghiadev.ex6datasource.model;

/**
 * Created by PDNghiaDev on 11/1/2015.
 */
public class RedditPost {

    private Children[] childrens;
    private String after;
    private String before;

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public Children[] getChildrens() {
        return childrens;
    }

    public void setChildrens(Children[] childrens) {
        this.childrens = childrens;
    }
}
