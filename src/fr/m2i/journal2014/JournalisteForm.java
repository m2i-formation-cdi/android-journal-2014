package fr.m2i.journal2014;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class JournalisteForm extends Activity implements OnFocusChangeListener {

	private EditText editDateInscription;
	private Spinner spinnerCivilite;
	private boolean isNew;
	
	private Button btValid;
	private Button btDelete;
	private Button btCancel;
	
	private String pk;

	private DatePickerDialog.OnDateSetListener datePickerDialogListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.journaliste_form);

		editDateInscription = (EditText) findViewById(R.id.EditTextJournalisteDateInscription);
		btValid = (Button) findViewById(R.id.buttonJrFormValid);
		btCancel = (Button) findViewById(R.id.buttonJrFormCancel);
		btDelete = (Button) findViewById(R.id.buttonJrFormDelete);
		
		//Récupération des paramètres
		Bundle params = this.getIntent().getExtras();
		pk = params.getString("pk");
		isNew = pk.equals("");
		
		if(isNew){
			btDelete.setVisibility(View.INVISIBLE);
		}
		

		// -------------------------------------------------------------------------------
		// Écouteurs et gestion d'événements

		//Gestion du contrôle datePicker
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
	}
	
	

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
		if (v == editDateInscription && hasFocus) {
			// Recuperation de la date actuelle
			Calendar dh = Calendar.getInstance();
			DatePickerDialog dpd = new DatePickerDialog(this,
					datePickerDialogListener, dh.get(Calendar.YEAR),
					dh.get(Calendar.MONTH), dh.get(Calendar.DAY_OF_MONTH));
			dpd.show();
		}

	}
}
