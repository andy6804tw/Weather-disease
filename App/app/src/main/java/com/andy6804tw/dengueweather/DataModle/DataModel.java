package com.andy6804tw.dengueweather.DataModle;

/**
 * Created by andy6804tw on 2017/7/2.
 */

public class DataModel {

    private String title;//文章標題
    private String time;//文章發布時間
    private String detail;//文章部分內容
    private String image;//文章圖片
    private String url;//文章連結

    public DataModel(String title, String time, String detail, String image, String url) {
        this.title = title;
        this.time = time;
        this.detail = detail;
        this.image = image;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getDetail() {
        return detail;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }
}
