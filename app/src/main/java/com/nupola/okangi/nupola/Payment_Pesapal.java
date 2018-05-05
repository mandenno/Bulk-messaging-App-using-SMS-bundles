package com.nupola.okangi.nupola;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;


public class Payment_Pesapal extends Activity implements View.OnClickListener {
    /** Called when the activity is first created. */

    private WebView webView;
ImageView home;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_pesapal);




        this.webView = (WebView) findViewById(R.id.webview01xp);

        home=(ImageView)findViewById(R.id.imghome);
        home.setOnClickListener(this);

        //Toast.makeText(this, "Loading Site...Please wait...", 10000).show();
        loadUrl();


    }

    public void loadUrl()
    {

        String plan=Payment_Pesapal.this.getIntent().getExtras().getString("plan");
        String accountno=Payment_Pesapal.this.getIntent().getExtras().getString("accountno");
        String email=Payment_Pesapal.this.getIntent().getExtras().getString("email");
        String phone=Payment_Pesapal.this.getIntent().getExtras().getString("phone");
        String urlpath= "http://nupola.com/app/payment_session.php?accountno="+accountno+"& plan="+plan+"& email="+email+"& phone="+phone;

        webView.setWebViewClient(new myWebClient());
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(urlpath);
        webView.setWebChromeClient(new WebChromeClient() {
            private ProgressDialog mProgress;

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (mProgress == null) {
                    mProgress = new ProgressDialog(Payment_Pesapal.this);
                    mProgress.show();
                }
                mProgress.setMessage("Please wait..." + String.valueOf(progress) + "%");
                if (progress == 100) {
                    mProgress.dismiss();
                    mProgress = null;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view==home)
        {
            Intent intent = new Intent(Payment_Pesapal.this, Large.class);
            startActivity(intent);
        }
    }


    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);

            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub

            super.onPageFinished(view, url);

        }
    }


    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
