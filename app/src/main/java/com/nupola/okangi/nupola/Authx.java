package com.nupola.okangi.nupola;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class Authx extends AppCompatActivity {

    String user, pass;
    ProgressDialog progress;
    final Context context = this;
    LoginDataBaseAdapter2 loginDataBaseAdapter;

    public static final String JSON_URL = "http://nupola.com/app/verify_account.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.authx);
        loginDataBaseAdapter=new LoginDataBaseAdapter2(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        sendRequest();
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
           Authx.this.progress.dismiss();
            try {
                Authx.this.showJSON(response);
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
          Authx.this.progress.dismiss();
            Toast.makeText(Authx.this, "Network connection error", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    private void sendRequest() {
       final String code = loginDataBaseAdapter.getSinlgeEntry1();
            this.progress = ProgressDialog.show(this, "Verifying", "Please Wait...", true);
            Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new C03971(), new C03982()) {
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap();
                    params.put("code", code);


                    return params;
                }
            });

    }


    class C02821 implements DialogInterface.OnClickListener {
        C02821() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            finish();
        }
    }

    class C02832 implements DialogInterface.OnClickListener {
        C02832() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            finish();
        }
    }



    private void showJSON(String json) throws JSONException, InterruptedException {
      //  Toast.makeText(getApplicationContext(), "json: "+json, Toast.LENGTH_LONG).show();
        if (!json.equals("nun")) {


            try {
                JSONArray contacts = new JSONArray(json);

                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    String days = c.getString("username");
                    String key = c.getString("key");
                    String response = c.getString("response");
if(response.contains("paid")) {
    final String gname = Authx.this.getIntent().getExtras().getString("gname");
    final String gid = Authx.this.getIntent().getExtras().getString("gid");

    Intent intent = new Intent(Authx.this, MyAccount.class);
    intent.putExtra("gname", gname);
    intent.putExtra("gid", gid);
    intent.putExtra("days", days);
    intent.putExtra("key", key);
    startActivity(intent);
    // Save the Data in Database
    finish();


}
else if(response.contains("pay"))
{
    final String gname = Authx.this.getIntent().getExtras().getString("gname");
    Intent intent = new Intent(Authx.this, MyAccount.class);
    intent.putExtra("gname", gname);
    intent.putExtra("days", days);
    intent.putExtra("key", key);
    startActivity(intent);
    // Save the Data in Database
    finish();
}
else if(response.contains("none"))
{

    finish();
}

                }

            } catch (JSONException e) {
                e.printStackTrace();
            finish();
            }
        } else {
            Toast.makeText(this, "The Details", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        loginDataBaseAdapter.close();
    }
}