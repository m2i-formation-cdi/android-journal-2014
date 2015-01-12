package fr.m2i.journal2014;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.m2i.journal2014.models.DAOFichier;
import fr.m2i.journal2014.models.DbConnexion;
import fr.m2i.journal2014.models.GenericDAO;
import fr.m2i.journal2014.models.PojoJournaliste;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Journalistes extends ListActivity implements OnClickListener {

	private List<Map<String, String>> listJournalistes;
	private ListView listeViewJournalistes;
	private Button btAjout;
	
	private Intent intentionFormulaire;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.journalistes);
		
		//Récupération des références des éléments d'interface
		btAjout = (Button) findViewById(R.id.buttonAjoutJournaliste);
		
		//écouteurs des gestionnaires d'événements
		btAjout.setOnClickListener(this);
		
		//Définition des intentions
		intentionFormulaire = new Intent(this.getBaseContext(),JournalisteForm.class);
		
		//Remplissage de la liste des journalistes depuis un tâche asynchrone
		TacheAsynchrone asyncTask = new TacheAsynchrone();
		asyncTask.execute("");	
	}

	/**
	 * Remplit la liste des journalistes
	 */
	private void populateListJournalistes() {

		try {
			this.listeViewJournalistes = this.getListView();
			SimpleAdapter sa = new SimpleAdapter(this.getBaseContext(),
					this.listJournalistes, R.layout.liste_journalistes,
					new String[] { "nom", "statut", "id" }, new int[] {
							R.id.lstJournalisteNom, R.id.lstJournalisteStatut,
							R.id.lstJournalisteId });

			this.listeViewJournalistes.setAdapter(sa);
			
		} catch (Exception e) {
			Log.i("Erreur", e.getMessage());
			Toast.makeText(getBaseContext(), "Erreur ? " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	
	
	private class TacheAsynchrone extends AsyncTask <String, Integer, List<Map<String, String>>> {

		//private List<Map<String, String>> resultSet;
		
		@Override
		protected List<Map<String, String>> doInBackground(String... params) {
			List<Map<String, String>> resultSet = new ArrayList<Map<String,String>>();
			try {
				Connection cn = DbConnexion.connect();
				GenericDAO dao = new GenericDAO("contributeur", cn);
				resultSet = dao.selectAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			return resultSet;
		}
		
		/**
		 * 
		 */
		protected void onPostExecute(List<Map<String, String>> rs) {
			listJournalistes = this.getFilteredData(rs);
			populateListJournalistes();
			
		} // / onPostExecute
		
		private List<Map<String, String>> getFilteredData(List<Map<String, String>> rs){
			List<Map<String, String>> filteredData = new ArrayList<Map<String, String>>();
			
			String idStatut;
			String statut;
			
			for (Map<String, String> map : rs) {
				Map<String,String> record = new HashMap<String, String>();
				idStatut = map.get("id_statut");
				statut = this.getStatut(idStatut);
				record.put("nom", map.get("prenom_contributeur") + " " + map.get("nom_contributeur"));
				record.put("id", map.get("id_contributeur"));
				record.put("statut", statut);
				filteredData.add(record);	
			}
			
			return filteredData;
		}

		private String getStatut(String idStatut){
			String statut = "";
			try {
				DAOFichier dao = new DAOFichier(getBaseContext(), R.raw.statut);
				dao.setfirstLineContainsLabel(false);
				dao.loadData();
				List<Map<String,String>> records = dao.findByColumn("col1", idStatut);
				if(records.size()>0){
					statut = records.get(0).get("col2").toString();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return statut;
			
		}
		
	}
	
	
	
	public void onListItemClick(ListView parent, View v, int position, long id) {
		String pk = listJournalistes.get(position).get("id");
		intentionFormulaire.putExtra("pk", pk);
		startActivityForResult(intentionFormulaire, 2);
	}

	@Override
	public void onClick(View v) {
		if(v==btAjout){
			intentionFormulaire.putExtra("pk", "");
			startActivityForResult(intentionFormulaire, 1);
		}
	}
}
