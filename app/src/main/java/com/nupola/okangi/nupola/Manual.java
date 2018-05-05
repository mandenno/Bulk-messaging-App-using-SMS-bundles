package com.nupola.okangi.nupola;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class Manual extends AppCompatActivity implements View.OnClickListener {
Button got;
    WebView webview;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_manual);
        got=(Button)findViewById(R.id.got);
        got.setOnClickListener(this);
        init();

    }

    private void init(){
        webview = (WebView)findViewById(R.id.webviewmanual);
        webview.loadUrl("file:///android_asset/manual.html");
        webview.requestFocus();

        progressDialog = new ProgressDialog(Manual.this);
        progressDialog.setMessage("Opening Manual");
        progressDialog.setCancelable(false);
        progressDialog.show();

        webview.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        if(view==got)
        {
            String gid = Manual.this.getIntent().getExtras().getString("id");
            String url = "http://www.nupola.com/groups/session.php?gid="+gid;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse((url)));
            startActivity(i);
            Toast.makeText(Manual.this, "Please wait...", Toast.LENGTH_LONG).show();
            //Toast.makeText(Friends.this, "Please Wait!", Toast.LENGTH_SHORT).show();

        }
    }
}
