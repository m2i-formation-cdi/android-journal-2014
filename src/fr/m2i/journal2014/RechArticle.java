package fr.m2i.journal2014;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import fr.m2i.journal2014.models.DbConnexion;

public class RechArticle extends ListActivity implements OnClickListener {
	
	// Bouton de synchro
//	private Button buttonSynchroRechArticle;
	// Tag pour le log
	private static final String TAG_APPLI = "Journal2014";
	// Contenu du fichier
	private String contenuFichier;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rech_article);
		
/*		// Liaison avec le bouton de synchro
		buttonSynchroRechArticle = (Button) findViewById(R.id.buttonSynchroRechArticle);
		// Attachement au listener
		buttonSynchroRechArticle.setOnClickListener(this);
*/	}

	
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


	@Override
	public void onClick(View v) {
		// Nom de fichier
		String nomFichier;
		// Requete d'extraction des donnees
		String monSql;
		// Tableau de parametres
		String[] param = {"",""};
		/*
		 *  Mise à jour des categories
		 */
/*		nomFichier = "categorie.txt";
		monSql = "SELECT categorie FROM categorie ORDER BY categorie";
		//
		param[0] = nomFichier;
		param[1] = monSql;
		try{
			new ChargeListeArticle().execute(param);
		}catch(Exception e){
			Log.e(TAG_APPLI, e.getMessage());
		}
*/		/*
		 *  Fin synchro categorie
		 */
		/*
		 *  Mise à jour des mots cles
		 */
/*		nomFichier = "mot_cle.txt";
		monSql = "SELECT mot_cle FROM mot_cle ORDER BY mot_cle";
		//
		param[0] = nomFichier;
		param[1] = monSql;
		try{
			new ChargeListeArticle().execute(param);
		}catch(Exception e){
			Log.e(TAG_APPLI, e.getMessage());
		}
*/		/*
		 *  Fin synchro mot_cle
		 */
		/*
		 *  Mise à jour des rubriques
		 */
/*		nomFichier = "rubrique.txt";
		monSql = "SELECT rubrique FROM rubrique ORDER BY rubrique";
		//
		param[0] = nomFichier;
		param[1] = monSql;
		try{
			new ChargeListeArticle().execute(param);
		}catch(Exception e){
			Log.e(TAG_APPLI, e.getMessage());
		}
*/		/*
		 *  Fin synchro categorie
		 */
		
	}


	private class ChargeListeArticle extends AsyncTask<String, Integer, String[]>{

		@Override
		protected String[] doInBackground(String... params) {
			// Liste des elements a inserer dans le fichier
			StringBuilder contenu = new StringBuilder();
			// Extraction de la liste
			try {
				// Connexion a la DB
				Connection connectDb = DbConnexion.connect();
				// Creation d'un statement
				Statement st = connectDb.createStatement();
				// Creation du curseur
				ResultSet rst = st.executeQuery(params[1]);
				// Construction du contenu du fichier
				while( rst.next() ){
					contenu.append(rst.getString(1));
					contenu.append("\n");
				}
				// Fermeture du curseur
				rst.close();
				// Fermeture du statement
				st.close();
				// Fermeture de la connexion
				connectDb.close();
			} catch (ClassNotFoundException e) {
				// Log des erreures
				Log.e(TAG_APPLI, e.getMessage());
			} catch (SQLException e) {
				// Log des erreures
				Log.e(TAG_APPLI, e.getMessage());
			}
			params[1] = contenu.toString();
			return params;
		}
		
		protected void onPostExecute(String[] param) {
//			Log.i(TAG_APPLI, "Post 0  : " + param[0]);
//			Log.i(TAG_APPLI, "Post 1  : " + param[1]);
			synchroParam(param[0], param[1]);
		}
	}
	
	/*
	 *  Synchro des fichiers
	 */
	private void synchroParam(String nomFichier, String contenuFichier){
		// Ouverture de fichier en mode creation
		try {	
			FileWriter fw = new FileWriter(getFilesDir().getAbsolutePath() + File.separator + nomFichier);
			fw.write(contenuFichier);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// Log des erreurs
			Log.i(TAG_APPLI, nomFichier + " : " + e.getMessage());
		}
		
	}
	
}
