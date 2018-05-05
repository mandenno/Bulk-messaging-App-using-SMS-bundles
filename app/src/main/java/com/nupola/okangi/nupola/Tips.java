package com.nupola.okangi.nupola;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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

public class Tips extends AppCompatActivity {
    EditText tips;
    public static final String JSON_URL2 = "http://sav-circuit.com/betips/saveChat.php";
    final Context context = this;

    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        tips = (EditText) findViewById(R.id.tips);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.send);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
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
            Tips.this.progress.dismiss();
            try {
                Tips.this.showJSON1(response);
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
            Tips.this.progress.dismiss();
            Toast.makeText(Tips.this, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }
    private void update() {
        final String un = tips.getText().toString().trim();
        if (un.equals("")) {
            Toast.makeText(this, "Enter your tips first!", Toast.LENGTH_SHORT).show();

        } else {


            this.progress = ProgressDialog.show(this, "Posting", "Please Wait...", true);
            Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL2, new Tips.C03971(), new Tips.C03982()) {
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap();
                    params.put("info", un);

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



    private void showJSON1(String json) throws JSONException, InterruptedException {
            tips.setText("");
            Toast.makeText(this, json, Toast.LENGTH_LONG).show();

    }


}
