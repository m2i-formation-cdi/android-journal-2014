package fr.m2i.journal2014;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Journalistes extends ListActivity {

	private List<Map<String, String>> listJournalistes;
	private ListView listeViewJournalistes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.RemplirListView();
		setContentView(R.layout.journalistes);

		//listeViewJournalistes = (ListView) findViewById(R.id.listViewJournalistes);
		RemplirListView();
		
	}

	/**
	 * Remplit la liste des journalistes
	 */
	private void RemplirListView() {

		try {
			this.listeViewJournalistes = this.getListView();
			this.setData();
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

	/**
	 * Création de données artificielles pour remplir la liste des journalistes
	 */
	private void setData() {
		this.listJournalistes = new ArrayList<Map<String, String>>();
		Map<String, String> record;

		record = new HashMap<String, String>();
		record.put("nom", "Edwy Plenel");
		record.put("statut", "rédacteur en chef");
		record.put("id", "1");
		//record.put("image", "plenel.png");
		this.listJournalistes.add(record);

		record = new HashMap<String, String>();
		record.put("nom", "Fabrice Arfi");
		record.put("statut", "journaliste");
		record.put("id", "2");
		//record.put("image", "arfi.png");
		this.listJournalistes.add(record);

		record = new HashMap<String, String>();
		record.put("nom", "Fabrice Lhomme");
		record.put("statut", "journaliste");
		record.put("id", "3");
		//record.put("image", "lhomme.png");
		this.listJournalistes.add(record);

		record = new HashMap<String, String>();
		record.put("nom", "Gérard Davet");
		record.put("statut", "journaliste");
		record.put("id", "4");
		//record.put("image", "davet.png");
		this.listJournalistes.add(record);

		record = new HashMap<String, String>();
		record.put("nom", "Pierre Haski");
		record.put("statut", "rédacteur en chef");
		record.put("id", "4");
		//record.put("image", "haski.png");
		this.listJournalistes.add(record);
	}
	
	public void onListItemClick(ListView parent, View v, int position, long id) {
		
	}
}
