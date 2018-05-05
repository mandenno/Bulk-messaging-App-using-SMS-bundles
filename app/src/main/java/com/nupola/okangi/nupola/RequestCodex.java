package com.nupola.okangi.nupola;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class RequestCodex extends AppCompatActivity implements View.OnClickListener {
    EditText em, pas;
    Button registerButton;
    String user, pass;
    TextView forgot;
    ProgressDialog progress;
    final Context context = this;
    LoginDataBaseAdapter2 loginDataBaseAdapter;
    LoginDataBaseAdapter loginDataBaseAdapter1;
    public static final String JSON_URL = "http://nupola.com/app/getcodex.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_codex);

       em = (EditText)findViewById(R.id.codex);
       forgot = (TextView) findViewById(R.id.forgotcody);
       forgot.setOnClickListener(this);

        registerButton = (Button)findViewById(R.id.suby);

        loginDataBaseAdapter=new LoginDataBaseAdapter2(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        loginDataBaseAdapter1=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter1=loginDataBaseAdapter1.open();


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view==forgot)
        {
            startActivity(new Intent(RequestCodex.this, ForgotCode.class));
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
           RequestCodex.this.progress.dismiss();
            try {
                RequestCodex.this.showJSON(response);
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
          RequestCodex.this.progress.dismiss();
            Toast.makeText(RequestCodex.this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }
    private void sendRequest() {
        final String code= em.getText().toString().trim();
       // final String password = pas.getText().toString().trim();
        if(code.equals(""))
        {
            Toast.makeText(context, "Please provide your code...", Toast.LENGTH_LONG).show();
        }
        else {
            final String email = loginDataBaseAdapter1.getSinlgeEntry1();


            this.progress = ProgressDialog.show(this, "Verifying code", "Please Wait...", true);
            Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new C03971(), new C03982()) {
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap();
                    params.put("email", email);
                    params.put("code", em.getText().toString());


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
      //  Toast.makeText(getApplicationContext(), "json: "+json, Toast.LENGTH_LONG).show();
        if (!json.contains("nun")) {


            try {
                JSONArray contacts = new JSONArray(json);

                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    String code = c.getString("username");
                    String email = c.getString("email");
                    String response = c.getString("response");
if(response.contains("true")) {


    loginDataBaseAdapter.insertEntry(code, email);
    Intent intent = new Intent(RequestCodex.this, Large.class);
    startActivity(intent);
    // Save the Data in Database
    finish();

    Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();

}
else
{
    Toast.makeText(getApplicationContext(), "Invalid account code or does not exist!", Toast.LENGTH_LONG).show();
}


                }

            } catch (JSONException e) {
                e.printStackTrace();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(RequestCodex.this);
                alertDialog.setTitle("Error!");
                alertDialog.setMessage("We have encountered an error :"+json);
                alertDialog.setPositiveButton("OK", new RequestCodex.C02821());
                alertDialog.setNegativeButton("Cancel", new RequestCodex.C02832());
                alertDialog.setIcon(R.mipmap.ic_launcher).show();
            }
        } else {
            Toast.makeText(this, "The Details", Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        loginDataBaseAdapter.close();
    }
}