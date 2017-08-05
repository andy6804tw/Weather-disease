package com.andy6804tw.dengueweather;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.andy6804tw.dengueweather.Fragment.CheckedDengueFragment;
import com.andy6804tw.dengueweather.Fragment.DengueMapFragment;
import com.andy6804tw.dengueweather.Fragment.EarthMapFragment;
import com.andy6804tw.dengueweather.Fragment.NewsFragment;
import com.andy6804tw.dengueweather.Fragment.WeatherNowFragment;
import com.andy6804tw.dengueweather.SpinMenu.OnSpinMenuStateChangeListener;
import com.andy6804tw.dengueweather.SpinMenu.SpinMenu;
import com.andy6804tw.dengueweather.Utils.ExitApplication;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SpinMenu spinMenu;
    private long temptime = 0;//計算退出秒數

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExitApplication.getInstance().addActivity(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//頂部工具列隱
        spinMenu = (SpinMenu) findViewById(R.id.spin_menu);

        // 设置页面标题
        List<String> hintStrList = new ArrayList<>();
        hintStrList.add(getResources().getString(R.string.weather));
        hintStrList.add(getResources().getString(R.string.dengue_fever));
        hintStrList.add(getResources().getString(R.string.global_map));
        hintStrList.add(getResources().getString(R.string.tw_dendue_map));
        hintStrList.add(getResources().getString(R.string.dengun_prevention));
        hintStrList.add("阅读空间");
        hintStrList.add("听听唱唱");
        hintStrList.add("系统设置");
        spinMenu.setContent(MainActivity.this);
        spinMenu.setHintTextStrList(hintStrList);
        spinMenu.setHintTextColor(Color.parseColor("#FFFFFF"));
        spinMenu.setHintTextSize(14);

        // 设置启动手势开启菜单
        spinMenu.setEnableGesture(true);

        // 设置页面适配器
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new WeatherNowFragment());
        fragmentList.add(new NewsFragment());
        fragmentList.add(new EarthMapFragment());
        fragmentList.add(new DengueMapFragment());
        fragmentList.add(new CheckedDengueFragment());
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        spinMenu.setFragmentAdapter(fragmentPagerAdapter);

        // 设置菜单状态改变时的监听器
        spinMenu.setOnSpinMenuStateChangeListener(new OnSpinMenuStateChangeListener() {
            @Override
            public void onMenuOpened() {
                //Toast.makeText(MainActivity.this, "SpinMenu opened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMenuClosed() {
                //Toast.makeText(MainActivity.this, "SpinMenu closed", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//手機按鈕事件
    {
        // TODO Auto-generated method stub
        if (1 == getSupportFragmentManager().getBackStackEntryCount()) {
            getSupportFragmentManager().popBackStack();
            return true;
        }else{
            if((keyCode == KeyEvent.KEYCODE_BACK)&&(event.getAction() == KeyEvent.ACTION_DOWN))
            {
                if(System.currentTimeMillis() - temptime >2000) // 2s內再次選擇back有效
                {
                    Toast.makeText(this, "再按一次離開", Toast.LENGTH_LONG).show();
                    temptime = System.currentTimeMillis();
                }
                else {
                    ExitApplication.getInstance().exit();
                }

                return true;

            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
