package com.nupola.okangi.nupola;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Chat extends Activity {
    private static final String TAG = "ChatActivity";
    public static final String JSON_URL2 = "http://sav-circuit.com/betips/saveChat.php";
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    ProgressBar progressBar;
    TextView pr,name,time;
    private boolean side = false;
    public static final String JSON_URL= "http://sav-circuit.com/betips/chats.php";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        buttonSend = (Button) findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);
        requestChat();
pr=(TextView)findViewById(R.id.prog);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);


    }


    class C03971 implements Response.Listener<String> {
        C03971() {
        }

        public void onResponse(String response) {
            Chat.this.showJSON(response);
        }
    }

    class C03983 implements Response.ErrorListener {
        C03983() {
        }

        public void onErrorResponse(VolleyError error) {

            Toast.makeText(Chat.this, "Network Error", Toast.LENGTH_SHORT).show();
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

    class C03972 implements Response.Listener<String> {
        C03972() {
        }

        public void onResponse(String response) {

            try {
                Chat.this.showJSON3(response);
            } catch (JSONException e) {
                e.printStackTrace();

                progressBar.setVisibility(View.GONE);
            } catch (InterruptedException e) {
                e.printStackTrace();

                progressBar.setVisibility(View.GONE);
            }
        }
    }

    class C03982 implements Response.ErrorListener {
        C03982() {
        }

        public void onErrorResponse(VolleyError error) {
           // requestChat();
            progressBar.setVisibility(View.GONE);
            Toast.makeText(Chat.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void requestChat() {


            Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new Chat.C03972(), new Chat.C03983()) {
                protected Map<String, String> getParams() {
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    Map<String, String> params = new HashMap();
                    params.put("userphone", "08989");
                    params.put("time", currentDateTimeString);
                    return params;
                }
            });

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



    private void showJSON3(String json) throws JSONException, InterruptedException {

        progressBar.setVisibility(View.GONE);

        if (!json.equals("nun")) {

            try {
                JSONArray contacts = new JSONArray(json);

                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    String msgo = c.getString("message");

                    String date = c.getString("datesent");
                    String chat=msgo+"\n"+date;

                        chatArrayAdapter.add(new ChatMessage(true, chat));



                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }





    private void savetoDB() {
        String check = chatText.getText().toString();
        if (check.equals("")) {
return;
        } else {
            chatArrayAdapter.add(new ChatMessage(true, chatText.getText().toString()));
            //requestChat();
            pr.setText("Sending...");
            pr.setTextColor(Color.BLACK);
            Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL2, new Chat.C03971(), new Chat.C03982()) {
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap();
                    params.put("info", chatText.getText().toString());
                    return params;
                }
            });
        }
    }

    public void playBeep() {
        try {

            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.facebook_pop);
            mediaPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showJSON(String json) {
        playBeep();
            pr.setText("");
            pr.setTextColor(Color.BLUE);
            progressBar.setVisibility(View.GONE);

            chatText.setText("");


    }
    private void sendChatMessage() {
      savetoDB();
    }
}
