package com.nupola.okangi.nupola;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class PayStart extends Activity {

	private Spinner spinner1, spinner2;
	private Button btnSubmit;
	ProgressDialog progress;
	Context mcontext;
	LoginDataBaseAdapter2 loginDataBaseAdapter;
	LoginDataBaseAdapter loginDataBaseAdapter1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paystart);

		addItemsOnSpinner2();
		addListenerOnButton();
		addListenerOnSpinnerItemSelection();

		loginDataBaseAdapter=new LoginDataBaseAdapter2(this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();

		loginDataBaseAdapter1=new LoginDataBaseAdapter(this);
		loginDataBaseAdapter1=loginDataBaseAdapter1.open();



	}

	//add items into spinner dynamically
	public void addItemsOnSpinner2() {

		List<String> list = new ArrayList<String>();
		list.add("list 1");
		list.add("list 2");
		list.add("list 3");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter);
	}

	public void addListenerOnSpinnerItemSelection(){
		
		spinner1 = (Spinner) findViewById(R.id.spinner1x);
		spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}
	
	//get the selected dropdown list value
	public void addListenerOnButton() {

		spinner1 = (Spinner) findViewById(R.id.spinner1x);

		
		btnSubmit = (Button) findViewById(R.id.btnSubmitx);

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String accountno = loginDataBaseAdapter.getSinlgeEntry1();
				final String email = loginDataBaseAdapter1.getSinlgeEntry1();
				Intent intent = new Intent(PayStart.this, Payment_Pesapal.class);
				intent.putExtra("plan",String.valueOf(spinner1.getSelectedItem()));
				intent.putExtra("accountno",accountno);
				intent.putExtra("email",email);
				startActivity(intent);

				//Toast.makeText(PayStart.this,
					//	"OnClickListener : " +
						//"\nSpinner 1 : " + String.valueOf(spinner1.getSelectedItem()) +
					//	"\nSpinner 2 : " + String.valueOf(spinner2.getSelectedItem()),
					//	Toast.LENGTH_SHORT).show();
			}

		});

	}

}