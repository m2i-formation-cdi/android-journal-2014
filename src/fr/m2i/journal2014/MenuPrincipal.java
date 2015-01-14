package fr.m2i.journal2014;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class MenuPrincipal extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_principal);
	}
	
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		Intent intentionNavigation = null;
	    switch(position){
	    case 0:
	    	// Parametrage de l'application
	    	intentionNavigation = new Intent(this,MenuParametrage.class);
	    	break;	
	    case 1:
	    	// Gestion des journaliste
	    	intentionNavigation = new Intent(this,MenuJournaliste.class);
	    	break;
	    case 2:
	    	// Gestion des articles
	    	intentionNavigation = new Intent(this,MenuArticle.class);
	    	break;
	    case 3:
	    	// Gestion des images
//	    	intentionNavigation = new Intent(this,Rubrique.class);
	    	break;
	    }
		
		
/*		Sauvegarde de l'existant
 * 
	    switch(position){
	    case 0:
	    	intentionNavigation = new Intent(this,Statut.class);
	    	break;	
	    case 1:
	    	intentionNavigation = new Intent(this,Categorie.class);
	    	break;
	    case 3:
	    	intentionNavigation = new Intent(this,Rubrique.class);
	    	break;
	    case 4:
	    	intentionNavigation = new Intent(this,MotCle.class);
	    	break;
	    case 5:
	    	break;
	    case 6:
	    	intentionNavigation = new Intent(this,Journalistes.class);
	    	break;
	    case 7:
	    	intentionNavigation = new Intent(this,RechArticle.class);
	    	break;
	    case 8:
	    	intentionNavigation = new Intent(this,TestDAO.class);
	    	break;
	    case 9:
	    	break;
	    
	    }
*/	    
	    if( intentionNavigation != null ){
	    	startActivity(intentionNavigation);
	    }
	} 
}
