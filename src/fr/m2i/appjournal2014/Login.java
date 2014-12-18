package fr.m2i.appjournal2014;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {

	private EditText editTextIdentifiant;
	private EditText editTextMotDePasse;
	private Button btOk;
	private Button btAnnuler;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		//Récupération des contrôles de l'activité
		btOk = (Button) findViewById(R.id.buttonOK);
		btAnnuler = (Button) findViewById(R.id.buttonAnnuler);
		editTextIdentifiant = (EditText) findViewById(R.id.editTextIdentifiant);
		editTextMotDePasse = (EditText) findViewById(R.id.editTextMotDePasse);
		
	}
}
