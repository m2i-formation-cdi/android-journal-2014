package fr.m2i.journal2014;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity implements
		android.view.View.OnClickListener {

	private EditText editTextIdentifiant;
	private EditText editTextMotDePasse;
	private TextView textViewErreur;
	private Button btOk;
	private Button btAnnuler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Récupération des contrôles de l'activité
		btOk = (Button) findViewById(R.id.buttonOK);
		btAnnuler = (Button) findViewById(R.id.buttonAnnuler);
		editTextIdentifiant = (EditText) findViewById(R.id.editTextIdentifiant);
		editTextMotDePasse = (EditText) findViewById(R.id.editTextMotDePasse);
		textViewErreur = (TextView) findViewById(R.id.textViewLoginErreur);

		//Gestion des événements
		btOk.setOnClickListener(this);
		btAnnuler.setOnClickListener(this);

	}

	/**
	 * Validation de la validité de la saisie de l'identifiant et du mot de
	 * passe
	 * 
	 * @return boolean
	 */
	private boolean validerSaisie() {
		// Instanciation et initialisation d'un StringBuilder pour le message
		// d'erreur
		StringBuilder sbErreur = new StringBuilder(
				"Veuillez corriger les erreurs suivantes");
		boolean saisieOk = true;

		// Contrôle de l'identifiant
		if (editTextIdentifiant.equals("")) {
			sbErreur.append("\nVous devez saisir un identifiant");
			saisieOk = false;
		}
		// Contrôle du mot de passe
		if (editTextMotDePasse.equals("")) {
			sbErreur.append("\nVous devez saisir un mot de passe");
			saisieOk = false;
		}
		// Affichage du message d'erreur
		if (!saisieOk) {
			textViewErreur.setText(sbErreur.toString());
		}

		return saisieOk;
	}

	@Override
	public void onClick(View v) {
		if(v== btOk){
			
		} else if(v== btAnnuler){
			
		}
	
		
	}
}
