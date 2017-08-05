package com.andy6804tw.dengueweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.andy6804tw.dengueweather.SpinMenu.SpinMenu;

public class EarthMapActivity extends AppCompatActivity {

    private WebView mWebView = null;
    private Toolbar toolbar;
    private String mUrl="http://www.healthmap.org/dengue/en/";
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_map);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//頂部工具列隱
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mWebView = (WebView)findViewById(R.id.webView);
        tvTitle=(TextView)findViewById(R.id.tvTitle);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setInitialScale(1);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);

    }

    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.item_global_temp:
                mUrl="https://earth.nullschool.net/zh-cn/#current/wind/surface/level/overlay=total_precipitable_water/grid=on/winkel3=56.59,-2.58,298/loc=87.436,3.031";
                tvTitle.setText(getResources().getString(R.string.item_global_temp));
                onResume();
                return true;
            case R.id.item_global_wet:
                mUrl="https://earth.nullschool.net/zh-cn/#current/wind/surface/level/overlay=total_precipitable_water/grid=on/winkel3=117.14,26.87,1192/loc=121.678,23.713";
                tvTitle.setText(getResources().getString(R.string.item_global_wet));
                onResume();
                return true;
            case R.id.item_global_dengue:
                mUrl="http://www.healthmap.org/dengue/en/";
                tvTitle.setText(getResources().getString(R.string.item_global_dengue));
                onResume();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
