package fr.m2i.journal2014;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MenuJournaliste extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_journaliste);
	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		
		Intent intentionNavigation = null;		
		String message = "Fonction à implementer";
		
	    switch(position){
	    case 0:
	    	// Mise a jour ,suppression et ajout
	    	intentionNavigation = new Intent(this,Journalistes.class);
	    	break;	
	    case 1:
	    	// Ajout
//	    	intentionNavigation = new Intent(this,Journalistes.class);
	    	break;
	    }
	    // Lancement de l'intention si definie
	    if( intentionNavigation != null ){
	    	startActivity(intentionNavigation);
	    }else{
	    	Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
	    }
	}

}
