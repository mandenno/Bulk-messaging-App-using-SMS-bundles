package com.nupola.okangi.nupola;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Loginx extends AppCompatActivity implements View.OnClickListener {

    public static final String JSON_URL = "http://sav-circuit.com/betips/loginadmin.php";

    final Context context = this;
    LoginDataBaseAdapter loginDataBaseAdapter;
    ProgressDialog progress;
    private EditText phone,pass;
private CardView lgn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        setContentView(R.layout.activity_loginx);
lgn=(CardView) findViewById(R.id.cardViewx0);
        lgn.setOnClickListener(this);
        pass=(EditText)findViewById(R.id.pass);
        phone=(EditText)findViewById(R.id.uname);
        // get Instance  of Database Adapter
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

    }

    @Override
    public void onClick(View v) {
        if(v==lgn)
        {
            sendRequest();
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
            Loginx.this.progress.dismiss();
            try {
                Loginx.this.showJSON(response);
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
            Loginx.this.progress.dismiss();

            Toast.makeText(Loginx.this, "Network Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void sendRequest() {
        final String un = pass.getText().toString();
        final String ph =phone.getText().toString();
        if (un.equals("") || ph.equals("")) {
            Toast.makeText(this, "Fill all the fields!", Toast.LENGTH_SHORT).show();

        } else {


            this.progress = ProgressDialog.show(this, "Verifying", "Please Wait...", true);
            Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new C03971(), new C03982()) {
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap();
                    params.put("phone", ph);
                    params.put("password", un);
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
        if (json.equals("truea")) {
            final String un = pass.getText().toString();
            final String ph =phone.getText().toString();
            loginDataBaseAdapter.insertEntry(ph, un);
                           Intent intent = new Intent(Loginx.this, Final.class);

                           startActivity(intent);


            } else {
                Toast.makeText(this, "Invalid Details!", Toast.LENGTH_SHORT).show();
            }

        }



    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        loginDataBaseAdapter.close();
    }


}
