package com.nupola.okangi.nupola;

/**
 * Created by Okangi on 10/25/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Bulktype extends Activity {
Toolbar ct;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.bulktype);
        MobileAds.initialize(this,
                "ca-app-pub-3246984456778707/6261148414");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
ct=(Toolbar)findViewById(R.id.ctbarx);
        ct.setTitle("SMS Type");
        ct.setTitleTextColor(Color.WHITE);
        // Get reference of widgets from XML layout
        final ListView lv = (ListView) findViewById(R.id.lvx);

        // Initializing a new String Array
        String[] fruits = new String[] {
                "\nBulk SMS for smaller groups\n (1-100) recipients.\n\n",
                "\nBulk SMS for larger groups\n(more than 100 recipients).\n\n"
        };

        // Create a List from String Array elements
        final List<String> fruits_list = new ArrayList<String>(Arrays.asList(fruits));

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, fruits_list);

        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Animate the background color of clicked Item
                ColorDrawable[] color = {
                        new ColorDrawable(Color.parseColor("#E0F7FA")),
                        new ColorDrawable(Color.parseColor("#B2EBF2"))
                };
                TransitionDrawable trans = new TransitionDrawable(color);
                view.setBackground(trans);
                trans.startTransition(2000); // duration 2 seconds

                // Go back to the default background color of Item
                ColorDrawable[] color2 = {
                        new ColorDrawable(Color.parseColor("#B2EBF2")),
                        new ColorDrawable(Color.parseColor("#E0F7FA"))
                };
                TransitionDrawable trans2 = new TransitionDrawable(color2);
                view.setBackground(trans2);
                trans2.startTransition(2000); // duration 2 seconds

                String Slecteditem = fruits_list.get(position);
               // Toast.makeText(Counties.this, "YOUR BUSINESS PREMISE IS IN "+Slecteditem+" COUNTY", Toast.LENGTH_LONG).show();
              if(position==0)
              {
                  Intent intent = new Intent(Bulktype.this, MainActivity.class);
                  startActivity(intent);
              }
              else if(position==1)
              {
                  Intent intent = new Intent(Bulktype.this, MyActivity1.class);
                  intent.putExtra("type","large_group");
                  startActivity(intent);
              }

            }
        });
    }

}