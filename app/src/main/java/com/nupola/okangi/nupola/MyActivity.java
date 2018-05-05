package com.nupola.okangi.nupola;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MyActivity extends Activity {
    private static final String COUNT_URL = "http://sav-circuit.com/bride/getMessage.php";
    private static final String TAG = MyActivity.class.getSimpleName();

    private Uri uriContact;
    private String contactID;     // contacts unique ID
    LoginDataBaseAdapter loginDataBaseAdapter;
private Button schat;
    private ImageView iv;
    ProgressDialog progress;
    TextView tvm;
    final Context context = this;
    private InterstitialAd mInterstitialAd;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        MobileAds.initialize(this,
                "ca-app-pub-3246984456778707~7594056165");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3246984456778707/9631209919");

        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        this.loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        this.loginDataBaseAdapter = this.loginDataBaseAdapter.open();
        String storedname = this.loginDataBaseAdapter.getSinlgeEntry1();
        String storedpass = this.loginDataBaseAdapter.getSinlgeEntry2();

        // Toast.makeText(this,"User:"+storedname+" Pass: "+storedpass,Toast.LENGTH_LONG).show();
        if (storedname.equals("NOT EXIST")) {
          //  Toast.makeText(this,"Do you have an account?", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
            finish();
        }
        else  {


                startActivity(new Intent(MyActivity.this, Bulktype.class));
                finish();


        }

        setContentView(R.layout.mainx);


    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        loginDataBaseAdapter.close();
    }
}