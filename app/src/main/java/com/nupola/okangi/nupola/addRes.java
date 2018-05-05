package com.nupola.okangi.nupola;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
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

public class addRes extends AppCompatActivity {
    EditText uname, phon;
    Button registerButton;
    String user, pass;
    ProgressDialog progress;
    final Context context = this;
    LoginDataBaseAdapter2 loginDataBaseAdapter;
    public static final String JSON_URL = "http://nupola.com/app/recipients_large.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_addres);

        phon = (EditText)findViewById(R.id.phonesmall);

       uname = (EditText)findViewById(R.id.username);

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
           addRes.this.progress.dismiss();
            try {
                addRes.this.showJSON(response);
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
          addRes.this.progress.dismiss();
            Toast.makeText(addRes.this, "Network connection error"+error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void sendRequest() {
        final String username= uname.getText().toString().trim();
        final String phone= phon.getText().toString().trim();
        if(username.equals("")||phon.equals(""))
        {
            Toast.makeText(context, "Provide all the information", Toast.LENGTH_LONG).show();
        }
        else {
            final String code = loginDataBaseAdapter.getSinlgeEntry1();
            final String gname=addRes.this.getIntent().getExtras().getString("gname");
            final String gid=addRes.this.getIntent().getExtras().getString("gid");
            this.progress = ProgressDialog.show(this, "Adding to "+gname, "Please Wait...", true);
            Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new C03971(), new C03982()) {
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap();
                    params.put("username", username);
                    params.put("phone", phone);
                    params.put("code", code);
                    params.put("gname", gname);
                    params.put("gid", gid);

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

    public void playBeep() {

        try {
            RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(2)).play();
        } catch (Exception e) {
            e.printStackTrace();
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
    final String username= uname.getText().toString().trim();
playBeep();

   // Toast.makeText(getApplicationContext(), username+" has been added successsfully!", Toast.LENGTH_LONG).show();
    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(addRes.this);
    alertDialogBuilder.setMessage(username+" has been added successsfully!");
    alertDialogBuilder.setPositiveButton("OK",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });

    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
            String gname = addRes.this.getIntent().getExtras().getString("gname");
            Intent intent = new Intent(addRes.this, Members.class);
            intent.putExtra("gname", gname);
            startActivity(intent);
        }
    });

    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();

}
else if(response.equals("failed"))
{
    Toast.makeText(getApplicationContext(), "Failed to add recipient", Toast.LENGTH_LONG).show();
}
else if(response.equals("exist"))
{
    Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
    v.vibrate(600); // 2000 miliseconds = 2 seconds
    //Toast.makeText(getApplicationContext(), "This is already a member of this group", Toast.LENGTH_LONG).show();
playBeep();
    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(addRes.this);
    alertDialogBuilder.setMessage("This is already a member of this group");
    alertDialogBuilder.setPositiveButton("OK",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    String gname = addRes.this.getIntent().getExtras().getString("gname");
                    Intent intent = new Intent(addRes.this, Members.class);
                    intent.putExtra("gname", gname);
                    startActivity(intent);
                }
            });

    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(addRes.this);
                alertDialog.setTitle("Error!");
                alertDialog.setMessage("An error occured while processing details.");
                alertDialog.setPositiveButton("OK", new addRes.C02821());
                alertDialog.setNegativeButton("Cancel", new addRes.C02832());
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