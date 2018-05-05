package com.nupola.okangi.nupola;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.HashMap;
import java.util.Map;

public class manage extends AppCompatActivity {
    final Context context = this;
    ListView list;
    private AdView mAdView;
    TextView gnamex;
    public static final String JSON_URL = "http://nupola.com/app/recipients.php";
    public static final String JSON_URL_DELETE = "http://nupola.com/app/deletegroup.php";


    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        String gname=manage.this.getIntent().getExtras().getString("gname");
        String gid=manage.this.getIntent().getExtras().getString("id");
        MobileAds.initialize(this,
                "ca-app-pub-3246984456778707/6261148414");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if(gid.equals("new-group"))
        {
            Toast.makeText(context, "Please create a group first!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(manage.this, MainActivity.class);
                            startActivity(intent);
                            finish();
        }


        final String[] web = new String[]{"View Members - "+gname+" ", "Delete "+gname+" group", "ADD ADMIN \n Admin inherits all the group members but cannot make any changes to member details."};
        CustomListSpannerozy adapter = new CustomListSpannerozy(this, web, new Integer[]{Integer.valueOf(R.drawable.mygroups),Integer.valueOf(R.drawable.delete),Integer.valueOf(R.drawable.adduser)});
        this.list = (ListView) findViewById(R.id.listycalozy);
        this.list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            class C02511 implements AdapterView.OnItemClickListener {
                C02511() {
                }

                public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {
                    String Slecteditem = web[position];
                    int itempos = position;
                    if(itempos==0)
                    {
String gname=manage.this.getIntent().getExtras().getString("gname");
String gid=manage.this.getIntent().getExtras().getString("id");
                        Intent intent = new Intent(manage.this, Friends.class);
                        intent.putExtra("id", gid);
                        intent.putExtra("gname", gname);
                        startActivity(intent);
                    }

                    else if(itempos==1)
                    {
                        final String gname = manage.this.getIntent().getExtras().getString("gname");
                        if (gname.contains("Inherited")) {
                            Toast.makeText(manage.this, "This group is inherited! You can only send messages to members.", Toast.LENGTH_LONG).show();
                        } else {

                            final String gid = manage.this.getIntent().getExtras().getString("id");
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(manage.this);
                            alertDialogBuilder.setMessage("Are you sure you want to delete " + gname + " group?");
                            alertDialogBuilder.setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            sendRequestd(gid);

                                        }
                                    });

                            alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }

                    }
                    else if(itempos==2) {
                        final String gname = manage.this.getIntent().getExtras().getString("gname");
                        if (gname.contains("Inherited")) {
                            Toast.makeText(manage.this, "This group is inherited! You can only send messages to members.", Toast.LENGTH_LONG).show();
                        } else {
                            String gid = manage.this.getIntent().getExtras().getString("id");
                            Intent intent = new Intent(manage.this, Inherit.class);
                            intent.putExtra("id", gid);
                            intent.putExtra("gname", gname);
                            startActivity(intent);
                        }

                    }


                }



            }


            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                manage.this.list.setOnItemClickListener(new C02511());
            }
        });

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
            manage.this.progress.dismiss();

                manage.this.showJSON(response);

        }
    }

    class C03982 implements Response.ErrorListener {
        C03982() {
        }

        public void onErrorResponse(VolleyError error) {

            Toast.makeText(manage.this,"Network Error!", Toast.LENGTH_SHORT).show();
            progress.dismiss();

        }
    }
    private void sendRequest(final String id) {

        final String gname=manage.this.getIntent().getExtras().getString("gname");
        final String gid=manage.this.getIntent().getExtras().getString("id");

            this.progress = ProgressDialog.show(this, "Fetching Contacts", "Please Wait...", true);
            Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new C03971(), new C03982()) {
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap();
                    params.put("gname", gname);
                    params.put("gid", gid);
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
    public void playBeep() {

        try {
            RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(2)).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showJSON(String recipients){
playBeep();

                        Uri sms_uri = Uri.parse("smsto:" + recipients);
                        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                        sms_intent.putExtra("sms_body", "");
                        startActivity(sms_intent);
        Toast.makeText(context, "Opening SMS platform", Toast.LENGTH_LONG).show();


    }




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
            manage.this.progress.dismiss();

            manage.this.showJSONd(response);

        }
    }

    class C03982d implements Response.ErrorListener {
        C03982d() {
        }

        public void onErrorResponse(VolleyError error) {

            Toast.makeText(manage.this,"Network Error!", Toast.LENGTH_SHORT).show();
            progress.dismiss();

        }
    }
    private void sendRequestd(final String id) {


        this.progress = ProgressDialog.show(this, "Deleting group", "Please Wait...", true);
        Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL_DELETE, new C03971d(), new C03982d()) {
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap();
                params.put("group_id",id);
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
String repo=resp.toString();
       // Toast.makeText(context, resp, Toast.LENGTH_LONG).show();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(manage.this);
        alertDialogBuilder.setMessage(repo);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(manage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
