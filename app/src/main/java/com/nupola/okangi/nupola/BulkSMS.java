package com.nupola.okangi.nupola;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class BulkSMS extends AppCompatActivity implements OnClickListener {
    LoginDataBaseAdapter2 loginDataBaseAdapter;
    public static final String SMS_URL = "http://nupola.com/app/sendsms.php";
    final Context context = this;
    ProgressDialog progress;
    AlertDialog alertDialogBuilder;
    Toolbar title,desc;
    private TextView smsdata;
    FloatingActionButton exit;
    CardView landy;

    @Override
    public void onClick(View view) {
        if(view==exit)
        {
            landy=(CardView)findViewById(R.id.cview);
            landy.setVisibility(View.GONE);

        }
    }

    class C02431 implements OnClickListener {
        C02431() {
        }

        public void onClick(View view) {
            BulkSMS.this.sendSMS();
        }
    }

    class C02442 implements DialogInterface.OnClickListener {
        C02442() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C02453 implements DialogInterface.OnClickListener {

        class C03911 implements Listener<String> {
            C03911() {
            }

            public void onResponse(String response) {
                BulkSMS.this.progress.dismiss();
                BulkSMS.this.showJSONSMS(response);
            }
        }

        class C03922 implements ErrorListener {
            C03922() {
            }

            public void onErrorResponse(VolleyError error) {
                BulkSMS.this.progress.dismiss();
                Toast.makeText(BulkSMS.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        }

        C02453() {
        }

        public void onClick(DialogInterface dialog, int id) {
            final String code = loginDataBaseAdapter.getSinlgeEntry1();
            BulkSMS.this.progress = ProgressDialog.show(BulkSMS.this, "Sending...", "Sit back & relax..", true);
            Volley.newRequestQueue(BulkSMS.this).add(new StringRequest(1, BulkSMS.SMS_URL, new C03911(), new C03922()) {
                protected Map<String, String> getParams() {
                    String sms = BulkSMS.this.smsdata.getText().toString();
                    Map<String, String> params = new HashMap();
                    params.put("sms", sms);
                    params.put("code", code);
                    params.put("gname", BulkSMS.this.getIntent().getExtras().getString("gname"));
                    return params;
                }
            });
            dialog.cancel();
        }
    }

    class C02464 implements DialogInterface.OnClickListener {
        C02464() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C02475 implements DialogInterface.OnClickListener {
        C02475() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C02486 implements DialogInterface.OnClickListener {
        C02486() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C02497 implements DialogInterface.OnClickListener {
        C02497() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_bulk_sms);
        title=(Toolbar)findViewById(R.id.title);
        desc=(Toolbar)findViewById(R.id.desc);
        String gname=BulkSMS.this.getIntent().getExtras().getString("gname");
        title.setTitle(gname+" SMS group");
        title.setTitleTextColor(Color.WHITE);
        desc.setTitleTextColor(Color.WHITE);
        exit=(FloatingActionButton)findViewById(R.id.fbcancel);
        exit.setOnClickListener(this);
        desc.setTitle("Kindly check if you have enough SMS bundles before sending your message!");
        this.smsdata = (EditText) findViewById(R.id.smsvalx);
        ((FloatingActionButton) findViewById(R.id.fabysmsx)).setOnClickListener(new C02431());

        loginDataBaseAdapter=new LoginDataBaseAdapter2(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();


    }

    private void sendSMS() {
        Builder alertDialogBuilder = new Builder(this.context);
        alertDialogBuilder.setTitle((CharSequence) "Confirm");
        alertDialogBuilder.setMessage((CharSequence) "Are you sure to send this message?.Ensure your SMS balance is equal or more than the recipients number.").setCancelable(false).setPositiveButton((CharSequence) "Ok", new C02453()).setNegativeButton((CharSequence) "Cancel", new C02442());
        alertDialogBuilder.create().show();
    }

    private void showJSONSMS(String json) {
        if (json.equals("sent")) {
            Builder alertDialogBuilder = new Builder(this.context);
            alertDialogBuilder.setTitle((CharSequence) "Sent!");
            alertDialogBuilder.setMessage((CharSequence) "SMS sent successfully!").setCancelable(false).setPositiveButton((CharSequence) "Ok", new C02475()).setNegativeButton((CharSequence) "Cancel", new C02464());
            alertDialogBuilder.create().show();
            return;
        }
        else {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(BulkSMS.this);
            alertDialogBuilder.setMessage("Failed: "+json);
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();

                        }
                    });

            alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            //Toast.makeText(this, "Failed:"+json, Toast.LENGTH_SHORT).show();
        }
    }




    public void playBeep() {
        try {
            RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(2)).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
