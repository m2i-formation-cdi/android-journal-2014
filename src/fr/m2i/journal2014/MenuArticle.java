package fr.m2i.journal2014;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MenuArticle extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_article);
	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		
		Intent intentionNavigation = null;		
		String message = "Fonction à implementer";
		
	    switch(position){
	    case 0:
	    	// Ajouter
	    	intentionNavigation = new Intent(this,ArticlePlus.class);
	    	break;	
	    case 1:
	    	// Modifier
//	    	intentionNavigation = new Intent(this,Journalistes.class);
	    	break;
	    case 2:
	    	// Supprimer
//	    	intentionNavigation = new Intent(this,Journalistes.class);
	    	break;
	    case 3:
	    	// Valider
//	    	intentionNavigation = new Intent(this,Journalistes.class);
	    	break;
	    case 4:
	    	// Consulter
	    	intentionNavigation = new Intent(this,RechArticle.class);
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
