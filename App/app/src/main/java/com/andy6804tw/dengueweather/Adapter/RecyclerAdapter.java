package com.andy6804tw.dengueweather.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andy6804tw.dengueweather.DataBase.DBAccessWeather;
import com.andy6804tw.dengueweather.R;
import com.github.pwittchen.weathericonview.WeatherIconView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by andy6804tw on 2017/7/27.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context mContext;
    private DBAccessWeather mAccess;
    private SharedPreferences settings;


    public RecyclerAdapter(Context context){
        mContext=context;
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        private WeatherIconView weatherIconView;
        private TextView tvTemperature,tvDay;
        public ViewHolder(View itemView) {
            super(itemView);
            weatherIconView = (WeatherIconView) itemView.findViewById(R.id.my_weather_icon);
            tvTemperature=(TextView)itemView.findViewById(R.id.tvTemperature);
            tvDay=(TextView)itemView.findViewById(R.id.tvDay);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        mAccess = new DBAccessWeather(mContext, "weather", null, 1);
        settings=mContext.getSharedPreferences("Data",MODE_PRIVATE);
        Cursor cl8 = mAccess.getData("Forecast",null,null);
        cl8.moveToPosition(position);
            //set Day
            if(position==0)
                viewHolder.tvDay.setText(mContext.getString(R.string.Today));
            else if(position==1)
                viewHolder.tvDay.setText(mContext.getString(R.string.Tomorrow));
            else
                viewHolder.tvDay.setText(day(cl8.getString(2)));
            //set temperature
            if(settings.getString("Temperature","").equals("°C")||settings.getString("Temperature","").equals(""))
                viewHolder.tvTemperature.setText((int)Math.round((cl8.getShort(4)-32)*5/9.)+"°/"+(int)Math.round((cl8.getShort(3)-32)*5/9.)+"°");
            else
                viewHolder.tvTemperature.setText(cl8.getShort(4)+"°/"+cl8.getShort(3)+"°");
            //set icon
            viewHolder.weatherIconView.setIconSize(25);
            viewHolder.weatherIconView.setIconColor(Color.WHITE);
            viewHolder.weatherIconView.setIconResource(weatherIcon(cl8.getShort(5)));
    }

    @Override
    public int getItemCount() {
        return 10;
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
    public String day(String day){
        if(day.equals("Sun")){
            return mContext.getResources().getString(R.string.Sun);
        }
        else if(day.equals("Mon")){
            return mContext.getResources().getString(R.string.Mon);
        }
        else if(day.equals("Tue")){
            return mContext.getResources().getString(R.string.Tue);
        }
        else if(day.equals("Wed")){
            return mContext.getResources().getString(R.string.Wed);
        }
        else if(day.equals("Thu")){
            return mContext.getResources().getString(R.string.Thu);
        }
        else if(day.equals("Fri")){
            return mContext.getResources().getString(R.string.Fri);
        }
        else{
            return mContext.getResources().getString(R.string.Sat);
        }
    }
}