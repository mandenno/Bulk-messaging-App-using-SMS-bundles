package com.nupola.okangi.nupola;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Control extends AppCompatActivity {
    public static final String JSON_URL = "http://sav-circuit.com/maziwaplus/login.php";
    private AppCompatButton appCompatButtonLogin;
    final Context context = this;
    ListView list;
    ProgressDialog progress;
    TextView group;
    LoginDataBaseAdapter2 loginDataBaseAdapter;
    public static final String CountUrl = "https://nupola.com/app/smsbalance.php";
    public static final String balUrl = "https://nupola.com/app/smsrecips.php";
    public static final String JSON_URL_DELETE = "https://nupola.com/app/deletegroup_large.php";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.control);


        //DB section
        final String gname = Control.this.getIntent().getExtras().getString("gname");
        loginDataBaseAdapter=new LoginDataBaseAdapter2(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        final String storedphone = loginDataBaseAdapter.getSinlgeEntry1();

        group=(TextView)findViewById(R.id.gnamel);
        group.setText(gname+" SMS group");

        final String[] web = new String[]{"Send Message", "My Account", "Delete this group"};
        CustomListSpannerlarge adapter = new CustomListSpannerlarge(this, web, new Integer[]{ Integer.valueOf(R.drawable.sms), Integer.valueOf(R.drawable.topup), Integer.valueOf(R.drawable.delete)});
        this.list = (ListView) findViewById(R.id.listycallarge);
        this.list.setAdapter(adapter);
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            class C02511 implements AdapterView.OnItemClickListener {
                C02511() {
                }

                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String Slecteditem = web[position];
                    int itempos = position;
                    if(itempos==0)
                    {
                        String gname=Control.this.getIntent().getExtras().getString("gname");
                        Intent i = new Intent(Control.this, BulkSMS.class);
                        i.putExtra("gname",gname);
                       // i.putExtra("code",storedphone);
                        startActivity(i);
                    }
                    else if(itempos==1)
                    {
                        Intent intent = new Intent(Control.this, MyAccount.class);
                        intent.putExtra("gname",gname);
                        intent.putExtra("code",storedphone);

                        startActivity(intent);

                    }
                    else if(itempos==2)
                    {
                        String gname=Control.this.getIntent().getExtras().getString("gname");
                        final String gid=Control.this.getIntent().getExtras().getString("id");
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Control.this);
                        alertDialogBuilder.setMessage("Are you sure you want to delete "+ gname+" group?");
                        alertDialogBuilder.setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        sendRequestd(gid);

                                    }
                                });

                        alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            }

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Control.this.list.setOnItemClickListener(new C02511());
            }
        });
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
            Control.this.progress.dismiss();

            Control.this.showJSONd(response);

        }
    }

    class C03982d implements ErrorListener {
        C03982d() {
        }

        public void onErrorResponse(VolleyError error) {

            Toast.makeText(Control.this,"Network Error!", Toast.LENGTH_SHORT).show();
            progress.dismiss();

        }
    }
    private void sendRequestd(final String id) {


        this.progress = ProgressDialog.show(this, "Deleting group", "Please Wait...", true);
        Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL_DELETE, new Control.C03971d(), new Control.C03982d()) {
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap();
                params.put("group_id",id);
                return params;
            }
        });

    }



    class C03949 implements ErrorListener {
        C03949() {
        }

        public void onErrorResponse(VolleyError error) {
        }
    }

    private void showJSONd(String resp){

        String repo=resp.toString();
        // Toast.makeText(context, resp, Toast.LENGTH_LONG).show();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Control.this);
        alertDialogBuilder.setMessage(repo);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }






        }



