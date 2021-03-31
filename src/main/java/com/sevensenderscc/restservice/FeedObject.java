package com.sevensenderscc.restservice;

import java.util.Date;

public class FeedObject implements Comparable<FeedObject> {
    private String title;
    private String pictureURL;
    private String webURL;
    private Date publishingDate;
    private String publishingDateString;

    public FeedObject() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public Date getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(Date publishingDate) {
        this.publishingDate = publishingDate;
    }

    public String getPublishingDateString() {
        return publishingDateString;
    }

    public void setPublishingDateString(String publishingDateString) {
        this.publishingDateString = publishingDateString;
    }

    @Override
    public int compareTo(FeedObject feedObject) {
        return getPublishingDate().compareTo(feedObject.getPublishingDate());
    }
}
