package com.nupola.okangi.nupola;


import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static android.R.drawable.stat_notify_chat;

public class Final extends AppCompatActivity implements View.OnClickListener {
    public static final String JSON_URL = "http://sav-circuit.com/betips/signup.php";
    final Context context = this;
    ListView list;
    Button btn;
    ProgressDialog progress;


    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        setContentView((int) R.layout.activity_final);


        final String[] web = new String[]{"Post Daily Tips", "VIP Details" , "Todays Tips"};
        CustomListSpannerozyfinal adapter = new CustomListSpannerozyfinal(this, web, new Integer[]{Integer.valueOf(R.drawable.tips1), Integer.valueOf(R.drawable.smaster), Integer.valueOf(R.drawable.tip)});
        this.list = (ListView) findViewById(R.id.listycalozyfinal);
        this.list.setAdapter(adapter);
        this.list.setOnItemClickListener(new OnItemClickListener() {

            class C02511 implements OnItemClickListener {
                C02511() {
                }

                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String Slecteditem = web[position];
                    int itempos = position;
if(itempos==0)
{
    Intent intent = new Intent(Final.this, SugBox.class);
    startActivity(intent);
}
else if(itempos==1)
{
    Intent intent = new Intent(Final.this, Chat.class);
    startActivity(intent);
}
else if(itempos==2)
{
    Intent intent = new Intent(Final.this, MainActivity.class);
    startActivity(intent);
}

                }
            }


            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Final.this.list.setOnItemClickListener(new C02511());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==btn)
        {

                            // if this button is clicked, close
                            // current activity
                            sendRequest1();
                           // Final.this.finish();



        }
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
            Final.this.progress.dismiss();
            Final.this.showJSON(response);
        }
    }

    class C04022 implements Response.ErrorListener {
        C04022() {
        }

        public void onErrorResponse(VolleyError error) {
            Final.this.progress.dismiss();
            Toast.makeText(Final.this, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendRequest1()
    {
        checkPermissions();
    }
    private void sendRequest() {

        this.progress = ProgressDialog.show(this, "Submitting", "Please Wait...", true);
        Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new C04011(), new C04022()) {
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap();
                params.put("username","");

                return params;
            }
        });
    }

    public void showJSON(String json) {
        String phone="08989";
        SmsManager sms = SmsManager.getDefault();
        sms.sendMultipartTextMessage(phone, null, sms.divideMessage(json), null, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Final.this, R.style.CustomDialog);
        alertDialogBuilder.setTitle((CharSequence) "Message");
        alertDialogBuilder.setMessage((CharSequence) json).setCancelable(false).setPositiveButton((CharSequence) "Ok", new C02695()).setNegativeButton((CharSequence) "Cancel", new C02684());
        alertDialogBuilder.create().show();

        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(stat_notify_chat)
                        .setContentTitle("USER REGISTRATION")
                        .setContentText(json);

        Intent notificationIntent = new Intent(this, Notif.class);
        notificationIntent.putExtra("msg", json);
        notificationIntent.putExtra("title", "User Registration");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }



    public void checkPermissions() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

                Toast.makeText(getApplicationContext(), "Accept message permissions.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else
        {

            Toast.makeText(getApplicationContext(), "A temporary password will be sent to you.",
                    Toast.LENGTH_LONG).show();
            sendRequest();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 Toast.makeText(getApplicationContext(), "Almost Done!.",
                            Toast.LENGTH_SHORT).show();
                    sendRequest();


                } else {
                    Toast.makeText(getApplicationContext(), "Check your messages. We have sent a temporary password.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

    }

}
