package fr.m2i.journal2014;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MenuPrincipal extends ListActivity {

	private Intent intentionNavigation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_principal);
	}
	
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
	    switch(position){
	    case 5:
	    	intentionNavigation = new Intent(this,Journalistes.class);
	    	break;
	    }
	    
	    startActivity(intentionNavigation);
	} 
}
