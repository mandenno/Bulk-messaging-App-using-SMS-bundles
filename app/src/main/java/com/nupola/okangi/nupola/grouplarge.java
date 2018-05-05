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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class grouplarge extends AppCompatActivity {
    EditText username, name;
    Button registerButton;

    ProgressDialog progress;
    final Context context = this;
    LoginDataBaseAdapter2 loginDataBaseAdapter;
    public static final String JSON_URL = "http://nupola.com/app/reg_group_large.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_grouplarge);

        name = (EditText)findViewById(R.id.accodelarge);

        registerButton = (Button)findViewById(R.id.registerButton);

        loginDataBaseAdapter=new LoginDataBaseAdapter2(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
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
           grouplarge.this.progress.dismiss();
            try {
                grouplarge.this.showJSON(response);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class C03982 implements Response.ErrorListener {
        C03982() {
        }

        public void onErrorResponse(VolleyError error) {
          grouplarge.this.progress.dismiss();
            Toast.makeText(grouplarge.this, "Check your network connection and try again", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendRequest() {
        final String uname = name.getText().toString().trim();
        final String storedname = this.loginDataBaseAdapter.getSinlgeEntry1();

        if (uname.equals("")) {
            Toast.makeText(context, "Enter group name please...", Toast.LENGTH_SHORT).show();
        } else {
            this.progress = ProgressDialog.show(this, "", "Please Wait...(Creating)", true);
            Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new C03971(), new C03982()) {
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap();
                    params.put("code", storedname);
                    params.put("gname", uname);

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



    private void showJSON(String json) throws JSONException, InterruptedException {
       // Toast.makeText(getApplicationContext(), "json: "+json, Toast.LENGTH_LONG).show();
        if (!json.equals("nun")) {


            try {
                JSONArray contacts = new JSONArray(json);

                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    String userData = c.getString("gname");
                    String response = c.getString("response");
if(response.equals("success"))
{
    playBeep();
   // Toast.makeText(getApplicationContext(), userData+" group created successfully", Toast.LENGTH_SHORT).show();
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(grouplarge.this);
    alertDialogBuilder.setMessage(userData+" group created successfully");
    alertDialogBuilder.setPositiveButton("OK",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent(grouplarge.this, Large.class);
                    startActivity(intent);
                    // Save the Data in Database
                    arg0.dismiss();

                }
            });

    alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(grouplarge.this, Large.class);
            startActivity(intent);
            // Save the Data in Database
            dialog.dismiss();
        }
    });

    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();

}
else if(response.equals("exist"))
{
    playBeep();
   // Toast.makeText(getApplicationContext(), "Failed to register group. Try again please.", Toast.LENGTH_SHORT).show();
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(grouplarge.this);
    alertDialogBuilder.setMessage("Sorry, the group name "+userData+" exists in your account!");
    alertDialogBuilder.setPositiveButton("OK",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();

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
else
{
    playBeep();
    // Toast.makeText(getApplicationContext(), "Failed to register group. Try again please.", Toast.LENGTH_SHORT).show();
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(grouplarge.this);
    alertDialogBuilder.setMessage("Failed to create "+userData+" exist!");
    alertDialogBuilder.setPositiveButton("OK",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();

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

            } catch (JSONException e) {
                e.printStackTrace();
                playBeep();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(grouplarge.this);
                alertDialog.setTitle("Error!");
                alertDialog.setMessage("Error encountered while creating account."+e.getMessage());
                alertDialog.setPositiveButton("OK", new grouplarge.C02821());
                alertDialog.setNegativeButton("Cancel", new grouplarge.C02832());
                alertDialog.setIcon(R.mipmap.ic_launcher).show();
            }
        } else {
            Toast.makeText(this, "Invalid Details!", Toast.LENGTH_SHORT).show();
        }


    }
    public void playBeep() {

        try {
            RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(2)).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        loginDataBaseAdapter.close();
    }
}