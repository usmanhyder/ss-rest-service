package com.sevensenderscc.restservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebComicAPIObject {

    @JsonProperty("month")
    private Integer month;

    @JsonProperty("day")
    private Integer day;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("num")
    private String num;

    @JsonProperty("link")
    private String link;

    @JsonProperty("news")
    private String news;

    @JsonProperty("title")
    private String title;

    @JsonProperty("img")
    private String img;

    @JsonProperty("alt")
    private String alt;

    @JsonProperty("transcript")
    private String transcript;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    @Override
    public String toString() {
        return "WebComicAPIObject{" +
                "month=" + month +
                ", day=" + day +
                ", year=" + year +
                ", num='" + num + '\'' +
                ", link='" + link + '\'' +
                ", news='" + news + '\'' +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", alt='" + alt + '\'' +
                ", transcript='" + transcript + '\'' +
                '}';
    }
}
