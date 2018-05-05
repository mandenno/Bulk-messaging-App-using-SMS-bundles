package com.nupola.okangi.nupola;

/**
 * Created by Okangi on 10/25/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyAccount extends Activity {
Toolbar ct;
    LoginDataBaseAdapter2 loginDataBaseAdapter;
    public static final String  balUrl = "https://nupola.com/app/smsbalance.php";
    public static final String CountUrl = "https://nupola.com/app/smsrecips.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.myaccount);
        loginDataBaseAdapter=new LoginDataBaseAdapter2(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        final String storedphone = loginDataBaseAdapter.getSinlgeEntry1();
        String gname=MyAccount.this.getIntent().getExtras().getString("gname");
        String gid=MyAccount.this.getIntent().getExtras().getString("gid");
        final String key = MyAccount.this.getIntent().getExtras().getString("key");
        ct=(Toolbar)findViewById(R.id.ctbarxp);



        ct.setTitle("My Account");
        ct.setTitleTextColor(Color.WHITE);
        // Get reference of widgets from XML layout
        final ListView lv = (ListView) findViewById(R.id.lvxp);

        // Initializing a new String Array
        String[] fruits = new String[] {
                "\nSMS BALANCE\n",
                "\nTOP UP\n",
                "MY RECIPIENTS"
        };

        // Create a List from String Array elements
        final List<String> fruits_list = new ArrayList<String>(Arrays.asList(fruits));

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, fruits_list);

        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Animate the background color of clicked Item
                ColorDrawable[] color = {
                        new ColorDrawable(Color.parseColor("#E0F7FA")),
                        new ColorDrawable(Color.parseColor("#B2EBF2"))
                };
                TransitionDrawable trans = new TransitionDrawable(color);
                view.setBackground(trans);
                trans.startTransition(2000); // duration 2 seconds

                // Go back to the default background color of Item
                ColorDrawable[] color2 = {
                        new ColorDrawable(Color.parseColor("#B2EBF2")),
                        new ColorDrawable(Color.parseColor("#E0F7FA"))
                };
                TransitionDrawable trans2 = new TransitionDrawable(color2);
                view.setBackground(trans2);
                trans2.startTransition(2000); // duration 2 seconds

                String Slecteditem = fruits_list.get(position);
               // Toast.makeText(Counties.this, "YOUR BUSINESS PREMISE IS IN "+Slecteditem+" COUNTY", Toast.LENGTH_LONG).show();
              if(position==0)
              {
                  if(key.equals(""))
                  {
                      Toast.makeText(MyAccount.this, "This account is not enabled!", Toast.LENGTH_LONG).show();
                  }
                  else {
                      smsBal();
                  }

              }
              else if(position==1)
              {
                  if(key.equals(""))
                  {
                      Toast.makeText(MyAccount.this, "This account is not enabled!", Toast.LENGTH_LONG).show();
                  }
                  else {
                      Intent i = new Intent(MyAccount.this, WebViewClientDemoActivity.class);
                      i.putExtra("link", "http://nupola.com/app/topup.php?code=" + storedphone);
                      startActivity(i);
                  }

              }
              else if(position==2)
              {
                  if(key.equals(""))
                  {
                      Toast.makeText(MyAccount.this, "This account is not enabled!", Toast.LENGTH_LONG).show();
                  }
                  else {
                      CountAll();
                  }
              }

            }
        });
    }

    // Creating user login function.
    public void smsBal() {
        final ProgressDialog dialog = new ProgressDialog(MyAccount.this);
        dialog.setMessage("Checking Balance");
        dialog.show();


        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, balUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
playBeep();
                    dialog.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyAccount.this);
                        alertDialogBuilder.setMessage("Balance: "+ServerResponse);
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

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        //  progressDialog.dismiss();

                        // Showing error message if something goes wrong.
                        // Toast.makeText(Control.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                String gname=MyAccount.this.getIntent().getExtras().getString("gname");
                String gid=MyAccount.this.getIntent().getExtras().getString("gid");
                final String storedphone = loginDataBaseAdapter.getSinlgeEntry1();
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                // The firs argument should be same sa your MySQL database table columns.
                params.put("gname", gname);
                params.put("gid", gid);
                params.put("code",storedphone );


                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(MyAccount.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
    public void playBeep() {

        try {
            RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(2)).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Creating user login function.
    public void CountAll() {
        final ProgressDialog dialog = new ProgressDialog(MyAccount.this);
        dialog.setMessage("Counting...");
       dialog.show();

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, CountUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        playBeep();
                        dialog.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyAccount.this);
                        alertDialogBuilder.setMessage(ServerResponse+" recipient(s)");
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

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        dialog.dismiss();

                        // Showing error message if something goes wrong.
                        // Toast.makeText(Control.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                String gname=MyAccount.this.getIntent().getExtras().getString("gname");
                String gid=MyAccount.this.getIntent().getExtras().getString("gid");
                final String storedphone = loginDataBaseAdapter.getSinlgeEntry1();
                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                // The firs argument should be same sa your MySQL database table columns.
                params.put("gname", gname);
                params.put("gid", gid);
                params.put("code",storedphone );


                return params;
            }

        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(MyAccount.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }
}