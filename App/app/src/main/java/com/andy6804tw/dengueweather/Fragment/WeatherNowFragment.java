package com.andy6804tw.dengueweather.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andy6804tw.dengueweather.Adapter.RecyclerAdapter;
import com.andy6804tw.dengueweather.DataBase.DBAccessWeather;
import com.andy6804tw.dengueweather.R;
import com.andy6804tw.dengueweather.SpinMenu.SpinMenu;
import com.andy6804tw.weatherviewlibrary.WeatherView;
import com.github.pwittchen.weathericonview.WeatherIconView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherNowFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private WeatherIconView weatherIconView;
    private WeatherView weatherView;
    private DBAccessWeather mAccess;  //DataBase for weather
    private SharedPreferences settings;//設定
    private TextView tvTime,tvCity,tvLow,tvHigh,tvTemperature,tvCondition,tvFelt,tvHumidity,tvVisiblity;
    private ImageView ivNow;
    private Context mContext;
    private View mView;
    private ImageView ivMenu;

    public WeatherNowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_weather_now, container, false);
        mContext=getContext();
        mView=view;
        mAccess = new DBAccessWeather(mContext, "weather", null, 1);//資料庫版本
        settings=mContext.getSharedPreferences("Data",MODE_PRIVATE);//設定
        recyclerView =(RecyclerView)view.findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(mContext);
        recyclerView.setAdapter(adapter);

        ivMenu=(ImageView) view.findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinMenu.openMenu();
            }
        });

        initInfo();//載入目前時間地點
        initView();//載入View
        initOther();//其他資訊
        initWeatherView();//風車日出落


        return view;
    }
    private void initOther(){
        tvFelt=(TextView)mView.findViewById(R.id.tvFelt);
        tvHumidity=(TextView)mView.findViewById(R.id.tvHumidity);
        tvVisiblity=(TextView)mView.findViewById(R.id.tvVisiblity);
        //載入大氣資料
        Cursor c = mAccess.getData("Atmosphere", null, null);
        c.moveToFirst();
        //載入體感溫度資料
        Cursor c3 = mAccess.getData("Wind", null, null);
        c3.moveToFirst();
        if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals("")) {
            tvFelt.setText(Math.round((c3.getShort(1)-32)*5/9.)+"°C");
        }else{
            tvFelt.setText(c3.getString(1)+"°F");
        }
        tvHumidity.setText(c.getString(1)+" %");
        tvVisiblity.setText(c.getString(3)+" km");
    }
    private void initWeatherView() {
        /**天文**/
        Cursor cl5 = mAccess.getData("Astronomy", null, null);
        cl5.moveToFirst();
        //設定日出日落時間(12、24小時制)
        String str_start[]=cl5.getString(1).split(":");
        String str_end[]=cl5.getString(2).split(":");
        str_start[1]=str_start[1].split(" ")[0]; //把am去掉
        str_end[1]=str_end[1].split(" ")[0];//把pm去掉
        if(str_start[1].length()==1)//開始分補零
            str_start[1]="0"+str_start[1];
        //結束時間+12小時
        str_end[0]=Integer.parseInt(str_end[0])+12+"";
        if(str_end[1].length()==1)//結束分補零
            str_end[1]="0"+str_end[1];
        /**大氣**/
        Cursor c = mAccess.getData("Atmosphere", null, null);
        c.moveToFirst();
        String pressure=c.getString(2);
        /**風**/
        Cursor c2 = mAccess.getData("Wind", null, null);
        c2.moveToFirst();
        String windSpeed=c2.getString(3),direction=c2.getString(2);
        //WeatherView
        weatherView = (WeatherView) mView.findViewById(R.id.weatherView);
        weatherView.initData(windSpeed,direction,"0"+str_start[0]+":"+str_start[1],str_end[0]+":"+str_end[1],pressure,true);
    }

    private void initView() {
        weatherIconView=(WeatherIconView)mView.findViewById(R.id.weatherIconView);
        tvLow=(TextView)mView.findViewById(R.id.tvLow);
        tvHigh=(TextView)mView.findViewById(R.id.tvHigh);
        tvTemperature=(TextView)mView.findViewById(R.id.tvTemperature);
        tvCondition=(TextView)mView.findViewById(R.id.tvCondition);
        Cursor c = mAccess.getData("Condition", null, null);
        c.moveToFirst();
        Cursor c2 = mAccess.getData("Code", null, null);
        c2.moveToPosition(c.getShort(6));
        if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals("")) {
            tvHigh.setText(Math.round((c.getShort(3)-32)*5/9.)+"°");
            tvLow.setText(Math.round((c.getShort(4)-32)*5/9.)+"°");
            tvTemperature.setText(Math.round((c.getShort(5)-32)*5/9.)+"°");
        }else{
            tvHigh.setText(c.getString(3)+"°");
            tvLow.setText(c.getString(4)+"°");
            tvTemperature.setText(c.getString(5)+"°");
        }
        tvCondition.setText(c2.getString(1));
        //天氣圖示
        weatherIconView.setIconSize(75);
        weatherIconView.setIconColor(Color.WHITE);
        weatherIconView.setIconResource(weatherIcon(c.getShort(6)));
    }

    public void initInfo(){
        tvTime=(TextView)mView.findViewById(R.id.tvTime);
        tvCity=(TextView)mView.findViewById(R.id.tvCity);
        ivNow=(ImageView)mView.findViewById(R.id.ivNow);
        Cursor cl1 = mAccess.getData("Location", null, null);
        cl1.moveToFirst();
        Cursor cl6 = mAccess.getData("Condition", null, null);
        cl6.moveToFirst();
        //取得系統時間 Fri, 10 Mar 2017 03:23 PM CST
        String str[]=cl6.getString(7).split(" "),time[]=str[4].split(":");
        int hour=Integer.parseInt(time[0]);
        if(settings.getString("Clock","").equals("24hr")||settings.getString("Clock","").equals("")){
            if(str[5].equals("AM")&&Integer.parseInt(time[0])==12)
                hour=00;
            if(str[5].equals("PM")&&Integer.parseInt(time[0])!=12) {
                hour+=12;
                tvTime.setText(Integer.parseInt(time[0]) + 12 + ":" + time[1] + " " + str[6]);
            }
            else
                tvTime.setText(Integer.parseInt(time[0])+":"+time[1]+" "+str[6]);
            tvCity.setText(cl1.getString(2));
        }
        else{
            if(str[5].equals("PM")&&Integer.parseInt(time[0])!=12)
                hour+=12;
            tvTime.setText(str[4]+" "+str[5]+" "+str[6]);
            tvCity.setText(cl1.getString(2));
        }
    }
    public String weatherIcon(int code){
        //天氣圖示
        if(code==0)
            return mContext.getString(R.string.wi_yahoo_0);
        else if(code==1)
            return mContext.getString(R.string.wi_yahoo_1);
        else if(code==2)
            return mContext.getString(R.string.wi_yahoo_2);
        else if(code==3)
            return mContext.getString(R.string.wi_yahoo_3);
        else if(code==4)
            return mContext.getString(R.string.wi_yahoo_4);
        else if(code==5)
            return mContext.getString(R.string.wi_yahoo_5);
        else if(code==6)
            return mContext.getString(R.string.wi_yahoo_6);
        else if(code==7)
            return mContext.getString(R.string.wi_yahoo_7);
        else if(code==8)
            return mContext.getString(R.string.wi_yahoo_8);
        else if(code==9)
            return mContext.getString(R.string.wi_yahoo_9);
        else if(code==10)
            return mContext.getString(R.string.wi_yahoo_10);
        else if(code==11)
            return mContext.getString(R.string.wi_yahoo_11);
        else if(code==12)
            return mContext.getString(R.string.wi_yahoo_12);
        else if(code==13)
            return mContext.getString(R.string.wi_yahoo_13);
        else if(code==14)
            return mContext.getString(R.string.wi_yahoo_14);
        else if(code==15)
            return mContext.getString(R.string.wi_yahoo_15);
        else if(code==16)
            return mContext.getString(R.string.wi_yahoo_16);
        else if(code==17)
            return mContext.getString(R.string.wi_yahoo_17);
        else if(code==18)
            return mContext.getString(R.string.wi_yahoo_18);
        else if(code==19)
            return mContext.getString(R.string.wi_yahoo_19);
        else if(code==20)
            return mContext.getString(R.string.wi_yahoo_20);
        else if(code==21)
            return mContext.getString(R.string.wi_yahoo_21);
        else if(code==22)
            return mContext.getString(R.string.wi_yahoo_22);
        else if(code==23)
            return mContext.getString(R.string.wi_yahoo_23);
        else if(code==24)
            return mContext.getString(R.string.wi_yahoo_24);
        else if(code==25)
            return mContext.getString(R.string.wi_yahoo_25);
        else if(code==26)
            return mContext.getString(R.string.wi_yahoo_26);
        else if(code==27)
            return mContext.getString(R.string.wi_yahoo_27);
        else if(code==28)
            return mContext.getString(R.string.wi_yahoo_28);
        else if(code==29)
            return mContext.getString(R.string.wi_yahoo_29);
        else if(code==30)
            return mContext.getString(R.string.wi_yahoo_30);
        else if(code==31)
            return mContext.getString(R.string.wi_yahoo_31);
        else if(code==32)
            return mContext.getString(R.string.wi_yahoo_32);
        else if(code==33)
            return mContext.getString(R.string.wi_yahoo_33);
        else if(code==34)
            return mContext.getString(R.string.wi_yahoo_34);
        else if(code==35)
            return mContext.getString(R.string.wi_yahoo_35);
        else if(code==36)
            return mContext.getString(R.string.wi_yahoo_36);
        else if(code==37)
            return mContext.getString(R.string.wi_yahoo_37);
        else if(code==38)
            return mContext.getString(R.string.wi_yahoo_38);
        else if(code==39)
            return mContext.getString(R.string.wi_yahoo_39);
        else if(code==40)
            return mContext.getString(R.string.wi_yahoo_40);
        else if(code==41)
            return mContext.getString(R.string.wi_yahoo_41);
        else if(code==42)
            return mContext.getString(R.string.wi_yahoo_42);
        else if(code==43)
            return mContext.getString(R.string.wi_yahoo_43);
        else if(code==44)
            return mContext.getString(R.string.wi_yahoo_44);
        else if(code==45)
            return mContext.getString(R.string.wi_yahoo_45);
        else if(code==46)
            return mContext.getString(R.string.wi_yahoo_46);
        else if(code==47)
            return mContext.getString(R.string.wi_yahoo_47);
        else
            return mContext.getString(R.string.wi_yahoo_3200);
    }

}
