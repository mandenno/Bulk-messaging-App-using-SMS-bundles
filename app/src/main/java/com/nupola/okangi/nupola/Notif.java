package com.nupola.okangi.nupola;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Notif extends AppCompatActivity {
TextView title,msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);
        title=(TextView)findViewById(R.id.title);
        msg=(TextView)findViewById(R.id.msg);
        String tit=Notif.this.getIntent().getExtras().getString("title");
        String ms=Notif.this.getIntent().getExtras().getString("msg");
       title.setText(tit);
       msg.setText(ms);
    }
}
