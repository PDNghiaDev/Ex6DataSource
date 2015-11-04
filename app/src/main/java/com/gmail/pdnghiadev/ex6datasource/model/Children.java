package com.gmail.pdnghiadev.ex6datasource.model;

/**
 * Created by PDNghiaDev on 11/2/2015.
 */
public class Children {

    private String id;
    private String title;
    private int score;
    private int commentCount;
    private String url;
    private boolean isStickyPost;
    private String author;
    private String subreddit;
    private String domain;
    private long createUTC;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public long getCreateUTC() {
        return createUTC;
    }

    public void setCreateUTC(long createUTC) {
        this.createUTC = createUTC;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isStickyPost() {
        return isStickyPost;
    }

    public void setIsStickyPost(boolean isStickyPost) {
        this.isStickyPost = isStickyPost;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
