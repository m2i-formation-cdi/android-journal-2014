package fr.m2i.journal2014;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import fr.m2i.journal2014.models.DbConnexion;

public class ArticleListe extends ListActivity implements OnClickListener, OnItemSelectedListener {
	// Liste deroulante de choix
	private Spinner spinnerListeSelection;
	// Bouton de retour
	private Button buttonRetourArticleListe;
	// Texte de message
	private TextView textViewMessageArticleList;
	// Source du Spinner
	List<String> listFichier;
	// Selection des articles transmise par l'activite appelante
	private String selection = "";
	// Declaration d'un arrayAdapter pour la liste deroulante
	private ArrayAdapter<String> aa;
	// Mot selectionne pour filtrer les article
	private String monFiltre;
	// Liste des articles selectionnes
//	List<Map<String, String>> listeArticle = null;
	// Definition d'un contexte
	private Context contexte; 
	
	// Tag pour le debug
	private final static String TAG_APPLI = "Journal2014";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_liste);
		
//		selection = "Rubriques";
		selection = this.getIntent().getStringExtra("cle");
//		Log.i(TAG_APPLI, selection);
		
		// Alimentation du Spinner
		alimenterSourceSpinner();
		
		initInterface();
		
		
		
	}

	private void initInterface() {

		// Recuperation du type de selection des articles transmis par l'activite appelante
//		selection = this.getIntent().getStringExtra("cle");
//		selection = "Rubriques";

		// Liaison avec la zone de message
		textViewMessageArticleList = (TextView) findViewById(R.id.textViewMessageArticleList);
		// Information sur le type de selection des articles
		textViewMessageArticleList.setText("Article par " + selection);
<<<<<<< Upstream, based on branch 'master' of https://github.com/m2i-formation-cdi/android-journal-2014.git
=======
		
		// Liaison avec le bouton de retour
		//buttonRetourArticleListe = (Button) findViewById(R.id.buttonRetourArticleListe);
		// Rattachement du bouton au listener
		//buttonRetourArticleListe.setOnClickListener(this);
>>>>>>> e71ebcd JournalisteForm CRUD et photo
		
		// Liaison avec la liste deroulante
		spinnerListeSelection = (Spinner) findViewById(R.id.spinnerListeSelection);
		// Rattachement au listener
		spinnerListeSelection.setOnItemSelectedListener(this);
		// Definition de l'array adapter
		 aa  = new ArrayAdapter(this,
				  android.R.layout.simple_spinner_item
				  , listFichier);
				
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Affectation de la source du spinner
		spinnerListeSelection.setAdapter(aa);

		// Instanciation du contexte
		contexte = this.getBaseContext();
		
	}

	@Override
	public void onClick(View vue) {		
		
	}
	

	@Override
	public void onItemSelected(AdapterView<?> parent, View vue, int position, long id) {
		// Valeur recuperee
		monFiltre = parent.getAdapter().getItem(position).toString();
 		try{ 
			new ChargeListeArticle().execute("");
		}catch(Exception e){
			Log.e(TAG_APPLI, e.getMessage());
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void alimenterSourceSpinner(){
		// Liste des valeurs retournees
		listFichier = new ArrayList<String>();
		// Nom du fichier utilise
		String nomFichier;
		// Ligne de fichier
		String ligne;
		
		// Recherche du nom de fichier associe au parametre recu
		if (selection.equals("Rubriques")) {
			// Recherche par rubrique
			nomFichier = "rubrique.txt";
		}else if (selection.equals("Mots clés")) {
			// Recherche par mot cle
			nomFichier = "mot_cle.txt";
		}else if (selection.equals("Catégories")) {
			// Recherche par categorie
			nomFichier = "categorie.txt";
		}else{
			// Parametre non prevu
			nomFichier = "";
		}
			// Controle de l'association avec un non de fichier
			if (nomFichier.equals("")) {
				// Le parametre n'est pas associe a un fichier
				textViewMessageArticleList.setText("Choix erroné");
			}else{
				// Verification de l'existence du fichier
				File f = new File(getBaseContext().getFilesDir().getAbsolutePath() + File.separator + nomFichier);
			if (f.exists()) {
				// Recuperation des valeurs dans le fichier
				try {
					// Ouverture d'un flux de lecture
					FileReader fr = new FileReader(getFilesDir().getAbsolutePath() + File.separator + nomFichier);
					//  Creation du buffer de lecture
					BufferedReader br = new BufferedReader(fr);
					// Parcours du fichier
					while( (ligne = br.readLine()) != null ){
						// Alimentation de la liste
						listFichier.add(ligne);
					}				
					// Fermeture du buffer
					br.close();
					// Fermeture du flux
					fr.close();			
					
				} catch (FileNotFoundException e) {
					// Log de l'erreur
					Log.e(TAG_APPLI, e.getMessage());
				}catch (IOException e) {	 
					// Log de l'erreur
					Log.e(TAG_APPLI, e.getMessage());
				}
			}else{
				// Le fichier associe n'existe pas
				textViewMessageArticleList.setText("Fichier n'existe pas !");
			}
		}
	} // Fin recuperation contenu de fichier
	
	private class ChargeListeArticle extends AsyncTask<String, Integer, List<HashMap<String, String>> >{

		@Override
		protected List<HashMap<String, String>> doInBackground(String... urls) {
			List<HashMap<String, String>> maListe = new ArrayList<HashMap<String, String>>();
			// Declaration de la chaine Sql
			String monSql;
			// Declaration de la condition supplementaire
			String monWhere;
			// Declaration d'une connexion
			Connection connectDb;
			// Declaration d'un statement
			Statement st;
			// Declaration du curseur
			ResultSet rst;
			
			// Initialisation de la requete
			monSql = "SELECT DISTINCT " +
						"ar.id_article" +
						",ar.titre_article" +
						",ar.chapeau_article" +
						",DATE_FORMAT(ar.date_parution_article,'%d/%m/%Y') date_parution_article" +
					" FROM" +
						" article ar" +
						" LEFT JOIN rubrique ru ON ru.id_rubrique=ar.id_rubrique" +
						" LEFT JOIN article_mot_cle amc ON amc.id_article=ar.id_article" +
						" LEFT JOIN mot_cle mc ON mc.id_mot_cle=amc.id_mot_cle" +
						" LEFT JOIN categorie cat ON cat.id_categorie=ar.id_categorie" +
					" WHERE ar.etat_article=1";
			// Determination de la condition de filtrage supplementaire
			if( selection.equals("Rubriques") ){
				monWhere = "AND ru.rubrique='" + monFiltre + "'";
			}else if( selection.equals("Mots clés") ){
				monWhere = "AND mc.mot_cle='" + monFiltre + "'";
			}else if( selection.equals("Catégories") ){
				monWhere = "AND cat.categorie='" + monFiltre + "'";
			}else{
				monWhere = "AND ar.id_rubrique=-1";
			}
			// Finalisation de la requete
			monSql = monSql + " " + monWhere + " ORDER BY ar.date_parution_article DESC LIMIT 0,25";
//			Log.i(TAG_APPLI, "Selection : " + selection + " - Filtre : " + monFiltre);
//			Log.i(TAG_APPLI, monSql);
			// Recuperation de la liste des articles
 			try {
 				// Instanciation de la connexion
 				connectDb = DbConnexion.connect();
 				// Instanciation du statement
 				st = connectDb.createStatement();
 				// Execution de la requete et ouverture du curseur
 				rst = st.executeQuery(monSql);
 				// Parcours du curseur
 				while( rst.next() ){
 					HashMap<String, String> liste = new HashMap<String, String>();
 					liste.put("id_article", rst.getString("id_article"));
 					liste.put("titre_article", rst.getString("titre_article"));
 					liste.put("chapeau_article", rst.getString("chapeau_article"));
 					liste.put("date_parution_article", rst.getString("date_parution_article"));
 					maListe.add(liste);
 				}
 				// Fermeture du curseur
 				rst.close();
 				// Fermeture du statement
 				st.close();
 				// Fermeture de la connexion
 				connectDb.close();
			} catch (SQLException e) {
				// Log des erreurs
				Log.e(TAG_APPLI, e.getMessage());
			} catch (ClassNotFoundException e) {
				// Log des erreurs
				Log.e(TAG_APPLI, "Class");
				Log.e(TAG_APPLI, e.getMessage());
			}
			return maListe;
		}
		
		protected void onPostExecute(List<HashMap<String, String>> maListe) {
			ListView liste = getListView();
	        SimpleAdapter sa = new SimpleAdapter (
	                contexte,
	                maListe, // nom du map associ� definissant la source
	                R.layout.article_detail,
	                new String[] {"id_article", "titre_article", "date_parution_article", "chapeau_article"}, // cl� du map source
	                new int[] {R.id.textViewIdArticle, R.id.textViewTitreArticleCategorieDetail, R.id.textViewDateParutionArticleCategorieDetail, R.id.textViewChapeauArticleCategorieDetail} // R�f�rence du champ dans le layout secondaire
	            );

	            // -- Attribue a la listView l'adaptateur
	            liste.setAdapter(sa);	
			
			
		}
	} // / TacheAsynchrone


}
