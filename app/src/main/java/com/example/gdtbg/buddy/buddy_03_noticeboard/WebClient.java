package com.example.gdtbg.buddy.buddy_03_noticeboard;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by gdtbg on 2017-11-15.
 */

public class WebClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl("https://www.facebook.com/bufs.lounge/");

        return super.shouldOverrideUrlLoading(view, request);
    }
}
