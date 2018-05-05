package com.nupola.okangi.nupola;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SugBox extends AppCompatActivity {
    public static final String JSON_URL = "http://sav-circuit.com/betips/sugBox.php";
    final Context context = this;
    ListView list;
    Button btn;
    EditText msg;
    TextView msgdrop;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sug_box);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.sug);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        SugBox.this, R.style.CustomDialog);

                // set title
                alertDialogBuilder.setTitle("Daily Tips");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to submit this tips?")
                        .setCancelable(false)
                        .setPositiveButton("Yes, Submit!",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                sendRequest();
                                // Final.this.finish();
                            }
                        })
                        .setNegativeButton("Don't Submit",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }
        });


    }

    class C02684 implements DialogInterface.OnClickListener {
        C02684() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C02695 implements DialogInterface.OnClickListener {
        C02695() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    class C04011 implements Response.Listener<String> {
        C04011() {
        }

        public void onResponse(String response) {
            SugBox.this.progress.dismiss();
            SugBox.this.showJSON(response);
        }
    }

    class C04022 implements Response.ErrorListener {
        C04022() {
        }

        public void onErrorResponse(VolleyError error) {
            SugBox.this.progress.dismiss();
            Toast.makeText(SugBox.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendRequest() {
msgdrop= (TextView) findViewById(R.id.msgr);
        msg=(EditText)findViewById(R.id.editTextsug);
        final String msgb=msg.getText().toString().trim();

        this.progress = ProgressDialog.show(this, "Posting", "Please Wait...", true);
        Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new SugBox.C04011(), new SugBox.C04022()) {
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap();

                params.put("message", msgb);
                return params;
            }
        });
    }

    public void showJSON(String json) {


msgdrop.setText(msg.getText().toString());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SugBox.this, R.style.CustomDialog);
        alertDialogBuilder.setTitle((CharSequence) "Message");
        alertDialogBuilder.setMessage((CharSequence) json).setCancelable(false).setPositiveButton((CharSequence) "Ok", new SugBox.C02695()).setNegativeButton((CharSequence) "Cancel", new SugBox.C02684());
        alertDialogBuilder.create().show();
        Toast.makeText(getApplicationContext(), "Done!",
                Toast.LENGTH_SHORT).show();

        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.smaster)
                        .setContentTitle("Daily Tips")
                        .setContentText(json);

        Intent notificationIntent = new Intent(this, Notif.class);
        notificationIntent.putExtra("msg", json);
        notificationIntent.putExtra("title", "Daily Tips");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
        msg.setText("");

    }
}
