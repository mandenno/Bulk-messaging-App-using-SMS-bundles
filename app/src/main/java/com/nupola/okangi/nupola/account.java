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
import android.widget.TextView;
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

public class account extends AppCompatActivity implements View.OnClickListener {
    EditText username, name;
    Button registerButton;
TextView req,forgot;
    ProgressDialog progress;
    final Context context = this;
    LoginDataBaseAdapter2 loginDataBaseAdapter;
    public static final String JSON_URL = "http://nupola.com/app/code_verify.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_account);

        name = (EditText)findViewById(R.id.phonesmall);

        registerButton = (Button)findViewById(R.id.subButton);

        loginDataBaseAdapter=new LoginDataBaseAdapter2(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        req=(TextView)findViewById(R.id.request);
        req.setOnClickListener(this);
        forgot=(TextView)findViewById(R.id.forgot);
        forgot.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
if(view==req)
{
    Intent intent = new Intent(account.this, Request.class);
    startActivity(intent);
}
else if(view==forgot)
{
    Intent intent = new Intent(account.this, ForgotCode.class);
    startActivity(intent);
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
           account.this.progress.dismiss();
            try {
                account.this.showJSON(response);
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
          account.this.progress.dismiss();
            Toast.makeText(account.this, "Check your network connection and try again", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendRequest() {
        final String uname = name.getText().toString().trim();

            this.progress = ProgressDialog.show(this, "", "Please Wait...(Verifying)", true);
            Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new C03971(), new C03982()) {
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap();
                    params.put("code", uname);

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



    private void showJSON(String json) throws JSONException, InterruptedException {
        //  Toast.makeText(getApplicationContext(), "json: "+json, Toast.LENGTH_LONG).show();
        if (!json.equals("nun")) {


            try {
                JSONArray contacts = new JSONArray(json);

                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    String resp = c.getString("response");
                    final String code = name.getText().toString().trim();
if(resp.equals("success")) {
    loginDataBaseAdapter.insertEntry(code, resp);
   Intent intent = new Intent(account.this, Large.class);
  startActivity(intent);
    // Save the Data in Database
    finish();

    Toast.makeText(getApplicationContext(), "Code Verified Successfully! ", Toast.LENGTH_LONG).show();

}
else
{
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(account.this);
    alertDialogBuilder.setMessage("Sorry, the account code you provided is invalid!");
    alertDialogBuilder.setPositiveButton("OK",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
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

                }


            } catch (JSONException e) {
                e.printStackTrace();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(account.this);
                alertDialog.setTitle("Error!");
                alertDialog.setMessage("Error found while verifying your details.");
                alertDialog.setPositiveButton("OK", new account.C02821());
                alertDialog.setNegativeButton("Cancel", new account.C02832());
                alertDialog.setIcon(R.mipmap.ic_launcher).show();
            }
        } else {
            Toast.makeText(this, "Invalid Details!", Toast.LENGTH_SHORT).show();
            account.this.progress.dismiss();
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