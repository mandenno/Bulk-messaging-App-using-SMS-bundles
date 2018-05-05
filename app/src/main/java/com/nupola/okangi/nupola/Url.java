package com.nupola.okangi.nupola;

/**
 * Created by Okangi on 10/26/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Url extends Activity {

    FloatingActionButton sendBtn;

    EditText txtMessage;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.url);

        sendBtn = (FloatingActionButton) findViewById(R.id.btnSendSMS);

        txtMessage = (EditText) findViewById(R.id.editText2);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSMSMessage();
            }
        });
    }

    protected void sendSMSMessage() {
      String link = txtMessage.getText().toString();
        Intent i = new Intent(Url.this, WebViewClientDemoActivity.class);
        i.putExtra("link",link);
        startActivity(i);
        Toast.makeText(Url.this, "Preparing "+link, Toast.LENGTH_SHORT).show();
    }

}
