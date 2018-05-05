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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SMS extends AppCompatActivity implements View.OnClickListener {

        LoginDataBaseAdapter2 loginDataBaseAdapter;
        public static final String SENDSMS_URL = "http://nupola.com/app/sendsms_large.php";
      //  public static final String GET_RES = "http://tbcapp.org/app/recipients_sms.php";
    public static final String GET_RES = "https://nupola.com/app/recipients_large_sms.php?gname=";
        final Context context = this;
        ProgressDialog progress;
        AlertDialog alertDialogBuilder;
        Toolbar title;
        TextView hm;
        private TextView smsdata;
        FloatingActionButton sendsms;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_sms);

        String gname=SMS.this.getIntent().getExtras().getString("gname");
        String days=SMS.this.getIntent().getExtras().getString("days");
        String key=SMS.this.getIntent().getExtras().getString("key");

hm=(TextView)findViewById(R.id.hommy);

        smsdata = (EditText) findViewById(R.id.smsarea);

        if(key.equals(""))
        {
         smsdata.setEnabled(false);
            hm.setText("Your account is pending for activation\nBulk SMS | "+days+" days remaining\n (Status: Paid | SMS Disabled)");
            hm.setTextColor(Color.WHITE);
            hm.setBackgroundColor(Color.RED);
            smsdata.setHint("Messaging area not enabled!...");
        }
        else
        {
            smsdata.setEnabled(true);
            hm.setText("Messaging | "+days+" days remaining\n (Status: Paid | SMS Enabled)");
            hm.setTextColor(Color.BLACK);
            smsdata.setHint("Type message for "+gname+" group...");
        }

        loginDataBaseAdapter=new LoginDataBaseAdapter2(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        sendsms=(FloatingActionButton)findViewById(R.id.sendMessage);
        sendsms.setOnClickListener(this);


    }






    public void playBeep() {
        try {
            RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(2)).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if(view==sendsms) {
            String sms = smsdata.getText().toString();
            if (sms.equals("")) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Enter your message first please!");
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                               arg0.dismiss();
                            }
                        });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                playBeep();
                String gname=SMS.this.getIntent().getExtras().getString("gname");
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Send message to "+gname+" group?");
                alertDialogBuilder.setPositiveButton("SEND",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                fetchContactsf();
                            }
                        });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        }
    }


    class C02594 implements DialogInterface.OnClickListener {
        C02594() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C02605 implements DialogInterface.OnClickListener {
        C02605() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C03971 implements Response.Listener<String> {
        C03971() {
        }

        public void onResponse(String response) {
            SMS.this.progress.dismiss();

            SMS.this.showJSONd(response);

        }
    }

    class C03982 implements Response.ErrorListener {
        C03982() {
        }

        public void onErrorResponse(VolleyError error) {

            Toast.makeText(SMS.this,"Network Error!", Toast.LENGTH_SHORT).show();
            progress.dismiss();

        }
    }
    private void sendRequest(final String resp) {
        final String code = loginDataBaseAdapter.getSinlgeEntry1();
        final String gname=SMS.this.getIntent().getExtras().getString("gname");

        this.progress = ProgressDialog.show(this, "Sending message...", "", true);
        Volley.newRequestQueue(this).add(new StringRequest(1, SENDSMS_URL, new SMS.C03971(), new SMS.C03982()) {
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap();
                params.put("code", code);
                params.put("gname", gname);
                params.put("sms", smsdata.getText().toString());
                params.put("recipients", resp);
                return params;
            }
        });

    }


    class C02821 implements DialogInterface.OnClickListener {
        C02821() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C02832 implements DialogInterface.OnClickListener {
        C02832() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }



    //send sms
    //delete group
    class C02594d implements DialogInterface.OnClickListener {
        C02594d() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C02605d implements DialogInterface.OnClickListener {
        C02605d() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C03971d implements Response.Listener<String> {
        C03971d() {
        }

        public void onResponse(String response) {
            SMS.this.progress.dismiss();

            sendRequest(response);

        }
    }

    class C03982d implements Response.ErrorListener {
        C03982d() {
        }

        public void onErrorResponse(VolleyError error) {

            Toast.makeText(SMS.this,"Network Error!", Toast.LENGTH_SHORT).show();
            progress.dismiss();

        }
    }
    private void fetchContactsf() {

        final String gname=SMS.this.getIntent().getExtras().getString("gname");
        this.progress = ProgressDialog.show(this, "", "Getting contacts ready...", true);
        Volley.newRequestQueue(this).add(new StringRequest(1, GET_RES, new SMS.C03971d(), new SMS.C03982d()) {
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap();
                params.put("gname",gname);
                return params;
            }
        });

    }


    class C02821d implements DialogInterface.OnClickListener {
        C02821d() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C02832d implements DialogInterface.OnClickListener {
        C02832d() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }



    private void showJSONd(String resp){
        playBeep();

        // Toast.makeText(context, resp, Toast.LENGTH_LONG).show();
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SMS.this);
        alertDialogBuilder.setMessage(resp);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


}
