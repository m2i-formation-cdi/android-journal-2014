package fr.m2i.journal2014;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ListActivity;
import android.content.Intent;

public class RechArticle extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rech_article);
	}

	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		//1. Récuperer le nom de l'élément sélectionné - OK -
		//2. Transmettre l'info sélectionnee a l'activity apellee
		//3. On demarre l'activity
		// Recuperation de la selection
		String lsSelection = l.getItemAtPosition(position).toString();
		// Instanciation de l'intention
		Intent intention = new Intent(this, ArticleListe.class);
		//  Transmission du choix a l'activite appelee
		intention.putExtra("cle", lsSelection);
		// Demarrage de l'activite secondaire
		startActivity(intention);
	}

}
