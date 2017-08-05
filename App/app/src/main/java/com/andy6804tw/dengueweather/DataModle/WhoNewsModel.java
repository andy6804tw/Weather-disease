package com.andy6804tw.dengueweather.DataModle;

/**
 * Created by andy6804tw on 2017/8/3.
 */

public class WhoNewsModel {
    private String title,description,url,date;

    public WhoNewsModel(String title, String description, String date, String url) {
        this.title = title;
        this.description=description;
        this.url = url;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }
}

