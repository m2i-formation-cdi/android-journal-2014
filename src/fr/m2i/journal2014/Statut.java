package fr.m2i.journal2014;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Statut extends ListActivity {

	private ListView listViewStatut;
	private Map<String,String> item;
	List<String> lstStatut;
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statut);
		listViewStatut = this.getListView();
		
		
		getData();
		
		ArrayAdapter<String> arrayAdapterStatut;
		arrayAdapterStatut = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lstStatut);
		listViewStatut.setAdapter(arrayAdapterStatut);
		
		
	}
	
	private void getData(){
		InputStream is;
		InputStreamReader isr;
		BufferedReader bfr;
		String ligne;
		
		lstStatut = new ArrayList<String>();
		
		is = this.getBaseContext().getResources().openRawResource(R.raw.statut);
		isr = new InputStreamReader(is);
		bfr = new BufferedReader(isr);
		try {
			while((ligne = bfr.readLine())!= null){
				lstStatut.add(ligne.replaceAll(";", " - "));
			}
			
			bfr.close();
			isr.close();
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
