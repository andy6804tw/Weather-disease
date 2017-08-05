package com.andy6804tw.dengueweather.DataModle;

/**
 * Created by andy6804tw on 2017/8/3.
 */

public class TWNewsModel {

    private String title,url,date;

    public TWNewsModel(String title, String url, String date) {
        this.title = title;
        this.url = url;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }
}
