package com.nupola.okangi.nupola;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Friends extends AppCompatActivity implements ContactsAdapterFriends.ContactsAdapterListener, View.OnClickListener {
    // private static final String TAG = Friends.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Contactfriends> contactList;
    private ContactsAdapterFriends mAdapter;
    private SearchView searchView;
    Context mcontext;
    LoginDataBaseAdapter loginDataBaseAdapter;
    ProgressDialog progress;
    Utils utils;
    private AdView mAdView;
    FloatingActionButton smsbtn;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE};

    // url to fetch contacts json
    private static final int RESULT_PICK_CONTACT = 85500;
    private static final String URLTWOM = "http://api.androidhive.info/json/contacts.json";
    private static final String URLTWO = "http://nupola.com/app/members_smaller.php?gid=";
    public static final String CON_URL = "http://nupola.com/app/add_member.php";
    public static final String JSON_URL = "http://nupola.com/app/recipients.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = findViewById(R.id.toolbarfriend);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        MobileAds.initialize(this,
                "ca-app-pub-3246984456778707/6261148414");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        smsbtn = (FloatingActionButton) findViewById(R.id.sms);
        smsbtn.setOnClickListener(this);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


       final String gname = Friends.this.getIntent().getExtras().getString("gname");
       final String id = Friends.this.getIntent().getExtras().getString("id");
        getSupportActionBar().setTitle(gname + " members");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Friends.this, manage.class);
                intent.putExtra("gname", gname);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        });

        mcontext = getApplicationContext();
        utils = new Utils(mcontext);
        recyclerView = findViewById(R.id.recycler_view_friends);
        contactList = new ArrayList<>();
        mAdapter = new ContactsAdapterFriends(this, contactList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecorationFriends(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
// add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        fetchContactsf();

    }
    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }



    private void userConnect(final String mname, final String mphone) {

        this.progress = ProgressDialog.show(this, "Adding", "Please Wait...", true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {

                            showJSON3(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Friends.this, error.toString(), Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                final String gname = Friends.this.getIntent().getExtras().getString("gname");
                final String gid = Friends.this.getIntent().getExtras().getString("id");
                String storedname = loginDataBaseAdapter.getSinlgeEntry1();
                Map<String, String> params = new HashMap<String, String>();
                params.put("gname", gname);
                params.put("gid", gid);
                params.put("admin", storedname);
                params.put("mname", mname);
                params.put("status", "added");
                params.put("mphone", mphone);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void playBeep() {

        try {
            RingtoneManager.getRingtone(mcontext, RingtoneManager.getDefaultUri(2)).play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showJSON3(String json) throws JSONException, InterruptedException {
        if (json.equals("true\n")) {
            playBeep();
            progress.dismiss();
            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(600); // 2000 miliseconds = 2 seconds
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Your friend has been added successfully!");

            alertDialogBuilder.setPositiveButton("OK, Thanks", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(Friends.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            //Toast.makeText(this, "Friend added successfully!", Toast.LENGTH_SHORT).show();

        } else if (json.equals("already\n")) {
            progress.dismiss();

            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(600); // 2000 miliseconds = 2 seconds
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("This member is already in your friends list!");

            alertDialogBuilder.setPositiveButton("OK, Thanks", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            //  Toast.makeText(this, "This member is already in your friends list!", Toast.LENGTH_SHORT).show();
        } else if (json.equals("failed\n")) {
            progress.dismiss();


            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(600); // 2000 miliseconds = 2 seconds

            Toast.makeText(this, "Failed to connect to friend!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        final String gname = Friends.this.getIntent().getExtras().getString("gname");
        if (view == smsbtn) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Friends.this);
            alertDialogBuilder.setMessage("Load members contact numbers for "+ gname+" group");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            sendsms(gname);

                        }
                    });

            alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Friends.this);
            alertDialogBuilder.setMessage("Check your network connection and try again!");

            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

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

    public void userConnect2(final String finalName, final String finalPhoneNo) {
        final String URL = "http://nupola.com/app/add_member.php";
        final String gname = Friends.this.getIntent().getExtras().getString("gname");
        final String gid = Friends.this.getIntent().getExtras().getString("id");
        final String storedname = loginDataBaseAdapter.getSinlgeEntry1();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray contacts = new JSONArray(response);

                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String userData = c.getString("username");
                        String resp = c.getString("response");
                        if (resp.equals("success")) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Friends.this);
                            alertDialogBuilder.setMessage("Member added successfully");
                            alertDialogBuilder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            finish();
                                        }
                                    });

                            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else if (resp.equals("exist")) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Friends.this);
                            alertDialogBuilder.setMessage("Member already in your messaging list!");
                            alertDialogBuilder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            finish();
                                        }
                                    });

                            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else if (resp.equals("failed")) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Friends.this);
                            alertDialogBuilder.setMessage("Failed to add member to your group!!");
                            alertDialogBuilder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            finish();
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

                } catch (JSONException e) {
                    e.printStackTrace();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Friends.this);
                    alertDialog.setTitle("Sorry");
                    alertDialog.setMessage("We have encountered an internal error while processing your request!");
                    alertDialog.setPositiveButton("OK", new Friends.C02821());
                    alertDialog.setNegativeButton("Cancel", new Friends.C02832());
                    alertDialog.setIcon(R.mipmap.ic_launcher).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
              progress.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("gname", gname);
                params.put("gid", gid);
                params.put("admin", storedname);
                params.put("mname", finalName);
                params.put("status", "added");
                params.put("mphone", finalPhoneNo);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    /**
     * fetches json by making http calls
     */
    private void fetchContactsf() {
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressf);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        String gid = Friends.this.getIntent().getExtras().getString("id");
        String gname = Friends.this.getIntent().getExtras().getString("gname");
        JsonArrayRequest request = new JsonArrayRequest(URLTWO + gid,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressf);
                            linlaHeaderProgress.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Contactfriends> items = new Gson().fromJson(response.toString(), new TypeToken<List<Contactfriends>>() {
                        }.getType());
                        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressf);
                        linlaHeaderProgress.setVisibility(View.GONE);
                        // adding contacts to contacts list
                        contactList.clear();
                        contactList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                //  Log.e(TAG, "Error: " + error.getMessage());
                LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressf);
                linlaHeaderProgress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error!", Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friends, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search_friends)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search_friends) {
            return true;
        } else if (id == R.id.action_add) {
            final CharSequence[] items = {
                    "Pick from contacts","Add manually","Import from Excel Sheet"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Recipients");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    if(item==0)
                    {
                        String gname = Friends.this.getIntent().getExtras().getString("gname");
                        if(gname.contains("Inherited"))
                        {
                            Toast.makeText(Friends.this, "This group is inherited! You can only send messages to members.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            String gid = Friends.this.getIntent().getExtras().getString("id");
                            Intent intent = new Intent(Friends.this, DemoActivity.class);
                            intent.putExtra("gname", gname);
                            intent.putExtra("gid", gid);
                            startActivity(intent);
                        }

                    }
                    else if(item==1)
                    {
                       final String gname = Friends.this.getIntent().getExtras().getString("gname");
                        if(gname.contains("Inherited"))
                        {
                            Toast.makeText(Friends.this, "This group is inherited! You can only send messages to members.", Toast.LENGTH_LONG).show();
                        }
                        else {

                            String gid = Friends.this.getIntent().getExtras().getString("id");
                            Intent intent = new Intent(Friends.this, addRes_Small.class);
                            intent.putExtra("gname", gname);
                            intent.putExtra("gid", gid);
                            startActivity(intent);
                        }

                    }
                    else if(item==2)
                    {
                        final String gname = Friends.this.getIntent().getExtras().getString("gname");
                        if(gname.contains("Inherited"))
                        {
                            Toast.makeText(Friends.this, "This group is inherited! You can only send messages to members.", Toast.LENGTH_LONG).show();
                        }
                        else {

                            String gid = Friends.this.getIntent().getExtras().getString("id");
                            Intent intent = new Intent(Friends.this, Manual.class);
                            intent.putExtra("gname", gname);
                            intent.putExtra("gid", gid);
                            startActivity(intent);
                        }

                    }

                }
            });
            AlertDialog alert = builder.create();
            alert.show();

            //pickContact();
        } else if (id == R.id.action_share) {
            final String gid = Friends.this.getIntent().getExtras().getString("id");
            final String gname = Friends.this.getIntent().getExtras().getString("gname");
            if(gname.contains("Inherited"))
            {
                Toast.makeText(Friends.this, "This group is inherited! You can only send messages to members.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(mcontext, "Opening sharing Apps", Toast.LENGTH_SHORT).show();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hi,\n Kindly join my Bulk SMS group (" + gname + ") on nupola App by following the link below.\n http://nupola.com/app/session.php?gid=" + gid + "\n\nGet the app by clicking the link below \nhttp://play.google.com/store/apps/details?id=com.nupola.okangi.nupola");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Invite Members to " + gname);
                startActivity(Intent.createChooser(shareIntent, "Invite Members to join " + gname));
            }
        }
        else if (id == R.id.action_refreshf) {

            Toast.makeText(this, "Refreshing contacts...", Toast.LENGTH_SHORT).show();
          fetchContactsf();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    public void pickContact() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }

        } else {
            Log.e("Friends", "Failed to pick contact");
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            // Set the value to the textviews
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Add " + name + " to this group?");
            final String finalName = name;
            final String finalPhoneNo = phoneNo;
            alertDialogBuilder.setPositiveButton("YES, ADD",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Toast.makeText(Friends.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                            userConnect2(finalName, finalPhoneNo);
                        }
                    });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onContactSelected(final Contactfriends contactdisp) {
        if(contactdisp.getPhone().equals("0-members-found!"))
        {
            Toast.makeText(mcontext, "Invite members to join your group!", Toast.LENGTH_LONG).show();
        }
        else {

            final String gname = Friends.this.getIntent().getExtras().getString("gname");
            if (gname.contains("Inherited")) {
                Toast.makeText(Friends.this, "This group is inherited! You can only send messages to members.", Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("You selected " + contactdisp.getName() + ". Click on settings for more options");
                alertDialogBuilder.setPositiveButton("SETTINGS",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                String gname = Friends.this.getIntent().getExtras().getString("gname");
                                String gid = Friends.this.getIntent().getExtras().getString("id");
                                Intent intent = new Intent(Friends.this, settings_small.class);
                                intent.putExtra("gname", gname);
                                intent.putExtra("gid", gid);
                                intent.putExtra("phone", contactdisp.getPhone());
                                intent.putExtra("member", contactdisp.getName());
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

    class C039714 implements Response.Listener<String> {
        C039714() {
        }

        public void onResponse(String response) {
            Friends.this.progress.dismiss();

                Friends.this.showJSONx(response);

        }
    }

    class C039824 implements Response.ErrorListener {
        C039824() {
        }

        public void onErrorResponse(VolleyError error) {

            Toast.makeText(Friends.this,"Network Error!", Toast.LENGTH_SHORT).show();
            progress.dismiss();

        }
    }
    private void sendsms(final String id) {
        final String gname = Friends.this.getIntent().getExtras().getString("gname");
        final String gid = Friends.this.getIntent().getExtras().getString("id");

        this.progress = ProgressDialog.show(this, "Fetching Contacts", "Please Wait...", true);
        Volley.newRequestQueue(this).add(new StringRequest(1, JSON_URL, new Friends.C039714(), new Friends.C039824()) {
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap();
                params.put("gname", gname);
                params.put("gid", gid);
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



    private void showJSONx(String recipients) {
        if (recipients.contains("no-recs")) {
            Toast.makeText(this, "This group does not have any member!", Toast.LENGTH_LONG).show();
        } else {
            playBeep();
            Toast.makeText(this, "Opening SMS platform", Toast.LENGTH_LONG).show();
            Uri sms_uri = Uri.parse("smsto:" + recipients);
            Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
            sms_intent.putExtra("sms_body", "");
            startActivity(sms_intent);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    }



