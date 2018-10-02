package com.kputsoftware.mileagecalculator.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.kputsoftware.mileagecalculator.R;

import static com.kputsoftware.mileagecalculator.library.variables.AFFILIATEURL;
import static com.kputsoftware.mileagecalculator.library.variables.MC;

/**
 * Created by duraiswa on 9/8/2017.
 */

public class Affiliate  extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affiliate);

        Toolbar myChildToolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient());

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);

        String  url = getSharedPreferences(MC,0).getString(AFFILIATEURL,"http://amzn.to/2xhL5td");
        if (validateUrl(url)) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
            return;
        }}

    private boolean validateUrl(String url) {
        return true;
    }



    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progress.setVisibility(View.GONE);
       //     findViewById(R.id.rlayout).setVisibility(View.GONE);
            Affiliate.this.progress.setProgress(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progress.setVisibility(View.VISIBLE);
            Affiliate.this.progress.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }

        public void onProgressChanged(WebView view, int progress) {
            setValue(progress);
        }
    }

    public void setValue(int progress) {
        this.progress.setProgress(progress);
    }
}
