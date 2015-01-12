package fr.m2i.journal2014;

import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import fr.m2i.journal2014.models.DAOFichier;
import fr.m2i.journal2014.models.DbConnexion;
import fr.m2i.journal2014.models.GenericDAO;
import fr.m2i.journal2014.models.PojoJournaliste;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class JournalisteForm extends Activity implements OnFocusChangeListener, OnClickListener, OnItemSelectedListener {

	private EditText editDateInscription;
	private RadioButton radioMadame;
	private RadioButton radioMonsieur;
	private Spinner spinnerStatut;
	private EditText editTextPrenom;
	private EditText editTextNom;
	private EditText editTextEmail;
	private EditText editTextEmailConfirm;
	private EditText editTextPseudo;
	private EditText editTextMotDePasse;
	private EditText editTextMotDePasseConfirm;
	private EditText editTextPhoto;
	private CheckBox checkBoxOffrePartenaire;

	private boolean isNew;

	private Button btValid;
	private Button btDelete;
	private Button btCancel;
	
	private ArrayAdapter<String> arrayAdapterStatut;

	private String pk;
	
	private PojoJournaliste pojo;

	private DatePickerDialog.OnDateSetListener datePickerDialogListener;
	
	private String validationErrors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.journaliste_form);

		initComponents();
		
		initEventListener();
		
		pojo = new PojoJournaliste();
		
		populateStatut();

		// Récupération des paramètres
		Bundle params = this.getIntent().getExtras();
		pk = params.getString("pk");
		isNew = pk.equals("");

		if (isNew) {
			btDelete.setVisibility(View.INVISIBLE);
		} else {
			AsyncLoadFromDataBase task = new AsyncLoadFromDataBase();
			task.execute("");
		}
	}

	private void initComponents() {

		btValid = (Button) findViewById(R.id.buttonJrFormValid);
		btCancel = (Button) findViewById(R.id.buttonJrFormCancel);
		btDelete = (Button) findViewById(R.id.buttonJrFormDelete);

		radioMadame = (RadioButton) findViewById(R.id.radioJournalisteMadame);
		radioMonsieur = (RadioButton) findViewById(R.id.radioJournalisteMonsieur);
		spinnerStatut = (Spinner) findViewById(R.id.spinnerJournalisteStatut);
		editTextPrenom = (EditText) findViewById(R.id.editTextJournalistePrenom);
		editTextNom = (EditText) findViewById(R.id.editTextJournalisteNom);
		editTextEmail = (EditText) findViewById(R.id.editTextJournalisteEmail);
		editTextEmailConfirm = (EditText) findViewById(R.id.editTextJournalisteEmailConfirm);
		editTextPseudo = (EditText) findViewById(R.id.editTextJournalistePseudo);
		editTextMotDePasse = (EditText) findViewById(R.id.editTextJournalisteMotDePasse);
		editTextMotDePasseConfirm = (EditText) findViewById(R.id.editTextJournalisteMotDePasseConfirm);
		editTextPhoto = (EditText) findViewById(R.id.editTextJournalistePhoto);
		checkBoxOffrePartenaire = (CheckBox) findViewById(R.id.CheckJournalisteOffre);
		editDateInscription = (EditText) findViewById(R.id.EditTextJournalisteDateInscription);
	}

	private void initEventListener() {
		// Écouteurs et gestion d'événements
		
		btCancel.setOnClickListener(this);
		btDelete.setOnClickListener(this);
		btValid.setOnClickListener(this);
		
		spinnerStatut.setOnItemSelectedListener(this);
		
		// Gestion du contrôle datePicker
		datePickerDialogListener = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker dp, int annee, int mois, int jour) {
				// On affecte à dh les valeurs sélectionnées par l'UT
				Calendar dh = Calendar.getInstance();
				 
				dh.set(Calendar.YEAR, annee);
				dh.set(Calendar.MONTH, mois);
				dh.set(Calendar.DAY_OF_MONTH, jour);
				
				

				// --- Formatage de la date (sans l'heure) en fonction de la
				// locale
				DateFormat fmtDate = DateFormat.getDateInstance(
						DateFormat.LONG, Locale.FRANCE);
				// Recupere la date puisque getDateInstance() a ete utilisee
				String lsDate = fmtDate.format(dh.getTime());

				// On affiche la date de naissance formatée dans la zone de
				// saisie
				editDateInscription.setText(lsDate);
			}
		}; // / DatePickerDialog.OnDateSetListener
		
		editDateInscription.setOnFocusChangeListener(this);
		
	}
	
	private void populateStatut(){
		try {
			DAOFichier dao = new DAOFichier(getBaseContext(), R.raw.statut);
			dao.setfirstLineContainsLabel(false);
			dao.loadData();
			String statut[] = dao.getColumnAsArray("col2");
			
			this.arrayAdapterStatut = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item,statut);
			this.arrayAdapterStatut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerStatut.setAdapter(this.arrayAdapterStatut);
		} catch (IOException e) {
			Log.e("Erreur Spinner Statut", e.getMessage());
		} catch (Exception e) {
			Log.e("Erreur Spinner Statut", e.getMessage());
		}
	}
	
	private void setSelectedStatut(String value){
		int nbItems = arrayAdapterStatut.getCount();
		String item;
		boolean found = false;
		
		for (int i = 0; i < nbItems && !found; i++) {
			item = arrayAdapterStatut.getItem(i);
			if(item.equals(value)){
				found = true;
				spinnerStatut.setSelection(i);
			}
			
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		if (v == editDateInscription && hasFocus) {
			Calendar dh;
			if(pojo.getDate_inscription_contributeur() == null){
				dh = Calendar.getInstance();
			} else {
				dh = pojo.getDate_inscription_contributeur();
			}
			
			
			DatePickerDialog dpd = new DatePickerDialog(this,
					datePickerDialogListener, dh.get(Calendar.YEAR),
					dh.get(Calendar.MONTH), dh.get(Calendar.DAY_OF_MONTH));
			dpd.show();
		}

	}
	
	private void populateForm(){
		editTextNom.setText(pojo.getNomContributeur());
		editTextPrenom.setText(pojo.getPrenomContributeur());
		editTextPseudo.setText(pojo.getPseudoContributeur());
		editTextEmail.setText(pojo.getEmailContributeur());
		editTextEmailConfirm.setText(pojo.getEmailContributeur());
		editTextMotDePasse.setText(pojo.getMdpContributeur());
		editTextMotDePasseConfirm.setText(pojo.getMdpContributeur());
		
		if(pojo.getOffresPartenaires() == 1){
			checkBoxOffrePartenaire.setChecked(true);
		}
		
		if(pojo.getCiviliteContributeur() == 'H'){
			radioMonsieur.setChecked(true);
		} else {
			radioMadame.setChecked(true);
		}
		
		String statut = this.getStatutFromId(String.valueOf(pojo.getId_statut()));
		this.setSelectedStatut(statut);
		
		Calendar dh;
		if(pojo.getDate_inscription_contributeur()== null){
			dh = Calendar.getInstance();
		} else {
			dh = pojo.getDate_inscription_contributeur();
		}
		DateFormat fmtDate = DateFormat.getDateInstance(
				DateFormat.LONG, Locale.FRANCE);
		String lsDate = fmtDate.format(dh.getTime());
		editDateInscription.setText(lsDate);
		
		
	}
	
	
	
	private String getStatutFromId(String idStatut){
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
	
	private int getIdFromStatut(String statut){
		int idStatut = 0;
		try {
			DAOFichier dao = new DAOFichier(getBaseContext(), R.raw.statut);
			dao.setfirstLineContainsLabel(false);
			dao.loadData();
			List<Map<String,String>> records = dao.findByColumn("col2", statut);
			if(records.size()>0){
				idStatut = Integer.valueOf(records.get(0).get("col1").toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idStatut;
	}
	
	/******************************************************************************
	 * Tâche asynchrone pour le chargement des données depuis la base de données 
	 * vers le formulaire 
	 * @author seb
	 *
	 *******************************************************************************/
	private class AsyncLoadFromDataBase extends AsyncTask <String, Integer, Map<String, String>> {		
		@Override
		protected Map<String, String> doInBackground(String... params) {
			Map<String, String>resultSet = new HashMap<String, String>();
			try {
				Connection cn = DbConnexion.connect();
				GenericDAO dao = new GenericDAO("contributeur", cn);
				PojoJournaliste pojo = new PojoJournaliste();
				pojo.setIdContributeur(Integer.valueOf(pk));
				resultSet = dao.selectOneByPk(pojo);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return resultSet;
		}
		
		/**
		 * 
		 */
		protected void onPostExecute(Map<String, String> record) {
			mapToPojo(record);
			populateForm();
			
		} // / onPostExecute
		
		private void mapToPojo(Map<String, String> record){
			this.nullToEmptyString(record);
			
			pojo.setIdContributeur(Integer.valueOf(record.get("id_contributeur").toString()));
			pojo.setCiviliteContributeur(record.get("civilite_contributeur").toString().charAt(0));
			pojo.setCv_contributeur(record.get("cv_contributeur").toString());
			pojo.setEmailContributeur(record.get("email_contributeur").toString());
			pojo.setIdContributeur(Integer.valueOf(record.get("id_contributeur").toString()));
			pojo.setMdpContributeur(record.get("mdp_contributeur").toString());
			pojo.setNomContributeur(record.get("nom_contributeur").toString());
			pojo.setOffresPartenaires(Integer.valueOf(record.get("offres_partenaires").toString()));
			pojo.setPhotoContributeur(record.get("photo_contributeur").toString());
			pojo.setPrenomContributeur(record.get("prenom_contributeur").toString());
			pojo.setPseudoContributeur(record.get("pseudo_contributeur").toString());
			pojo.setId_statut(Integer.valueOf(record.get("id_statut").toString()));
			Calendar dateInscription = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.FRANCE);
			try {
				dateInscription.setTime(sdf.parse(record.get("date_inscription_contributeur").toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			pojo.setDate_inscription_contributeur(dateInscription);
		}
		
		private Map<String, String> nullToEmptyString(Map<String, String> record){
			Set<String> keys = record.keySet();
			for (Object key : keys) {
				if(record.get(key) == null){
					record.put(key.toString(), "");
				}
			}
			return record;
		}
	}

	@Override
	public void onClick(View v) {
		
		if(v==btCancel){
			
		}
		
		if(v== btValid){
			
		}
		
		if(v==btDelete){
			
		}
		
		finish();
		
	}
	
	private void formToPojo(){
		char civ;
		if(radioMadame.isChecked()){
			civ = 'F';
		} else {
			civ = 'H';
		}
		
		String statut = spinnerStatut.getSelectedItem().toString();
		int idStatut = getIdFromStatut(statut);
		
		pojo.setId_statut(idStatut);
		pojo.setCiviliteContributeur(civ);
		pojo.setNomContributeur(editTextNom.getText().toString());
		pojo.setEmailContributeur(editTextEmail.getText().toString());
		pojo.setPrenomContributeur(editTextPrenom.getText().toString());
		
		
	}
	
	private boolean validateForm(){
		boolean isValid = true;
		
		
		
		return isValid;
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if(view == spinnerStatut){
			String statut = parent.getSelectedItem().toString();
			int idStatut = getIdFromStatut(statut);
			pojo.setId_statut(idStatut);
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
