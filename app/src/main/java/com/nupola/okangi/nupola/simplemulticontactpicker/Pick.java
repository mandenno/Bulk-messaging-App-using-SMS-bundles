package com.nupola.okangi.nupola.simplemulticontactpicker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nupola.okangi.nupola.Contacts;
import com.nupola.okangi.nupola.Friends;
import com.nupola.okangi.nupola.LoginDataBaseAdapter;
import com.nupola.okangi.nupola.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pick extends AppCompatActivity {

    TextView contactsDisplay;
    ListView list;
    ProgressDialog progress;
    LoginDataBaseAdapter loginDataBaseAdapter;
    FloatingActionButton pickContacts;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    final int CONTACT_PICK_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
init();
        contactsDisplay=(TextView)findViewById(R.id.selectedConts);
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        list = (ListView) findViewById(R.id.list);
        arrayList = new ArrayList<String>();

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);

        // Here, you set the data in your ListView
        list.setAdapter(adapter);

        pickContacts = (FloatingActionButton) findViewById(R.id.btn_pick_contacts);

        pickContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = contactsDisplay.getText().toString();
                if (check.equals("")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Pick.this);
                    alertDialogBuilder.setMessage("You have not selected any member!");

                    alertDialogBuilder.setPositiveButton("ADD NOW",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intentContactPick = new Intent(Pick.this,ContactsPickerActivity.class);
                                    Pick.this.startActivityForResult(intentContactPick,CONTACT_PICK_REQUEST);
                                }
                            });

                    alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    final String gname=Pick.this.getIntent().getExtras().getString("gname");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Pick.this);
                    alertDialogBuilder.setMessage("Do you want to add this recipients to "+gname+" group?");

                    alertDialogBuilder.setPositiveButton("ADD",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //Toast.makeText(Friends.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                                    userConnect2(contactsDisplay.getText().toString());
                                    progress = ProgressDialog.show(Pick.this, "Adding", "Please Wait...(Processing)", true);

                                }
                            });

                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            }
        });
    }
    public void init()
    {
        Intent intentContactPick = new Intent(Pick.this,ContactsPickerActivity.class);
        Pick.this.startActivityForResult(intentContactPick,CONTACT_PICK_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CONTACT_PICK_REQUEST && resultCode == RESULT_OK){

            ArrayList<Contact> selectedContacts = data.getParcelableArrayListExtra("SelectedContacts");

            String display="";
            String display2="";
            for(int i=0;i<selectedContacts.size();i++){

                display += selectedContacts.get(i).toString2()+ ((i==selectedContacts.size()-1) ? "" :",");
                //display2 = (i+1)+". "+selectedContacts.get(i).toString();
                contactsDisplay.setText(display);
                // this line adds the data of your EditText and puts in your array
                arrayList.add("\n\n"+selectedContacts.get(i).toString()+"\n\n");
            }

            // next thing you have to do is check if your adapter has changed
            adapter.notifyDataSetChanged();



        }

    }
    class C03971 implements Response.Listener<String> {
        C03971() {
        }

        public void onResponse(String response) {
            progress.dismiss();
            showJSON(response);
        }
    }

    class C03982 implements Response.ErrorListener {
        C03982() {
        }

        public void onErrorResponse(VolleyError error) {
            progress.dismiss();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Pick.this);
            alertDialogBuilder.setMessage("Check your network connection and try again!");

            alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void showJSON(String json) {

        Toast.makeText(this, json, Toast.LENGTH_LONG).show();

    }
    public void userConnect2(final String members)
    {
        final String URL = "http://nupola.com/app/add_member_multiple.php";
        final String gname=Pick.this.getIntent().getExtras().getString("gname");
        final String gid=Pick.this.getIntent().getExtras().getString("gid");
        final String storedname = loginDataBaseAdapter.getSinlgeEntry1();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("success")) {
                    progress.dismiss();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Pick.this);
                    alertDialogBuilder.setMessage("Added successfully!");

                    alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Pick.this,Friends.class);
                            intent.putExtra("gname", gname);
                            intent.putExtra("id", gid);
                            startActivity(intent);
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else if(response.contains("exist")) {
                    progress.dismiss();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Pick.this);
                    alertDialogBuilder.setMessage("Member already exist in your messaging list!");

                    alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Pick.this,Friends.class);
                            intent.putExtra("gname", gname);
                            intent.putExtra("id", gid);
                            startActivity(intent);
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else if(response.contains("failed")) {
                    progress.dismiss();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Pick.this);
                    alertDialogBuilder.setMessage("Failed to add member to group!");

                    alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Pick.this,Friends.class);
                            intent.putExtra("gname", gname);
                            intent.putExtra("id", gid);
                            startActivity(intent);
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Pick.this);
                alertDialogBuilder.setMessage("Failed to add member!"+error.getMessage());

                alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("gname", gname);
                params.put("gid", gid);
                params.put("admin", storedname);
                params.put("members", members);
                params.put("status", "added");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }



}
