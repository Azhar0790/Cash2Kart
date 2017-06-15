package com.example.azhar_sarps.cash2kart;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class WebviewActivity extends AppCompatActivity implements View.OnTouchListener, Handler.Callback{
    private SmoothProgressBar mPocketBar;
    public static final String WEB_URL_TO_LOAD = "https://linksredirect.com/?pub_id=12078CL10975&subid=sub_id&source=linkkit&url=https%3A%2F%2Fwww.myntra.com%2F";
    private static final String GOOGLE_SERACH_URL = "https://linksredirect.com/?pub_id=12078CL10975&subid=sub_id&source=linkkit&url=https%3A%2F%2Fwww.myntra.com%2F";

    private WebView webView;
    private FrameLayout customViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View mCustomView;
    private MyWebChromeClient mWebChromeClient;
    private MyWebViewClient mWebViewClient;

    private int webViewPreviousState;
    private final int PAGE_STARTED = 0x1;
    private final int PAGE_REDIRECTED = 0x2;
    private View rootView;
    private static final int CLICK_ON_WEBVIEW = 1;
    private static final int CLICK_ON_URL = 2;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal_web_view);

        customViewContainer = (FrameLayout) findViewById(R.id.customViewContainer);
        webView = (WebView)findViewById(R.id.webView);
        mPocketBar = (SmoothProgressBar)findViewById(R.id.pocket);
        mWebViewClient = new MyWebViewClient();
        webView.setWebViewClient(mWebViewClient);

        mWebChromeClient = new MyWebChromeClient();
        webView.setWebChromeClient(mWebChromeClient);
        webView.getSettings().setJavaScriptEnabled(true);

        // Important for PayUMoney
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSaveFormData(true);

        webView.requestFocus();
        mPocketBar.setSmoothProgressDrawableBackgroundDrawable(
                SmoothProgressBarUtils.generateDrawableWithColors(
                        getResources().getIntArray(R.array.pocket_background_colors), ((SmoothProgressDrawable) mPocketBar.getIndeterminateDrawable()).getStrokeWidth()));
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        loadWebUrl(url);
    }


    public void loadWebUrl(String urlLoad) {

        webView.loadUrl(urlLoad);


    }

    private boolean inCustomView() {
        return (mCustomView != null);
    }

    private void hideCustomView() {
        mWebChromeClient.onHideCustomView();
    }

    @Override
    public void onPause() {
        super.onPause(); // To change body of overridden methods use File |
        // Settings | File Templates.
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume(); // To change body of overridden methods use File |
        // Settings | File Templates.
        webView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop(); // To change body of overridden methods use File |
        // Settings | File Templates.
        if (inCustomView()) {
            hideCustomView();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareSubText = "WhatsApp - The Great Chat App";
                String shareBodyText = "https://play.google.com/store/apps/details?id=com.whatsapp&hl=en";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubText);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(shareIntent, "Share With"));
                return true;
            case R.id.rating:
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp&hl=en");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp&hl=en")));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    class MyWebChromeClient extends WebChromeClient {
        private View mVideoProgressView;

        @Override
        public void onShowCustomView(View view, int requestedOrientation,
                                     CustomViewCallback callback) {
            onShowCustomView(view, callback); // To change body of overridden
            // methods use File | Settings |
            // File Templates.
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            webView.setVisibility(View.GONE);
            customViewContainer.setVisibility(View.VISIBLE);
            customViewContainer.addView(view);
            customViewCallback = callback;
        }

        @Override
        public View getVideoLoadingProgressView() {

            if (mVideoProgressView == null) {
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
//                mVideoProgressView = inflater.inflate(R.layout.video_progress,
//                        null);
            }
            return mVideoProgressView;
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView(); // To change body of overridden methods
            // use File | Settings | File Templates.
            if (mCustomView == null)
                return;

            webView.setVisibility(View.VISIBLE);
            customViewContainer.setVisibility(View.GONE);

            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            customViewContainer.removeView(mCustomView);
            customViewCallback.onCustomViewHidden();

            mCustomView = null;
        }
    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        Dialog loadingDialog = new Dialog(WebviewActivity.this);

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            webViewPreviousState = PAGE_STARTED;

//            if (loadingDialog == null || !loadingDialog.isShowing())
//                loadingDialog = ProgressDialog.show(getActivity(), "",
//                        "Loading Please Wait", true, true,
//                        new OnCancelListener() {
//
//                            @Override
//                            public void onCancel(DialogInterface dialog) {
//                                // do something
//                            }
//                        });
//
//            loadingDialog.setCancelable(false);

        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request,
                                    WebResourceError error) {



            super.onReceivedError(view, request, error);

        }

        @Override
        public void onReceivedHttpError(WebView view,
                                        WebResourceRequest request, WebResourceResponse errorResponse) {



            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            if (webViewPreviousState == PAGE_STARTED) {
                mPocketBar.progressiveStop();
//                if (null != loadingDialog) {
//                    loadingDialog.dismiss();
//                    loadingDialog = null;
//                }
            }
        }
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return is Device Connected
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != cm) {
            NetworkInfo info = cm.getActiveNetworkInfo();

            return (info != null && info.isConnected());
        }

        return false;

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }


        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.webView && event.getAction() == MotionEvent.ACTION_DOWN) {
            handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500);
        }
        return false;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == CLICK_ON_URL) {
            handler.removeMessages(CLICK_ON_WEBVIEW);
            return true;
        }
        if (msg.what == CLICK_ON_WEBVIEW) {
            Toast.makeText(getApplicationContext(), "WebView clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}
