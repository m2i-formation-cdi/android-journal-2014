package fr.m2i.journal2014;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * CRUD d'un fichier TXT/CSV de /data/data/.../files dans ... 
 * 
 * Pour la validation on reecrit tout le ArrayList dans le fichier
 * 
 * @author pascal
 * 
 */
public class Rubrique extends Activity implements OnClickListener {

	private EditText editTextIdRubrique;
	private EditText editTextRubrique;

	private Button buttonClsRubrique;
	private Button buttonAjouterRubrique;
	private Button buttonSupprimerRubrique;
	private Button buttonModifierRubrique;

	private Button buttonPremierRubrique;
	private Button buttonPrecedentRubrique;
	private Button buttonSuivantRubrique;
	private Button buttonDernierRubrique;

	private Button buttonRollbackRubrique;
	private Button buttonCommitRubrique;

	private TextView textViewMessageRubrique;

	private final String FICHIER_TXT = "rubriques.txt";
	

	private Context contexte;
	
	private List<Map<String,String>> lstRubriques;
	private int nbRecords = 0;
	private int currentRecord = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rubrique);

		this.contexte = getBaseContext();

		initInterface();
		getDataFromCsv();
		nbRecords = lstRubriques.size()-1;
		showRecord();

	} // / onCreate

	/**
	 * 
	 */
	private void initInterface() {

		editTextIdRubrique = (EditText) findViewById(R.id.editTextIdRubrique);
		editTextRubrique = (EditText) findViewById(R.id.editTextRubrique);

		buttonClsRubrique = (Button) findViewById(R.id.buttonClsRubrique);
		buttonAjouterRubrique = (Button) findViewById(R.id.buttonAjouterRubrique);
		buttonSupprimerRubrique = (Button) findViewById(R.id.buttonSupprimerRubrique);
		buttonModifierRubrique = (Button) findViewById(R.id.buttonModifierRubrique);

		buttonPremierRubrique = (Button) findViewById(R.id.buttonPremierRubrique);
		buttonPrecedentRubrique = (Button) findViewById(R.id.buttonPrecedentRubrique);
		buttonSuivantRubrique = (Button) findViewById(R.id.buttonSuivantRubrique);
		buttonDernierRubrique = (Button) findViewById(R.id.buttonDernierRubrique);

		buttonRollbackRubrique = (Button) findViewById(R.id.buttonRollbackRubrique);
		buttonCommitRubrique = (Button) findViewById(R.id.buttonCommitRubrique);

		textViewMessageRubrique = (TextView) findViewById(R.id.textViewMessageRubrique);

		buttonClsRubrique.setOnClickListener(this);
		buttonAjouterRubrique.setOnClickListener(this);
		buttonSupprimerRubrique.setOnClickListener(this);
		buttonModifierRubrique.setOnClickListener(this);

		buttonPremierRubrique.setOnClickListener(this);
		buttonPrecedentRubrique.setOnClickListener(this);
		buttonSuivantRubrique.setOnClickListener(this);
		buttonDernierRubrique.setOnClickListener(this);

		buttonRollbackRubrique.setOnClickListener(this);
		buttonCommitRubrique.setOnClickListener(this);

	} // / initInterface

	

	/*
	 * LES EVENEMENTS
	 * 
	 */
	
	@Override
	public void onClick(View v) {
		if(v == buttonSuivantRubrique){
			currentRecord++;
			showRecord();
		}
		
		if(v==buttonPrecedentRubrique){
			currentRecord--;
			showRecord();
		}
		
		if(v==buttonPremierRubrique){
			currentRecord = 1;
			showRecord();
		}
		
		if(v== buttonDernierRubrique){
			currentRecord = nbRecords;
			showRecord();
		}
		
		if(v==buttonClsRubrique){
			editTextIdRubrique.setText("");
			editTextRubrique.setText("");
		}
		
		if(v==buttonAjouterRubrique){
			addToList(true);
		}
		
		if(v==buttonModifierRubrique){
			addToList(false);
		}
		
		if(v==buttonCommitRubrique){
			saveToCSV();
		}
		
		if(v==buttonSupprimerRubrique){
			lstRubriques.remove(currentRecord);
			nbRecords = lstRubriques.size() -1;
			if(currentRecord > nbRecords){
				currentRecord--;
			}
			showRecord();	
		}
		
		if(v==buttonRollbackRubrique){
			getDataFromCsv();
			currentRecord = 1;
			showRecord();
		}
		

	} // / onClick

	private void showRecord(){
		Map<String,String> record;
		record = lstRubriques.get(currentRecord);
		
		editTextIdRubrique.setText(record.get("id"));
		editTextRubrique.setText(record.get("rubrique"));
		
		boolean isFirst = (currentRecord == 1);
		boolean isLast = (currentRecord == nbRecords);
		
		buttonPremierRubrique.setEnabled(! isFirst);
		buttonPrecedentRubrique.setEnabled(! isFirst);
		
		buttonDernierRubrique.setEnabled(! isLast);
		buttonSuivantRubrique.setEnabled(! isLast);
		
	}///showRecord
	
	private void addToList(boolean isNew){
		String id = editTextIdRubrique.getText().toString();
		String rubrique = editTextRubrique.getText().toString();
		
		if(rubrique.equals("")){
			Toast toastMessage = Toast.makeText(contexte, "Vous devez saisir une rubrique", Toast.LENGTH_LONG);
			toastMessage.show();
		} else {
			Map<String,String> record = new HashMap<String,String>();
			record.put("id", id);
			record.put("rubrique", rubrique);
			if(isNew){
				lstRubriques.add(record);
				currentRecord = lstRubriques.size()-1;
				nbRecords++;
				showRecord();
			} else {
				lstRubriques.set(currentRecord, record);
			}
		}
	}///addToList
	
	private void getDataFromCsv(){
		try {
			InputStream is = contexte.openFileInput(FICHIER_TXT);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bfr = new BufferedReader(isr);
			String ligne;
			String [] cols;
			
			
			lstRubriques = new ArrayList<Map<String,String>>();
			Map<String, String> item;
			
			while((ligne = bfr.readLine()) != null){
				cols = ligne.split(";");
				if(cols.length == 2){
					item = new HashMap<String, String>();
					item.put("id", cols[0]);
					item.put("rubrique", cols[1]);
					
					lstRubriques.add(item);
				}
				
			}
			bfr.close();
			isr.close();
			is.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}///getDataFromCSV
	
	private String ListToCSV(){
		StringBuilder sb = new StringBuilder();
		
		for (Iterator<Map<String, String>> iterator = lstRubriques.iterator(); iterator.hasNext();) {
			Map<String, String> item = (Map<String, String>) iterator.next();
			sb.append(item.get("id"));
			sb.append(";");
			sb.append(item.get("rubrique"));
			sb.append("\n");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}///ListToCSV
	
	private void saveToCSV(){
		OutputStream os;
		OutputStreamWriter osw;
		
		try {
			os = contexte.openFileOutput(FICHIER_TXT, MODE_PRIVATE);
			osw = new OutputStreamWriter(os);
			osw.write(ListToCSV());
			
			osw.close();
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}///saveToCSV
	

} // / class