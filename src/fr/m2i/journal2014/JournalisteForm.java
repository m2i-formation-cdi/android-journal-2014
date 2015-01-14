package fr.m2i.journal2014;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;

public class JournalisteForm extends Activity implements OnFocusChangeListener,
		OnClickListener, OnItemSelectedListener {

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
	private Button btCapture;

	private ArrayAdapter<String> arrayAdapterStatut;

	private String pk;

	private PojoJournaliste pojo;

	private DatePickerDialog.OnDateSetListener datePickerDialogListener;

	private String validationErrors;
	static final int REQUEST_IMAGE_CAPTURE = 1;

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
		btCapture = (Button) findViewById(R.id.buttonJrFormPhoto);

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
		btCapture.setOnClickListener(this);

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

	private void populateStatut() {
		try {
			DAOFichier dao = new DAOFichier(getBaseContext(), R.raw.statut);
			dao.setfirstLineContainsLabel(false);
			dao.loadData();
			String statut[] = dao.getColumnAsArray("col2");

			this.arrayAdapterStatut = new ArrayAdapter<String>(
					getBaseContext(), android.R.layout.simple_spinner_item,
					statut);
			this.arrayAdapterStatut
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerStatut.setAdapter(this.arrayAdapterStatut);
		} catch (IOException e) {
			Log.e("Erreur Spinner Statut", e.getMessage());
		} catch (Exception e) {
			Log.e("Erreur Spinner Statut", e.getMessage());
		}
	}

	private void setSelectedStatut(String value) {
		int nbItems = arrayAdapterStatut.getCount();
		String item;
		boolean found = false;

		for (int i = 0; i < nbItems && !found; i++) {
			item = arrayAdapterStatut.getItem(i);
			if (item.equals(value)) {
				found = true;
				spinnerStatut.setSelection(i);
			}

		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		if (v == editDateInscription && hasFocus) {
			Calendar dh;
			if (pojo.getDateInscriptionContributeur() == null) {
				dh = Calendar.getInstance();
			} else {
				dh = pojo.getDateInscriptionContributeur();
			}

			DatePickerDialog dpd = new DatePickerDialog(this,
					datePickerDialogListener, dh.get(Calendar.YEAR),
					dh.get(Calendar.MONTH), dh.get(Calendar.DAY_OF_MONTH));
			dpd.show();
		}

	}

	private void populateForm() {
		editTextNom.setText(pojo.getNomContributeur());
		editTextPrenom.setText(pojo.getPrenomContributeur());
		editTextPseudo.setText(pojo.getPseudoContributeur());
		editTextEmail.setText(pojo.getEmailContributeur());
		editTextEmailConfirm.setText(pojo.getEmailContributeur());
		editTextMotDePasse.setText(pojo.getMdpContributeur());
		editTextMotDePasseConfirm.setText(pojo.getMdpContributeur());
		editTextPhoto.setText(pojo.getPhotoContributeur());

		if (pojo.getOffresPartenaires() == 1) {
			checkBoxOffrePartenaire.setChecked(true);
		}

		if (pojo.getCiviliteContributeur() == 'H') {
			radioMonsieur.setChecked(true);
		} else {
			radioMadame.setChecked(true);
		}

		String statut = this
				.getStatutFromId(String.valueOf(pojo.getIdStatut()));
		this.setSelectedStatut(statut);

		Calendar dh;
		if (pojo.getDateInscriptionContributeur() == null) {
			dh = Calendar.getInstance();
		} else {
			dh = pojo.getDateInscriptionContributeur();
		}
		DateFormat fmtDate = DateFormat.getDateInstance(DateFormat.LONG,
				Locale.FRANCE);
		String lsDate = fmtDate.format(dh.getTime());
		editDateInscription.setText(lsDate);

	}

	private String getStatutFromId(String idStatut) {
		String statut = "";
		try {
			DAOFichier dao = new DAOFichier(getBaseContext(), R.raw.statut);
			dao.setfirstLineContainsLabel(false);
			dao.loadData();
			List<Map<String, String>> records = dao.findByColumn("col1",
					idStatut);
			if (records.size() > 0) {
				statut = records.get(0).get("col2").toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return statut;
	}

	private int getIdFromStatut(String statut) {
		int idStatut = 0;
		try {
			DAOFichier dao = new DAOFichier(getBaseContext(), R.raw.statut);
			dao.setfirstLineContainsLabel(false);
			dao.loadData();
			List<Map<String, String>> records = dao
					.findByColumn("col2", statut);
			if (records.size() > 0) {
				idStatut = Integer.valueOf(records.get(0).get("col1")
						.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return idStatut;
	}

	/******************************************************************************
	 * Tâche asynchrone pour le chargement des données depuis la base de données
	 * vers le formulaire
	 * 
	 * @author seb
	 *
	 *******************************************************************************/
	private class AsyncLoadFromDataBase extends
			AsyncTask<String, Integer, Map<String, String>> {

		private Connection cn;

		@Override
		protected Map<String, String> doInBackground(String... params) {
			Map<String, String> resultSet = new HashMap<String, String>();
			try {
				cn = DbConnexion.connect();
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
			try {
				cn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			mapToPojo(record);
			populateForm();

		} // / onPostExecute

		private void mapToPojo(Map<String, String> record) {
			this.nullToEmptyString(record);

			pojo.setIdContributeur(Integer.valueOf(record
					.get("id_contributeur").toString()));
			pojo.setCiviliteContributeur(record.get("civilite_contributeur")
					.toString().charAt(0));
			pojo.setCvContributeur(record.get("cv_contributeur").toString());
			pojo.setEmailContributeur(record.get("email_contributeur")
					.toString());
			pojo.setIdContributeur(Integer.valueOf(record
					.get("id_contributeur").toString()));
			pojo.setMdpContributeur(record.get("mdp_contributeur").toString());
			pojo.setNomContributeur(record.get("nom_contributeur").toString());
			pojo.setOffresPartenaires(Integer.valueOf(record.get(
					"offres_partenaires").toString()));
			pojo.setPhotoContributeur(record.get("photo_contributeur")
					.toString());
			pojo.setPrenomContributeur(record.get("prenom_contributeur")
					.toString());
			pojo.setPseudoContributeur(record.get("pseudo_contributeur")
					.toString());
			pojo.setIdStatut(Integer
					.valueOf(record.get("id_statut").toString()));
			Calendar dateInscription = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
					Locale.FRANCE);
			try {
				dateInscription.setTime(sdf.parse(record.get(
						"date_inscription_contributeur").toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			pojo.setDateInscriptionContributeur(dateInscription);
		}

		private Map<String, String> nullToEmptyString(Map<String, String> record) {
			Set<String> keys = record.keySet();
			for (Object key : keys) {
				if (record.get(key) == null) {
					record.put(key.toString(), "");
				}
			}
			return record;
		}
	}// / AsyncLoadFromDatabase

	/************************************************************************************
	 * 
	 * @author seb
	 *
	 ************************************************************************************/
	private class AsyncPersistInDataBase extends
			AsyncTask<String, Integer, String> {

		private Connection cn;

		@Override
		protected String doInBackground(String... params) {
			String message = "";
			try {
				cn = DbConnexion.connect();
				GenericDAO dao = new GenericDAO("contributeur", cn);
				if (params[0].equals("Delete")) {
					dao.delete(pojo);
					message = "Votre enregistrement a été supprimé";
				} else if (isNew) {
					dao.insert(pojo);
					//cn.commit();
					message = "Votre enregistrement a été sauvegardé";
				} else {
					dao.update(pojo);
					//cn.commit();
					message = "Votre enregistrement a été modifié";
				}
			} catch (Exception e) {
				message = "Une erreur empêche l'exécution de votre commande";
				e.printStackTrace();
			}

			return message;
		}

		protected void onPostExecute(String message) {

			try {
				cn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
					.show();

		} // / onPostExecute

	}// / AsyncPersistInDataBase

	@Override
	public void onClick(View v) {

		if (v == btCancel) {
			finish();
		}

		if (v == btValid) {
			if (this.validateForm()) {
				this.formToPojo();
				AsyncPersistInDataBase persistTask = new AsyncPersistInDataBase();
				persistTask.execute("AddEdit");
				finish();
			} else {
				Toast.makeText(getApplicationContext(), validationErrors, Toast.LENGTH_LONG).show();
			}
			
		}

		if (v == btDelete) {
			if (!isNew) {
				this.formToPojo();
				AsyncPersistInDataBase persistTask = new AsyncPersistInDataBase();
				persistTask.execute("Delete");
				finish();
			} else {
				String message = "Vous ne pouvez pas supprimer un nouvel enregistrement";

				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();

			}
			
		}
		
		if(v==btCapture){
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
		        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		    }
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        Bundle extras = data.getExtras();
	        //Bitmap imageBitmap = (Bitmap) extras.get("data");
	        //mImageView.setImageBitmap(imageBitmap);
	    }
	}

	private void formToPojo() {
		char civ;
		if (radioMadame.isChecked()) {
			civ = 'F';
		} else {
			civ = 'H';
		}

		String statut = spinnerStatut.getSelectedItem().toString();
		int idStatut = getIdFromStatut(statut);

		pojo.setIdStatut(idStatut);
		pojo.setCiviliteContributeur(civ);
		pojo.setNomContributeur(editTextNom.getText().toString());
		pojo.setEmailContributeur(editTextEmail.getText().toString());
		pojo.setPrenomContributeur(editTextPrenom.getText().toString());
		pojo.setMdpContributeur(editTextMotDePasse.getText().toString());
		pojo.setPseudoContributeur(editTextPseudo.getText().toString());
		pojo.setPhotoContributeur(editTextPhoto.getText().toString());
		if (!pk.equals("")) {
			pojo.setIdContributeur(Integer.valueOf(pk));
		}

		if (checkBoxOffrePartenaire.isChecked()) {
			pojo.setOffresPartenaires(1);
		}else {
			pojo.setOffresPartenaires(0);
		}

		String inutDateInscription = editDateInscription.getText().toString();
		if (!inutDateInscription.equals("")) {
			try {
				Calendar dateInscription = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy",
						Locale.FRANCE);
				dateInscription.setTime(sdf.parse(inutDateInscription));
				pojo.setDateInscriptionContributeur(dateInscription);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

	}

	private boolean validateForm() {
		boolean isValid = true;
		StringBuilder sb = new StringBuilder();

		
		if(editTextPseudo.getText().toString().equals("")){
			isValid = false;
			sb.append("Le pseudo est requis\n");
		}
		
		if(! editTextEmail.getText().toString().equals(editTextEmailConfirm.getText().toString())){
			isValid = false;
			sb.append("L'adresse e-mail et sa confirmation sont différentes\n");
		}
		
		if(! editTextMotDePasse.getText().toString().equals(editTextMotDePasseConfirm.getText().toString())){
			isValid = false;
			sb.append("Le mot de passe et sa confirmation sont requis\n");
		}
		
		if(editDateInscription.getText().toString().equals("")){
			isValid = false;
			sb.append("La date d'inscription est requise\n");
		}
		
		this.validationErrors = sb.toString();
		return isValid;

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (view == spinnerStatut) {
			String statut = parent.getSelectedItem().toString();
			int idStatut = getIdFromStatut(statut);
			pojo.setIdStatut(idStatut);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
}
