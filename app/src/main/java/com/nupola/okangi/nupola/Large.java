package com.nupola.okangi.nupola;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Large extends AppCompatActivity implements ContactsAdapterLarge.ContactsAdapterListener {
    private static final String TAG = Large.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Contactlarge> contactList;
    private ContactsAdapterLarge mAdapter;
    private SearchView searchView;
    ProgressDialog progress;
    Context mcontext;
    LoginDataBaseAdapter2 loginDataBaseAdapter;
    // CAST THE LINEARLAYOUT HOLDING THE MAIN PROGRESS (SPINNER)


    // url to fetch contacts json
    private static final String URL = "http://nupola.com/app/groups_large.php?code=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large);
        Toolbar toolbar = findViewById(R.id.toolbarlarge);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.toolbar_title_large);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Landing.class));
            }
        });
        loginDataBaseAdapter=new LoginDataBaseAdapter2(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        recyclerView = findViewById(R.id.recycler_viewlarge);
        contactList = new ArrayList<>();
        mAdapter = new ContactsAdapterLarge(this, contactList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecorationLarge(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        fetchContacts();
    }


    public void playBeep() {

        try {
            RingtoneManager.getRingtone(mcontext, RingtoneManager.getDefaultUri(2)).play();
        } catch (Exception e) {
            e.printStackTrace();
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Large.this);
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
    /**
     * fetches json by making http calls
     */
    private void fetchContacts() {
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressl);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        final String storedname = loginDataBaseAdapter.getSinlgeEntry1();
        JsonArrayRequest request = new JsonArrayRequest(URL+storedname,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressl);
                            linlaHeaderProgress.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Couldn't fetch list! Please try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Contactlarge> items = new Gson().fromJson(response.toString(), new TypeToken<List<Contactlarge>>() {
                        }.getType());
                        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressl);
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
                Log.e(TAG, "Error: " + error.getMessage());
                LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressl);
                linlaHeaderProgress.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network Error!" , Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    private void fetchContactsOld() {
        Intent intent = new Intent(Large.this, grouplarge.class);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_large, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search_large)
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
        if (id == R.id.action_search_large) {
            return true;
        }
        else if (id == R.id.action_old_large) {
            fetchContactsOld();
        }

        else if (id == R.id.action_refresh_large) {
            fetchContacts();
            Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onContactSelected(Contactlarge contact) {


       nextPage(contact.getName(),contact.getPhone());

    }


    public void nextPage(String name, String phone)
    {
if(phone.equals("new-group"))
{
    Toast.makeText(this, "You dont have any groups yet! Please create first!", Toast.LENGTH_LONG).show();
}
else {
    Intent intent = new Intent(Large.this, Members.class);
    intent.putExtra("gname", name);
    intent.putExtra("id", phone);
    startActivity(intent);
}
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
