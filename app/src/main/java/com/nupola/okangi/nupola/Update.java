package com.nupola.okangi.nupola;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Update extends AppCompatActivity {

    public static final String JSON_URL = "http://sav-circuit.com/betips/update.php";
    public static final String JSON_URL2 = "http://sav-circuit.com/betips/yuu.php";

    final Context context = this;

    ProgressDialog progress;
TextView dt, disp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maint);
dt=(TextView)findViewById(R.id.info);
disp=(TextView)findViewById(R.id.textView7);

 sendRequest();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.update);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            sendRequest2();
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
            Update.this.progress.dismiss();
            try {
                Update.this.showJSON1(response);
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
            Update.this.progress.dismiss();
            Toast.makeText(Update.this, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendRequest2() {
        final String un = dt.getText().toString().trim();
        if (un.equals("")) {
            Toast.makeText(this, "Fill all the fields!", Toast.LENGTH_SHORT).show();

        } else {


            this.progress = ProgressDialog.show(this, "Updating", "Please Wait...", true);
            Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL2, new C03971(), new C03982()) {
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
        if (!json.equals("nun")) {
          //  Toast.makeText(this, json, Toast.LENGTH_LONG).show();
            disp.setText(json);
            dt.setText(json);
        } else {
            Toast.makeText(this, "Invalid Details!", Toast.LENGTH_SHORT).show();
        }


        String regEx =
                "^(([w-]+.)+[w-]+|([a-zA-Z]{1}|[w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9]).([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9]).([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[w-]+.)+[a-zA-Z]{2,4})$";

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
                Update.this.progress.dismiss();
                try {
                    Update.this.showJSON(response);
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
                Update.this.progress.dismiss();
                Toast.makeText(Update.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void sendRequest() {

        final String strEmailAddress = "denonkangi@gmail.com";

        if (strEmailAddress.equals("")) {
            Toast.makeText(this, "Fill all the fields!", Toast.LENGTH_SHORT).show();

        } else {


                this.progress = ProgressDialog.show(this, "Loading", "Please Wait...", true);
                Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new C03971(), new C03982()) {
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap();
                        params.put("phone", strEmailAddress);
                        return params;
                    }
                });

        }
    }




    private void showJSON(String json) throws JSONException, InterruptedException {

        Toast.makeText(this, json, Toast.LENGTH_SHORT).show();


    }


}
