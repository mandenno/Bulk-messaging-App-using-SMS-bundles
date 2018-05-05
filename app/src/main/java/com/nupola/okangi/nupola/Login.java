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

public class Login extends AppCompatActivity {
    EditText phonen, passw;
    Button registerButton;
    String user, pass;
    TextView forgot;
    ProgressDialog progress;
    final Context context = this;
    LoginDataBaseAdapter loginDataBaseAdapter;
    public static final String JSON_URL = "http://nupola.com/app/semauserlogin.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_login);

        passw = (EditText)findViewById(R.id.passwordx);

        phonen = (EditText)findViewById(R.id.phonenox);

        registerButton = (Button)findViewById(R.id.registerButtonx);
        forgot = (TextView)findViewById(R.id.forgotx);
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPass.class));
            }
        });

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
           Login.this.progress.dismiss();
            try {
                Login.this.showJSON(response);
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
          Login.this.progress.dismiss();
            Toast.makeText(Login.this, "Check your network connection and try again:"+error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void sendRequest() {
        final String phone = phonen.getText().toString().trim();
        final String pass = passw.getText().toString().trim();


            this.progress = ProgressDialog.show(this, "Verifying", "Please Wait...", true);
            Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new C03971(), new C03982()) {
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap();
                    params.put("password", pass);
                    params.put("email", phone);

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
        if (!json.equalsIgnoreCase("nun")) {


            try {
                JSONArray contacts = new JSONArray(json);

                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    //String userData = c.getString("username");
                    String response = c.getString("response");
                    if (response.contains("false")) {
                        Toast.makeText(getApplicationContext(), "Invalid user details. Click forgot password to retrieve your password.", Toast.LENGTH_LONG).show();

                    }
else {
                        final String un = phonen.getText().toString().trim();
                        final String ph = passw.getText().toString().trim();

                        loginDataBaseAdapter.insertEntry(un, ph);
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        // Save the Data in Database
                        finish();

                        Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();

                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Login.this);
                alertDialog.setTitle("Error!");
                alertDialog.setMessage("Failed to create account.");
                alertDialog.setPositiveButton("OK", new Login.C02821());
                alertDialog.setNegativeButton("Cancel", new Login.C02832());
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