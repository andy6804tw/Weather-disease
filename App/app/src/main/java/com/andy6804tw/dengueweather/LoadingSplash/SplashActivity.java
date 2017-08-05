package com.andy6804tw.dengueweather.LoadingSplash;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.andy6804tw.dengueweather.DataBase.DBAccessWeather;
import com.andy6804tw.dengueweather.DataModle.TWNewsModel;
import com.andy6804tw.dengueweather.DataModle.WhoNewsModel;
import com.andy6804tw.dengueweather.MainActivity;
import com.andy6804tw.dengueweather.R;
import com.andy6804tw.dengueweather.Utils.ExitApplication;
import com.andy6804tw.dengueweather.Utils.GPSTracker;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import taobe.tec.jcc.JChineseConvertor;

import static android.os.Build.VERSION_CODES.M;

public class SplashActivity extends AppCompatActivity {

    private GPSTracker mGps;
    private DBAccessWeather mAccess;
    private double mLatitude,mLongitude;
    private String mLanguage="en",mCity,mCountry,mDistrict,mVillage;
    private Context mContext;
    private static Document document;
    public static ArrayList<TWNewsModel>twNewsList;
    public static ArrayList<WhoNewsModel>whoNewsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //更改狀態欄顏色
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        ExitApplication.getInstance().addActivity(this);
        mContext=getApplicationContext();
        mAccess = new DBAccessWeather(this, "weather", null,1);//Sqlite上版本
        twNewsList=new ArrayList<>();
        whoNewsList=new ArrayList<>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initDengueNews();
        initWHONews();
        GPSPremessionCheck();
    }

    private void GPSPremessionCheck() {
        /**偵測權限**/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //如果沒有授權使用定位就會跳出來這個
            // TODO: Consider calling
            //Log.e("Data6", "進入!");
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // 如果裝置版本是6.0（包含）以上
            if (Build.VERSION.SDK_INT >= M) {
                // 取得授權狀態，參數是請求授權的名稱
                int hasPermission = checkSelfPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION);
                // 如果未授權
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    // 請求授權
                    //     第一個參數是請求授權的名稱
                    //     第二個參數是請求代碼
                    //Log.e("Data3", "失敗!");
                    requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            10);
                }
            }
        }else{
            init_GPS();
            init_Weather();
            new Handler().postDelayed(new Runnable() {//等待6秒鐘讀取GPS和Weather
                @Override
                public void run() {
                    Cursor c = mAccess.getData("Condition", null, null);
                    c.moveToFirst();
                    if(c.getCount()==0) {
                        //SunBabyLoadingView.str = "資料重新建置中請收後...";
                        onResume();
                    }
                    else
                        startActivity( new Intent(SplashActivity.this, MainActivity.class));
                }
            }, 3500);
        }
    }
    private void init_Weather() {
        ///**撈取資料END**///
        ///**撈取天氣資料START**///
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(SELECT%20woeid%20FROM%20geo.places%20WHERE%20text%3D\"("+mLatitude+","+mLongitude+")\")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            //位置 Location
                            if(!(mLatitude>=20&&mLatitude<=27)&&!(mLongitude>=118&&mLongitude<=124)){
                                mCountry = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("location").getString("country");
                                mCity = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("location").getString("city");
                                //寫入 Location 資料表
                                mAccess.update("1",mCountry,mCity,mDistrict,mVillage,Double.toString(mLatitude),Double.toString(mLongitude),null);
                            }
                            //風 wind
                            String chill = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getString("chill");
                            double direction = Double.parseDouble(jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getString("direction"));
                            int speed = Integer.parseInt(jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("wind").getString("speed"));
                            //direction外來鍵
                            String str_direction="";
                            if((direction>=0&&direction<=11.25) || (direction>=348.76&&direction<=360)) {
                                str_direction=mContext.getResources().getString(R.string.N);
                            }
                            if(direction>=11.26&&direction<=33.75){
                                str_direction=mContext.getResources().getString(R.string.NNE);
                            }
                            if(direction>=33.76&&direction<=56.25){
                                str_direction=mContext.getResources().getString(R.string.NE);
                            }
                            if(direction>=56.26&&direction<=78.75){
                                str_direction=mContext.getResources().getString(R.string.ENE);
                            }
                            if(direction>=78.76&&direction<=101.25){
                                str_direction=mContext.getResources().getString(R.string.E);
                            }
                            if(direction>=101.26&&direction<=123.75){
                                str_direction=mContext.getResources().getString(R.string.ESE);
                            }
                            if(direction>=123.76&&direction<=146.25){
                                str_direction=mContext.getResources().getString(R.string.SE);
                            }
                            if(direction>=146.26&&direction<=168.75){
                                str_direction=mContext.getResources().getString(R.string.SSE);
                            }
                            if(direction>=168.76&&direction<=191.25){
                                str_direction=mContext.getResources().getString(R.string.S);
                            }
                            if(direction>=191.26&&direction<=213.75){
                                str_direction=mContext.getResources().getString(R.string.SSW);
                            }
                            if(direction>=213.76&&direction<=236.25){
                                str_direction=mContext.getResources().getString(R.string.SW);
                            }
                            if(direction>=236.26&&direction<=258.75){
                                str_direction=mContext.getResources().getString(R.string.WSW);
                            }
                            if(direction>=258.76&&direction<=281.25){
                                str_direction=mContext.getResources().getString(R.string.W);
                            }
                            if(direction>=281.26&&direction<=303.75){
                                str_direction=mContext.getResources().getString(R.string.WNW);
                            }
                            if(direction>=303.76&&direction<=326.25){
                                str_direction=mContext.getResources().getString(R.string.NW);
                            }
                            if(direction>=326.26&&direction<=348.75){
                                str_direction=mContext.getResources().getString(R.string.NNW);
                            }

                            //大氣 Atmosphere
                            String humidity = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getString("humidity");
                            String pressure = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getString("pressure");
                            String rising = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getString("rising");
                            String visibility = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("atmosphere").getString("visibility");
                            //天文 Astronomy
                            String sunrise = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("astronomy").getString("sunrise");
                            String sunset = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("astronomy").getString("sunset");
                            //狀態 Condition
                            String date = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("date");
                            String day = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("day");
                            String high = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("high");
                            String low = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(0).getString("low");
                            String temp = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("temp");
                            String code = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONObject("condition").getString("code");
                            String pushTime = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getString("pubDate");
                            String publish_time = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getString("lastBuildDate");
                            Cursor c = mAccess.getData("Condition", null, null);
                            c.moveToFirst();
                            if(c.getCount()==0) {
                                mAccess.add();
                                //寫入 Location 資料表
                                //寫入 Wind資料表
                                mAccess.add("1", Double.parseDouble(chill), str_direction, speed+"");
                                //寫入 Atmosphere資料表
                                mAccess.add("1", humidity, pressure, rising, visibility);
                                //寫入 Astronomy資料表
                                mAccess.add("1", sunrise, sunset);
                                //寫入 Condition資料表
                                mAccess.add("1", date, day, Double.parseDouble(high), Double.parseDouble(low), Double.parseDouble(temp), Integer.parseInt(code),publish_time);
                                //寫入 Forecast
                                for(int i=0;i<10;i++){
                                    //預報Forecast
                                    String forecast_date = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("date");
                                    String forecast_day = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("day");
                                    String forecast_high = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("high");
                                    String forecast_low = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("low");
                                    String forecast_code = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("code");
                                    mAccess.add(i+"", forecast_date, forecast_day,Double.parseDouble(forecast_high),Double.parseDouble(forecast_low),forecast_code);
                                }

                            }else{
                                //Toast.makeText(SplashActivity.this,publish_time,Toast.LENGTH_SHORT).show();
                                //寫入 Wind資料表
                                mAccess.update("1", Double.parseDouble(chill), str_direction, speed+"",null);
                                //寫入 Atmosphere資料表
                                mAccess.update("1", humidity, pressure,visibility ,rising,null);
                                //寫入 Astronomy資料表
                                mAccess.update("1", sunrise, sunset,null);
                                //寫入 Condition資料表
                                mAccess.update("1", date, day, Double.parseDouble(high), Double.parseDouble(low), Double.parseDouble(temp), Integer.parseInt(code),publish_time,null);
                                //寫入 Forecast
                                for(int i=0;i<10;i++){
                                    //預報Forecast
                                    String forecast_date = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("date");
                                    String forecast_day = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("day");
                                    String forecast_high = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("high");
                                    String forecast_low = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("low");
                                    String forecast_code = jsonObject.getJSONObject("query").getJSONObject("results").getJSONObject("channel").getJSONObject("item").getJSONArray("forecast").getJSONObject(i).getString("code");
                                    mAccess.update(i+"", forecast_date, forecast_day,Double.parseDouble(forecast_high),Double.parseDouble(forecast_low),forecast_code,"forecast_id ="+i);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashActivity.this, "無法連接網路!", Toast.LENGTH_SHORT).show();

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    private void init_GPS() {
        mGps = new GPSTracker(this);
        if (mGps.canGetLocation && mGps.getLatitude() != (0.0) && mGps.getLongtitude() != (0.0)) {
            mLatitude = mGps.getLatitude();
            mLongitude =mGps.getLongtitude();
            if((mLatitude>=20&&mLatitude<=27)&&(mLongitude>=118&&mLongitude<=124)&&getResources().getConfiguration().locale.getCountry().equals("TW"))
                mLanguage="zh-TW";

            //Toast.makeText(getApplicationContext(), "Your Location is->\nLat: " + latitude + "\nLong: " + longtitude, Toast.LENGTH_LONG).show();
            ///**撈取時間資料START**///
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + mLatitude + "," + mLongitude + "&language="+mLanguage+"&sensor=true&key=AIzaSyDHA4UDKuJ_hZafj8Xn6m3mMzOsQnbTZ_w&lafhdfhfdhfdhrh";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //mTextView.setText("Response is: "+ response.substring(0,500));
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                int count = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").length();
                                mCountry = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 2).getString("long_name");
                                mCity = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 3).getString("long_name");
                                mDistrict = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 4).getString("short_name");
                                mVillage = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(count - 5).getString("short_name");
                                String str5 = jsonObject.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                                //寫入Location資料表
                                Cursor c = mAccess.getData("Location", null, null);
                                c.moveToFirst();
                                if(c.getCount()==0){
                                    mAccess.add("1",mCountry,mCity,mDistrict,mVillage,mLatitude+"",mLongitude+"");
                                }else if(c.getDouble(5)!=mLatitude||c.getDouble(6)!=mLongitude){
                                    //Toast.makeText(SplashActivity.this,"更新位置->\nLat: " + latitude + "\nLong: " + longtitude,Toast.LENGTH_SHORT).show();
                                    mAccess.update("1",mCountry,mCity,mDistrict,mVillage,Double.toString(mLatitude),Double.toString(mLongitude),null);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }else{
            final LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
                alertDialog.setTitle("Gps is settings");
                alertDialog.setMessage("Gps is not enabled.");
                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        }
    }
    public static void initDengueNews(){

        //登革熱新聞
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    document = Jsoup.connect("http://www.cdc.gov.tw/professional/list.aspx?did=641&treeid=6FD88FC9BF76E125&nowtreeid=283F5B5821AF305B")
                            .timeout(3000)
                            .get();
                    Elements noteList = document.select("div.in_list").select("ul").select("li");
                    int c=0;
                    //Log.e("Name"+c++,Image.attr("abs:src"));
                    for (Element element:noteList){
                        String data[]=element.select("td").text().split(" ");
                        String title=data[0];
                        String url=element.select("a").attr("abs:href");
                        String date=data[2];
                        twNewsList.add(new TWNewsModel(title,url,date));
                        //Log.e("Data"+c++,title+"     "+date+"      "+url);
                    }
                    Log.e("daffa",twNewsList.size()+" ");

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("TAG", "run: " + e.getMessage());
                }

            }
        }).start();

    }
    public  void initWHONews(){

        //WHO新聞
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String url="";
                    if(getResources().getConfiguration().locale.getCountry().equals("TW") )
                        url="http://www.who.int/feeds/entity/csr/don/zh/rss.xml";
                    else
                        url="http://www.who.int/feeds/entity/csr/don/en/rss.xml";
                    document = Jsoup.connect(url)
                            .timeout(3000)
                            .get();
                    Elements noteList = document.select("channel").select("item");
                    int c=0;
                    //Log.e("Name"+c++,Image.attr("abs:src"));
                    Log.e("Data",noteList.size()+"");
                    for (Element element:noteList){
                        //簡體轉繁體
                        JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
                        String title=element.select("title").text();
                        String description=element.select("description").text();
                        String date=element.select("pubDate").text();
                        String link=element.select("link").text();
                        whoNewsList.add(new WhoNewsModel(jChineseConvertor.s2t(title),jChineseConvertor.s2t(description),jChineseConvertor.s2t(date),link));
                        //Log.e("Data"+c++,jChineseConvertor.s2t(title)+" "+jChineseConvertor.s2t(description)+" "+jChineseConvertor.s2t(date)+" "+link);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("TAG", "run: " + e.getMessage());
                }

            }
        }).start();

    }
}