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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Plan extends Activity {
Toolbar ct;
EditText phone;
    LoginDataBaseAdapter2 loginDataBaseAdapter;
    LoginDataBaseAdapter loginDataBaseAdapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.plan);
        phone=(EditText)findViewById(R.id.infox);

ct=(Toolbar)findViewById(R.id.ctbarxp);
        ct.setTitle("Payment Plan");
        ct.setTitleTextColor(Color.WHITE);
        // Get reference of widgets from XML layout
        final ListView lv = (ListView) findViewById(R.id.lvxp);

        // Initializing a new String Array
        String[] fruits = new String[] {
                "\nWeekly plan (valid for 7 days)",
                "\nMonthly plan (valid for 30 days)\n\n"
        };

        loginDataBaseAdapter=new LoginDataBaseAdapter2(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        loginDataBaseAdapter1=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter1=loginDataBaseAdapter1.open();

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
                  String phoneno=phone.getText().toString();
                  if(phoneno.equals(""))
                  {
                      Toast.makeText(Plan.this, "Enter valid phone number!", Toast.LENGTH_LONG).show();
                  }
                  else {
                      final String accountno = loginDataBaseAdapter.getSinlgeEntry1();
                      final String email = loginDataBaseAdapter1.getSinlgeEntry1();
                      Intent intent = new Intent(Plan.this, Payment_Pesapal.class);
                      intent.putExtra("plan", "Weekly");
                      intent.putExtra("accountno", accountno);
                      intent.putExtra("email", email);
                      intent.putExtra("phone", phoneno);
                      startActivity(intent);

                      finish();
                  }

              }
              else if(position==1) {
                  String phoneno = phone.getText().toString();
                  if (phoneno.equals("")) {
                      Toast.makeText(Plan.this, "Enter valid phone number!", Toast.LENGTH_LONG).show();
                  } else {
                      final String accountno = loginDataBaseAdapter.getSinlgeEntry1();
                      final String email = loginDataBaseAdapter1.getSinlgeEntry1();
                      Intent intent = new Intent(Plan.this, Payment_Pesapal.class);
                      intent.putExtra("plan", "Monthly");
                      intent.putExtra("accountno", accountno);
                      intent.putExtra("email", email);
                      startActivity(intent);

                      finish();
                  }
              }

            }
        });
    }
}