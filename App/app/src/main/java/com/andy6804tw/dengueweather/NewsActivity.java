package com.andy6804tw.dengueweather;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.andy6804tw.dengueweather.Adapter.ViewPagerAdapter;
import com.andy6804tw.dengueweather.Fragment.TaiwanNewsFragment;
import com.andy6804tw.dengueweather.Fragment.WhoNewsFragment;
import com.andy6804tw.dengueweather.SpinMenu.SpinMenu;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;
import com.kekstudio.dachshundtablayout.indicators.DachshundIndicator;

public class NewsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private DachshundTabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//頂部工具列隱


        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new TaiwanNewsFragment(),getResources().getString(R.string.taiwan));
        viewPagerAdapter.addFragments(new WhoNewsFragment(),getResources().getString(R.string.global));
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = (DachshundTabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setAnimatedIndicator(new DachshundIndicator(tabLayout));

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//手機按鈕事件
    {
        if((keyCode == KeyEvent.KEYCODE_BACK)&&(event.getAction() == KeyEvent.ACTION_DOWN))
            {
                SpinMenu. openMenu();
                finish();
                return true;
            }

        return super.onKeyDown(keyCode, event);
    }
    public void onClick(View view) {
        SpinMenu. openMenu();
        finish();
    }
}
