package com.nupola.okangi.nupola;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Request extends AppCompatActivity implements View.OnClickListener {
Button req;
    ProgressDialog progress;
    EditText name;
    final Context context = this;
    LoginDataBaseAdapter loginDataBaseAdapter;
    public static final String JSON_URL = "http://nupola.com/app/requestcode.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_request);
        req=(Button)findViewById(R.id.request);
        req.setOnClickListener(this);
        name = (EditText)findViewById(R.id.email);
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
            Request.this.progress.dismiss();

                Request.this.showJSON(response);

        }
    }

    class C03982 implements Response.ErrorListener {
        C03982() {
        }

        public void onErrorResponse(VolleyError error) {
            Request.this.progress.dismiss();
            Toast.makeText(Request.this, "Check your network connection and try again", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendRequest() {
        final String uname = name.getText().toString().trim();
        final String storedname = this.loginDataBaseAdapter.getSinlgeEntry1();


        this.progress = ProgressDialog.show(this, "Requesting", "Please Wait...", true);
        Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new Request.C03971(), new Request.C03982()) {
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap();
                params.put("phone", storedname);
                params.put("email", uname);

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



    private void showJSON(String json)  {

                        playBeep();
                        // Toast.makeText(getApplicationContext(), userData+" group created successfully", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Request.this);
                        alertDialogBuilder.setMessage(json);
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(Request.this, MainActivity.class);
                                        startActivity(intent);
                                        // Save the Data in Database
                                        finish();

                                    }
                                });

                        alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Request.this, MainActivity.class);
                                startActivity(intent);
                                // Save the Data in Database
                                finish();
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

    }
}
