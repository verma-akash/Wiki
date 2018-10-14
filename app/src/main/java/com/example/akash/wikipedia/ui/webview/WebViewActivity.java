package com.example.akash.wikipedia.ui.webview;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.akash.wikipedia.R;
import com.example.akash.wikipedia.ui.base.BaseActivity;
import com.example.akash.wikipedia.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Akash Verma on 14/10/18.
 */
public class WebViewActivity extends BaseActivity {

    @BindView(R.id.web_view)
    public WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(AppConstants.SEARCH_ITEM)) {
            webView.setWebViewClient(new MyWebView());
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.loadUrl("https://en.wikipedia.org/wiki/" + getIntent().getStringExtra(AppConstants.SEARCH_ITEM));
        }

    }

    private class MyWebView extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
}
