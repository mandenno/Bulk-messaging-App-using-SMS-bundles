package com.nupola.okangi.nupola;

/**
 * Created by Okangi on 10/25/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class settings extends Activity {
Toolbar ct;
    public static final String JSON_URL_DELETE = "https://nupola.com/app/deletemember.php";
    ProgressDialog progress;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        setContentView(R.layout.settings);
        String member = settings.this.getIntent().getExtras().getString("member");
ct=(Toolbar)findViewById(R.id.ctbarxst);
        ct.setTitle("Manage "+member);
        ct.setTitleTextColor(Color.WHITE);
        // Get reference of widgets from XML layout
        final ListView lv = (ListView) findViewById(R.id.st);

        // Initializing a new String Array
        String[] fruits = new String[] {
                "\nRemove "+member+" from list\n",
                "\nCall "+member+"\n",
                "\nEdit contact info for "+member+"\n"
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
                  String member=settings.this.getIntent().getExtras().getString("member");
                  final String gname=settings.this.getIntent().getExtras().getString("gname");
                  final String gid=settings.this.getIntent().getExtras().getString("phone");
                  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(settings.this);
                  alertDialogBuilder.setMessage("Are you sure you want to remove "+member+" from "+ gname+" group?");
                  alertDialogBuilder.setPositiveButton("REMOVE",
                          new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface arg0, int arg1) {
                                  sendRequestd(gid,gname);

                              }
                          });

                  alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {

                      public void onClick(DialogInterface dialog, int which) {
                              dialog.dismiss();
                      }
                  });

                  AlertDialog alertDialog = alertDialogBuilder.create();
                  alertDialog.show();
              }
              else if(position==1)
              {
                  String member = settings.this.getIntent().getExtras().getString("member");
                  String phone = settings.this.getIntent().getExtras().getString("phone");

                  //Toast.makeText(Friends.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                  Intent callIntent = new Intent(Intent.ACTION_CALL);
                  callIntent.setData(Uri.parse("tel:" + phone));
                  if (ActivityCompat.checkSelfPermission(settings.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                      // TODO: Consider calling
                      ActivityCompat.requestPermissions(settings.this, PERMISSIONS, PERMISSION_ALL);
                      return;
                  }
                  startActivity(callIntent);
                  Toast.makeText(settings.this, "Calling "+member, Toast.LENGTH_SHORT).show();
              }
              else if(position==2)
              {
                  String member = settings.this.getIntent().getExtras().getString("member");
                  String phone = settings.this.getIntent().getExtras().getString("phone");
                  String gname = settings.this.getIntent().getExtras().getString("gname");
                  Intent intent = new Intent(settings.this, Edit.class);
                  intent.putExtra("gname",gname);
                  intent.putExtra("phone",phone);
                  intent.putExtra("member",member);
                  startActivity(intent);
              }

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
            settings.this.progress.dismiss();

            settings.this.showJSONd(response);

        }
    }

    class C03982d implements Response.ErrorListener {
        C03982d() {
        }

        public void onErrorResponse(VolleyError error) {

            Toast.makeText(settings.this,"Network Error!", Toast.LENGTH_SHORT).show();
            progress.dismiss();

        }
    }
    private void sendRequestd(final String id, final String gname) {


        this.progress = ProgressDialog.show(this, "Removing", "Please Wait...", true);
        Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL_DELETE, new settings.C03971d(), new settings.C03982d()) {
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap();
                params.put("phone",id);
                params.put("gname",gname);
                return params;
            }
        });

    }
    public void playBeep() {

        try {
            RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(2)).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(settings.this);
        alertDialogBuilder.setMessage(repo);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}