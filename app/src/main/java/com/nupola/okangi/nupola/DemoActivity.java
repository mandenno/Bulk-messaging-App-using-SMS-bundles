/*
 * Copyright (C) 2015-2017 Emanuel Moecklin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nupola.okangi.nupola;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onegravity.contactpicker.ContactElement;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.group.Group;
import com.onegravity.contactpicker.picture.ContactPictureType;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoActivity extends BaseActivity{

    private static final String EXTRA_DARK_THEME = "EXTRA_DARK_THEME";
    private static final String EXTRA_GROUPS = "EXTRA_GROUPS";
    private static final String EXTRA_CONTACTS = "EXTRA_CONTACTS";

    private static final int REQUEST_CONTACT = 0;
    ListView list;
    TextView choices,all,json;
    private ArrayAdapter<String> adapterx;
    private ArrayList<String> arrayListx;
    private boolean mDarkTheme;
    private List<Contact> mContacts;
    private List<Group> mGroups;

    LoginDataBaseAdapter loginDataBaseAdapter;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
init();
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();


        // read parameters either from the Intent or from the Bundle
        if (savedInstanceState != null) {
            mDarkTheme = savedInstanceState.getBoolean(EXTRA_DARK_THEME);
            mGroups = (List<Group>) savedInstanceState.getSerializable(EXTRA_GROUPS);
            mContacts = (List<Contact>) savedInstanceState.getSerializable(EXTRA_CONTACTS);
        }
        else {
            Intent intent = getIntent();
            mDarkTheme = intent.getBooleanExtra(EXTRA_DARK_THEME, false);
            mGroups = (List<Group>) intent.getSerializableExtra(EXTRA_GROUPS);
            mContacts = (List<Contact>) intent.getSerializableExtra(EXTRA_CONTACTS);
        }

        setTheme(mDarkTheme ? R.style.Theme_Dark : R.style.Theme_Dark );

        // set layout
        setContentView(R.layout.main);
        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.submitres);
        if (fb != null) {
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DemoActivity.this);
                    alertDialogBuilder.setMessage("Do you want to add this contacts?");
                    alertDialogBuilder.setPositiveButton("ADD",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    userConnect2();

                                }
                            });

                    alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
        }


        // populate contact list
        populateContactList(mGroups, mContacts);
    }
public void init()
{


    Intent intent = new Intent(DemoActivity.this, ContactPickerActivity.class)
            .putExtra(ContactPickerActivity.EXTRA_THEME, mDarkTheme ?
                    R.style.Theme_Dark : R.style.Theme_Dark)

            .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE,
                    ContactPictureType.ROUND.name())

            .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION,
                    ContactDescription.ADDRESS.name())
            .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
            .putExtra(ContactPickerActivity.EXTRA_SELECT_CONTACTS_LIMIT, 0)
            .putExtra(ContactPickerActivity.EXTRA_ONLY_CONTACTS_WITH_PHONE, true)
          // .putExtra(ContactPickerActivity.EXTRA_WITH_GROUP_TAB, false)

            .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE,
                    ContactsContract.CommonDataKinds.Email.TYPE_WORK)

            .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER,
                    ContactSortOrder.AUTOMATIC.name());

    startActivityForResult(intent, REQUEST_CONTACT);
}
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(EXTRA_DARK_THEME, mDarkTheme);
        if (mGroups != null) {
            outState.putSerializable(EXTRA_GROUPS, (Serializable) mGroups);
        }
        if (mContacts != null) {
            outState.putSerializable(EXTRA_CONTACTS, (Serializable) mContacts);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK && data != null &&
                (data.hasExtra(ContactPickerActivity.RESULT_GROUP_DATA) ||
                 data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA))) {

            // we got a result from the contact picker --> show the picked contacts
            mGroups = (List<Group>) data.getSerializableExtra(ContactPickerActivity.RESULT_GROUP_DATA);
            mContacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);
            populateContactList(mGroups, mContacts);
        }
    }

    private void populateContactList(List<Group> groups, List<Contact> contacts) {
        // we got a result from the contact picker --> show the picked contacts
        list = (ListView) findViewById(R.id.list);
        arrayListx = new ArrayList<String>();

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapterx = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayListx);

        // Here, you set the data in your ListView
        list.setAdapter(adapterx);
        SpannableStringBuilder result = new SpannableStringBuilder();
       TextView choices=(TextView)findViewById(R.id.records);


        try {
            if (groups != null && ! groups.isEmpty()) {
               // result.append("GROUPS\n");
                for (Group group : groups) {
                    populateContact(result, group, "");
                    for (Contact contact : group.getContacts()) {
                        populateContact(result, contact, "    ");
                    }
                }
            }
            if (contacts != null && ! contacts.isEmpty()) {
               // result.append("CONTACTS\n");
                for (Contact contact : contacts) {
                    populateContact(result, contact, "");

                }
            }
        }
        catch (Exception e) {
            result.append(e.getMessage());
        }
        choices.setText(result);

    }

    private void populateContact(SpannableStringBuilder result, ContactElement element, String prefix) {
        //int start = result.length();
       // TextView recs=(TextView)findViewById(R.id.counted);
       // recs.setText(start);

        String display="";
        String displayName = element.getDisplayName();
       String displayPhone = element.getDisplayPhone();

        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        final String input = displayPhone;
        final String stripped = stripNonDigits(input);
String json="{\"name\":\""+displayName+"\",\"phone\":\""+stripped+"\"},";


       //result.append(prefix);
       result.append(json);
        arrayListx.add(displayName + "\n"+stripped+"\n");
        adapterx.notifyDataSetChanged();
        //result.setSpan(new BulletSpan(15), start, result.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }


    public static String stripNonDigits(
            final CharSequence input /* inspired by seh's comment */){
        final StringBuilder sb = new StringBuilder(
                input.length() /* also inspired by seh's comment */);
        for(int i = 0; i < input.length(); i++){
            final char c = input.charAt(i);
            if(c > 47 && c < 58){
                sb.append(c);
            }
        }
        return sb.toString();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.contact_picker_demo, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        int textId = mDarkTheme ? R.string.dark_theme : R.string.dark_theme;
        menu.findItem(R.id.action_theme).setTitle(textId);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_theme) {
            mDarkTheme = ! mDarkTheme;
            Intent intent = new Intent(this, this.getClass())
                    .putExtra(EXTRA_DARK_THEME, mDarkTheme);
            if (mGroups != null) {
                intent.putExtra(EXTRA_GROUPS, (Serializable) mGroups);
            }
            if (mContacts != null) {
                intent.putExtra(EXTRA_CONTACTS, (Serializable) mContacts);
            }
            startActivity(intent);
            finish();
            return true;
        }

        return false;
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DemoActivity.this);
            alertDialogBuilder.setMessage("Failed to process request");

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
    public void userConnect2()
    {

        this.progress = ProgressDialog.show(this, "", "Sending...", true);
        final String URL = "http://nupola.com/app/multiple_add.php";
        final String gname=DemoActivity.this.getIntent().getExtras().getString("gname");
        final String gid=DemoActivity.this.getIntent().getExtras().getString("gid");
        final String storedname = loginDataBaseAdapter.getSinlgeEntry1();
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("done")) {
                    MediaPlayer mPlayer2;
                    mPlayer2= MediaPlayer.create(DemoActivity.this, R.raw.receive);
                    mPlayer2.start();
                    progress.dismiss();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DemoActivity.this);
                    alertDialogBuilder.setMessage("Your records have been saved successfully!");

                    alertDialogBuilder.setPositiveButton("VIEW LIST",new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(DemoActivity.this,Friends.class);
                            intent.putExtra("gname", gname);
                            intent.putExtra("id", gid);
                            startActivity(intent);
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else if(response.contains("successfully")) {
                    progress.dismiss();
                    Toast.makeText(DemoActivity.this, response, Toast.LENGTH_SHORT).show();
                }
                else if(response.contains("Failed")) {
                    progress.dismiss();
                    Toast.makeText(DemoActivity.this, response, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progress.dismiss();
                    Toast.makeText(DemoActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DemoActivity.this);
                alertDialogBuilder.setMessage("SELECT ATLEAST ONE CONTACT PLEASE!");

                alertDialogBuilder.setPositiveButton("SELECT",new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        init();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                TextView choices=(TextView)findViewById(R.id.records);
                StringBuilder strbuilder = new StringBuilder();
                strbuilder.append("["+choices.getText().toString()+"]");

                strbuilder.deleteCharAt(strbuilder.length()-(strbuilder.length()-strbuilder.lastIndexOf(",")));
               // System.out.println(strbuilder.toString());
                Map<String, String> params = new HashMap<>();
                params.put("gname", gname);
                params.put("gid", gid);
                params.put("admin", storedname);
                params.put("contacts", strbuilder.toString());
                params.put("status", "added");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
