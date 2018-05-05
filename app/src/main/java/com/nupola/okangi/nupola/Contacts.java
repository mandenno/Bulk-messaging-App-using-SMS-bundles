package com.nupola.okangi.nupola;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Contacts extends Activity{

	LoginDataBaseAdapter loginDataBaseAdapter;
	ProgressDialog progress;

	// url to fetch contacts json
	private static final int RESULT_PICK_CONTACT = 85500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		loginDataBaseAdapter=new LoginDataBaseAdapter(this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();

		pickContact();
	}
	
	public void pickContact2(View view)
	{
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
	    startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
	}

	public void pickContact()
	{
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
        	Log.e("Contacts", "Failed to pick contact");
        }
    }

	private void contactPicked(Intent data) {
		Cursor cursor = null;
        try {
        	String phoneNo = null ;
        	String name = null;
        	Uri uri = data.getData();
        	cursor = getContentResolver().query(uri, null, null, null, null);
        	cursor.moveToFirst();

        	int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        	int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        	
        	phoneNo = cursor.getString(phoneIndex);
        	name = cursor.getString(nameIndex);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setMessage("Add "+name+" to this group?");
			final String finalName = name;
			final String finalPhoneNo = phoneNo;
			alertDialogBuilder.setPositiveButton("YES, ADD",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							//Toast.makeText(Friends.this,"You clicked yes button",Toast.LENGTH_LONG).show();
							userConnect2(finalName, finalPhoneNo);
							progress = ProgressDialog.show(Contacts.this, "Adding", "Please Wait...(Processing)", true);

						}
					});

			alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

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
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Contacts.this);
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
	public void userConnect2(final String finalName, final String finalPhoneNo)
	{
		final String URL = "http://nupola.com/app/add_member.php";
		final String gname=Contacts.this.getIntent().getExtras().getString("gname");
		final String gid=Contacts.this.getIntent().getExtras().getString("gid");
		final String storedname = loginDataBaseAdapter.getSinlgeEntry1();
		StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				if(response.contains("success")) {
					progress.dismiss();
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Contacts.this);
					alertDialogBuilder.setMessage("Added successfully!");

					alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Contacts.this,Friends.class);
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
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Contacts.this);
					alertDialogBuilder.setMessage("Member already exist in your messaging list!");

					alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Contacts.this,Friends.class);
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
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Contacts.this);
					alertDialogBuilder.setMessage("Failed to add member to group!");

					alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Contacts.this,Friends.class);
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
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Contacts.this);
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
				params.put("mname", finalName);
				params.put("status", "added");
				params.put("mphone", finalPhoneNo);

				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);


	}

}
