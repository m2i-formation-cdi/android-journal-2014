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
		// d'erreur, le message est récupéré de string.xml
		StringBuilder sbErreur = new StringBuilder(
				getString(R.string.validation_erreur));
		boolean saisieOk = true;

		// Contrôle de l'identifiant
		if (editTextIdentifiant.equals("")) {
			sbErreur.append("\n");
			//récupération du message d'erreur depuis string.xml
			sbErreur.append(getString(R.string.validation_login_absent));
			saisieOk = false;
		}
		// Contrôle du mot de passe
		if (editTextMotDePasse.equals("")) {
			sbErreur.append("\n");
			sbErreur.append(getString(R.string.validation_mdp_absent));
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
