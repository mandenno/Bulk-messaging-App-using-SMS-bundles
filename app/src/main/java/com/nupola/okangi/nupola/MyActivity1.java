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

public class MyActivity1 extends Activity {

    private static final String TAG = MyActivity1.class.getSimpleName();

    private Uri uriContact;
    private String contactID;     // contacts unique ID
    LoginDataBaseAdapter2 loginDataBaseAdapter;
private Button schat;
    private ImageView iv;
    ProgressDialog progress;
    TextView tvm;
    final Context context = this;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


        loginDataBaseAdapter=new LoginDataBaseAdapter2(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        String storedname = this.loginDataBaseAdapter.getSinlgeEntry1();


        // Toast.makeText(this,"User:"+storedname+" Pass: "+storedpass,Toast.LENGTH_LONG).show();
        if (storedname.equals("NOT EXIST")) {
          //  Toast.makeText(this,"Do you have an account?", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RequestCode.class);
            startActivity(intent);
            finish();
        }
        else  {

               startActivity(new Intent(MyActivity1.this, Large.class));
               finish();


        }

        setContentView(R.layout.mainx1);


    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        loginDataBaseAdapter.close();
    }
}