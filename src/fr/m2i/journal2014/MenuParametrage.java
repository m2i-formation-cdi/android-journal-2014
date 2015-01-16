package fr.m2i.journal2014;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MenuParametrage extends ListActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_parametrage);
	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		
	Intent intentionNavigation = null;		
		
	String message = "Fonction à implementer";

	    switch(position){
	    case 0:
	    	// Gestion des statuts
	    	intentionNavigation = new Intent(this,Statut.class);
	    	break;	
	    case 1:
	    	// Gestion des categories
	    	intentionNavigation = new Intent(this,Categorie.class);
	    	break;
	    case 2:
	    	// civilités
//	    	intentionNavigation = new Intent(this,Categorie.class);
	    	break;
	    case 3:
	    	// Gestion des rubriques
//	    	intentionNavigation = new Intent(this,Rubrique.class);
	    	/*
	    	 * Classe rubrique a revoir
	    	 */
	    	break;
	    case 4:
	    	// Gestion des mots cl
	    	intentionNavigation = new Intent(this,MotCle.class);
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
