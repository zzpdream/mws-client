package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ydd.conference.R;
import com.ydd.conference.event.Event;
import com.ydd.conference.event.ShowSecondEvent;
import com.ydd.conference.util.SharedPreferencesUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.s
 */
public class FullscreenActivity extends BaseActivity {

    private static final String TAG = "FullscreenActivity";

    private WebView webView = null;

    private String url = "http://10.200.8.21/user/LiveVideo2.html";

    private int timeout = 10000;  //网页加载超时时间

    public Handler handler = new Handler();

    private Timer timer;


    Runnable pollRunnable = new Runnable() {
        @Override
        public void run() {
            webView.stopLoading();
            webView.loadUrl(url);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            String s = intent.getStringExtra("url");
            if (s != null)
                url = s;
        }
        webView = (WebView) findViewById(R.id.webView);
        boolean flag = intent.getBooleanExtra("flag", false);
        if (flag) {
            ViewGroup viewGroup = (ViewGroup) webView.getParent();
            ViewGroup.LayoutParams params = webView.getLayoutParams();
            params.height = 600;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            webView.setLayoutParams(params);
        }

        setWebView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
    }

    private void setWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new MonitorWebClient());
        webView.loadUrl(url);
    }

    private void delayReload() {
        Message msg = Message.obtain();
        handler.sendMessageDelayed(msg, 3000);
    }

    private class MonitorWebClient extends WebViewClient {

        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            Log.d(TAG, "------onPageStarted------");
//            Log.d(TAG, "------print url:" + url);
//            final int progressNum = view.getProgress();
//            timer = new Timer();
//            TimerTask tt = new TimerTask() {
//                @Override
//                public void run() {
//                    if (null != timer && progressNum < 100) {
//                        Log.d(TAG, "------load page timeout------" + progressNum);
//                        handler.post(pollRunnable);
//                        timer.cancel();
//                        timer.purge();
//                        timer = null;
//                    }
//                }
//            };
//            timer.schedule(tt, timeout, 1);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            Log.d(TAG, "------onPageFinished------");
//            if (null != timer) {
//                timer.cancel();
//                timer.purge();
//                timer = null;
//            }
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);
            Log.d(TAG, "onPageCommitVisible");
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            Log.d(TAG, "onReceivedHttpError");
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.d(TAG, "onReceivedError");
            try {
                handler.post(pollRunnable);
            } catch (Exception e) {
            }
        }

        private int getRespStatus(String url) {
            int status = -1;
            try {
                Log.d(TAG, "get page status");
                URL netUrl = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) netUrl.openConnection();
                return urlConnection.getResponseCode();
            } catch (IOException e) {
            }
            return status;
        }
    }

    public void onEventMainThread(Event event) {
        if (event instanceof ShowSecondEvent) {
            if (null != secondaryDisplay) {
                secondaryDisplay.showName(SharedPreferencesUtil.getMemberName());
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    public static void actionStart(Context context, String url) {
        Intent intent = new Intent(context, FullscreenActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, String url, boolean flag) {
        Intent intent = new Intent(context, FullscreenActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("flag", flag);
        context.startActivity(intent);
    }
}
