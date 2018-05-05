package com.nupola.okangi.nupola;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.kevinomyonga.pesapaldroid.IRequest;
import com.kevinomyonga.pesapaldroid.Pesapal;
import com.kevinomyonga.pesapaldroid.exception.PesapalException;
import com.kevinomyonga.pesapaldroid.ipn.DefaultIpnRequest;
import com.kevinomyonga.pesapaldroid.post.PostRequest;


public class Payment extends AppCompatActivity {

    private static final boolean DEBUG = true;
    private static final String TAG = "Payment";
    private static final String HTTP_ERROR = "<html><body><h1>OOps!!</h1><b><p>%s</p></body></html>";

    // Please provide your consumer key here
    private static String consumer_key = "K7bPI26lvAD1sZw5/e8cSoFskWtqYTh6";//"change_with_your_consumer_key";

    // Please provide your consumer secret here
    private static String consumer_secret = "S2Mm5aEgkrW5BBJ7RssuIAZyPzI=";//change_with_your_consumer_secret";

    private PostRequest request;
    private WebViewClient webViewClient = new WebViewClient(){

        /* (non-Javadoc)
         * @see android.webkit.WebViewClient#onReceivedError(android.webkit.WebView, int, java.lang.String, java.lang.String)
         */
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            Toast.makeText(Payment.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            if(DEBUG)
                Log.d(TAG, "onPageStarted:"+url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            if(DEBUG)Log.d(TAG, "override : "+url);

            String callback  = request.getCallback();
            if(url.startsWith(callback )){
                try {
					/*
					Map<String,String> map = IpnUtil.getIpnParamsFromUrl(url);
					String id = map.get(AbIpnRequest.PARAM_TARCK_ID);
					String reference = map.get(AbIpnRequest.PARAM_MERCHANT_REFERECE);
					if(DEBUG){
						Log.d(Pesapal.TAG, "Reference "+reference);
					}
					*/
                    IRequest ipn;
                    //ipn = new IpnRequestStatus(id,reference);
                    //ipn = new IpnRequestStatusDetail(id,reference);
                    //ipn = new IpnRequestStatusByMerchatRef(reference);
                    ipn = new DefaultIpnRequest(url); // only if no change the default callback!! otherwise not work
                    view.loadUrl(ipn.getURL());
                } catch (PesapalException e) {
                    // TODO Auto-generated catch block
                    String msg = String.format(HTTP_ERROR, e.getMessage());
                    view.loadData(msg, "text/html", null);
                }
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

    };




    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_payment);

        setProgressBarVisibility(true);
        Pesapal.initialize(consumer_key, consumer_secret);

        Pesapal.setDEMO(true);

        request = createPostRequest();
        final Activity activity = this;


        final WebView webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(new WebChromeClient(){

            /* (non-Javadoc)
             * @see android.webkit.WebChromeClient#onProgressChanged(android.webkit.WebView, int)
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                activity.setProgress(newProgress*100);
            }

        });

        try {
            String url = request.getURL();
            webView.loadUrl(url);
        } catch (PesapalException e) {
            // TODO Auto-generated catch block
            String msg = String.format(HTTP_ERROR, e.getMessage());
            webView.loadData(msg, "text/html", null);
        }

    }



    /**
     * create the payment request
     * Use the builder to create the request
     */
    private PostRequest createPostRequest() {
        // TODO Auto-generated method stub
        PostRequest.Builder builder = new PostRequest.Builder();

        //	Can take it from editText or where there you want
        builder
                .isMobile(true)
                .amount("1000")
                .description("SEMA BULK SMS")
                .mail("lib@mail.com")
                .name("MFA", "ACADEMY");

        return builder.build();
    }

}
