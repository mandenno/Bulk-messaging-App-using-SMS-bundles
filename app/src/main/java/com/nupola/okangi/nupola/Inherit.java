package com.nupola.okangi.nupola;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Inherit extends AppCompatActivity implements View.OnClickListener {
Button req;
    ProgressDialog progress;
    EditText name;
    TextView admin;
    final Context context = this;
    LoginDataBaseAdapter loginDataBaseAdapter;
    public static final String JSON_URL = "http://nupola.com/app/inherit.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_inherit);
        req=(Button)findViewById(R.id.requestpxi);
        req.setOnClickListener(this);
        name = (EditText)findViewById(R.id.emailpi);
        admin=(TextView)findViewById(R.id.admin);
        String gname=Inherit.this.getIntent().getExtras().getString("gname");

        admin.setText("Add admin to "+gname);
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
            Inherit.this.progress.dismiss();

                Inherit.this.showJSON(response);

        }
    }

    class C03982 implements Response.ErrorListener {
        C03982() {
        }

        public void onErrorResponse(VolleyError error) {
            Inherit.this.progress.dismiss();
            Toast.makeText(Inherit.this, "Check your network connection and try again", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendRequest() {
        final String uname = name.getText().toString().trim();
     //   final String storedname = this.loginDataBaseAdapter.getSinlgeEntry1();

        if (uname.equals("")) {
            Toast.makeText(context, "Enter the user email address please", Toast.LENGTH_SHORT).show();
        } else {
           final String gid=Inherit.this.getIntent().getExtras().getString("id");
            this.progress = ProgressDialog.show(this, "Sending Request", "Please Wait...", true);
            Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new Inherit.C03971(), new Inherit.C03982()) {
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap();
                    params.put("email", uname);
                    params.put("gid", gid);

                    return params;
                }
            });
        }
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



    private void showJSON(String json)  {

                        playBeep();
                        // Toast.makeText(getApplicationContext(), userData+" group created successfully", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Inherit.this);
                        alertDialogBuilder.setMessage(json);
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();

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
    public void playBeep() {

        try {
            RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(2)).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
if(view==req)
{
    sendRequest();
}
    }
}
