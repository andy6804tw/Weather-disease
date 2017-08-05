package com.andy6804tw.dengueweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.andy6804tw.dengueweather.SpinMenu.SpinMenu;

public class CheckedDengueActivity extends AppCompatActivity {
    private WebView mWebView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_dengue);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//頂部工具列隱
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setInitialScale(1);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://4xtw.tk/ckdgphone.html");
    }

    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//手機按鈕事件
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            SpinMenu.openMenu();
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onClick(View view) {
        SpinMenu.openMenu();
        finish();
    }
}