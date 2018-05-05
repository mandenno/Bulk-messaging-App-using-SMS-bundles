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

public class Register extends AppCompatActivity {
    EditText em, pas;
    Button registerButton;
    String user, pass;
    TextView login;
    ProgressDialog progress;
    final Context context = this;
    LoginDataBaseAdapter loginDataBaseAdapter;
    public static final String JSON_URL = "http://nupola.com/app/semauser.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_register);

        pas = (EditText)findViewById(R.id.phonesmall);

       em = (EditText)findViewById(R.id.username);

        registerButton = (Button)findViewById(R.id.registerButton);
        login = (TextView)findViewById(R.id.login);
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
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
           Register.this.progress.dismiss();
            try {
                Register.this.showJSON(response);
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
          Register.this.progress.dismiss();
            Toast.makeText(Register.this, "Network connection error"+error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void sendRequest() {
        final String email= em.getText().toString().trim();
        final String password = pas.getText().toString().trim();
        if(email.equals("")||password.equals(""))
        {
            Toast.makeText(context, "Your email and password are important!", Toast.LENGTH_LONG).show();
        }
        else {


            this.progress = ProgressDialog.show(this, "Signup", "Please Wait...(Setting up)", true);
            Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new C03971(), new C03982()) {
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap();
                    params.put("email", email);
                    params.put("password", password);

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
        if (!json.equals("nun")) {


            try {
                JSONArray contacts = new JSONArray(json);

                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    String userData = c.getString("username");
                    String response = c.getString("response");
if(response.equals("true")) {
    final String pass = pas.getText().toString().trim();
    final String email = em.getText().toString().trim();

    loginDataBaseAdapter.insertEntry(email, pass);
    Intent intent = new Intent(Register.this, Bulktype.class);
    startActivity(intent);
    // Save the Data in Database
    finish();

    Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();

}
else if(response.equals("failed"))
{
    Toast.makeText(getApplicationContext(), "Failed to create account", Toast.LENGTH_LONG).show();
}
else if(response.equals("exist"))
{
    Toast.makeText(getApplicationContext(), "The email address is aready in use!", Toast.LENGTH_LONG).show();
}

                }

            } catch (JSONException e) {
                e.printStackTrace();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Register.this);
                alertDialog.setTitle("Error!");
                alertDialog.setMessage("Failed to create account.");
                alertDialog.setPositiveButton("OK", new Register.C02821());
                alertDialog.setNegativeButton("Cancel", new Register.C02832());
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