package fr.m2i.journal2014;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class ArticlePlus extends Activity implements OnClickListener {

	private EditText editTextTitre;
	private EditText editTextChapeau;
	private EditText editTextResume;
	private EditText editTextTexte;
	private ListView listViewContributeur;
	private Spinner spinnerRubrique;
	private Spinner spinnerCategorie;
	private ListView listViewMotCle;

	private Button buttonCls;
	private Button buttonAjouter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_plus);

		//initInterface();

	}

	private void initInterface() {

		editTextTitre = (EditText) findViewById(R.id.editTextTitre);
		editTextChapeau = (EditText) findViewById(R.id.editTextChapeau);
		editTextResume = (EditText) findViewById(R.id.editTextResume);
		editTextTexte = (EditText) findViewById(R.id.editTextTexte);
		listViewContributeur = (ListView) findViewById(R.id.listViewContributeur);
		spinnerRubrique = (Spinner) findViewById(R.id.spinnerRubrique);
		spinnerCategorie = (Spinner) findViewById(R.id.spinnerCategorie);
		listViewMotCle = (ListView) findViewById(R.id.listViewMotCle);

		buttonCls.setOnClickListener(this);
		buttonAjouter.setOnClickListener(this);

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.article_plus, menu);
//		return true;
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
