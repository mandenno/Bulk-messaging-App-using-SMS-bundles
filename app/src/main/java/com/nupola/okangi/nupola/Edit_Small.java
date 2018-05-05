package com.nupola.okangi.nupola;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Edit_Small extends AppCompatActivity implements View.OnClickListener {
EditText ph,nam;
Toolbar ct;
Button save;
    public static final String JSON_URL_UPDATE = "https://nupola.com/app/saveDetails_small.php";
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editsmall);
        String member = Edit_Small.this.getIntent().getExtras().getString("member");
        String phone = Edit_Small.this.getIntent().getExtras().getString("phone");
        String gname = Edit_Small.this.getIntent().getExtras().getString("gname");
        ct=(Toolbar)findViewById(R.id.ctbarxeditsmall);
        ct.setTitle(member+" in "+gname);
        ct.setTitleTextColor(Color.WHITE);

        nam=(EditText)findViewById(R.id.mnamesmall);
        ph=(EditText)findViewById(R.id.mphonesmall);
        nam.setText(member);
        ph.setText(phone);

        save=(Button)findViewById(R.id.savebtnsmall);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==save)
        {
            saveDetails();
        }
    }

    //Save group
    class C02594d implements DialogInterface.OnClickListener {
        C02594d() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C02605d implements DialogInterface.OnClickListener {
        C02605d() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C03971d implements Response.Listener<String> {
        C03971d() {
        }

        public void onResponse(String response) {
            Edit_Small.this.progress.dismiss();

            Edit_Small.this.showJSONd(response);

        }
    }

    class C03982d implements Response.ErrorListener {
        C03982d() {
        }

        public void onErrorResponse(VolleyError error) {

            Toast.makeText(Edit_Small.this,"Network Error!", Toast.LENGTH_SHORT).show();
            progress.dismiss();

        }
    }
    private void saveDetails() {
        String member = Edit_Small.this.getIntent().getExtras().getString("member");
        final String phone = Edit_Small.this.getIntent().getExtras().getString("phone");
        final String gname = Edit_Small.this.getIntent().getExtras().getString("gname");

        this.progress = ProgressDialog.show(this, "Saving Details", "Please Wait...", true);
        Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL_UPDATE, new Edit_Small.C03971d(), new Edit_Small.C03982d()) {
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap();
                params.put("oldphone",phone);
                params.put("newphone",ph.getText().toString());
                params.put("newname",nam.getText().toString());
                params.put("gname",gname);
                return params;
            }
        });

    }
    public void playBeep() {

        try {
            RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(2)).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class C02821d implements DialogInterface.OnClickListener {
        C02821d() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C02832d implements DialogInterface.OnClickListener {
        C02832d() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }



    private void showJSONd(String resp){
        playBeep();
        String repo=resp.toString();
        // Toast.makeText(context, resp, Toast.LENGTH_LONG).show();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Edit_Small.this);
        alertDialogBuilder.setMessage(repo);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
