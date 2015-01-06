package fr.m2i.journal2014;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MotCle extends Activity implements OnClickListener, OnItemSelectedListener{

	private Context contexte;
	
	private Spinner spinnerMotsCles;
	private EditText editTextIdMotCle;
	private EditText editTextMotCle;
	private TextView textViewMessage;
	
	private Button buttonClsMotCle;
	private Button buttonAjouterMotCle;
	private Button buttonSupprimerMotCle;
	private Button buttonModifierMotCle;
	private Button buttonRollbackMotCle;
	private Button buttonCommitMotCle;
	
	private int currentRecord;
	
	
	private List<Map<String, String>> lstRecord;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mot_cle);

		this.contexte = getBaseContext();

		initInterface();
		
		xmlToList();
		populateSpinner();
		showRecord();
		
		

	} // / onCreate
	
	private void initInterface(){
		spinnerMotsCles = (Spinner) findViewById(R.id.spinnerMotsCles);
		editTextIdMotCle = (EditText) findViewById(R.id.editTextIdMotCle);
		editTextMotCle = (EditText) findViewById(R.id.editTextMotCle);
		
		textViewMessage = (TextView) findViewById(R.id.textViewMessageMotCle);
		
		buttonClsMotCle = (Button) findViewById(R.id.buttonClsMotCle);
		buttonAjouterMotCle = (Button) findViewById(R.id.buttonAjouterMotCle);
		buttonSupprimerMotCle = (Button) findViewById(R.id.buttonSupprimerMotCle);
		buttonModifierMotCle = (Button) findViewById(R.id.buttonModifierMotCle);
		buttonRollbackMotCle = (Button) findViewById(R.id.buttonRollbackMotCle);
		buttonCommitMotCle = (Button) findViewById(R.id.buttonCommitMotCle);
		
		buttonClsMotCle.setOnClickListener(this);
		buttonAjouterMotCle.setOnClickListener(this);
		buttonSupprimerMotCle.setOnClickListener(this);
		buttonModifierMotCle.setOnClickListener(this);
		buttonRollbackMotCle.setOnClickListener(this);
		buttonCommitMotCle.setOnClickListener(this);
		
		spinnerMotsCles.setOnItemSelectedListener(this);
	}
	
	private void showRecord(){
		Map<String,String> record;
		record = lstRecord.get(currentRecord);
		
		editTextIdMotCle.setText(record.get("id_mot_cle"));
		editTextMotCle.setText(record.get("mot_cle"));
		
	}///showRecord
	
	private void xmlToList(){
		lstRecord = new ArrayList<Map<String,String>>();
		String value;
		XmlPullParser xpp = getResources().getXml(R.xml.categories);
		try {
			//Boucle sur l'ensemble des balises du doc xml
			while(xpp.getEventType() != XmlPullParser.END_DOCUMENT){
				//Test de la balise ouvrante
				if(xpp.getEventType() == XmlPullParser.START_TAG){
					//Test du nom de la balise en cours
					if(xpp.getName().equals("enr")){
						Map item = new HashMap<String, String>();
						if(xpp.getAttributeCount()>0){
							value = xpp.getAttributeValue(0);
							item.put("id_mot_cle", value);
							value = xpp.getAttributeValue(1);
							item.put("mot_cle", value);
							lstRecord.add(item);
						}//Balise avec attributs
					}//Balise Enr
				}//Start tag
				xpp.next();
			}
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void populateSpinner(){
		int initialPosition = spinnerMotsCles.getSelectedItemPosition();
		String[] tItems = new String[lstRecord.size()];
		int count = 0;
		for (Map<String, String> item : lstRecord) {
			tItems[count] = item.get("mot_cle");
			count++;
		}
		ArrayAdapter<String> aa = new ArrayAdapter<String>(contexte, android.R.layout.simple_spinner_item,tItems );
		spinnerMotsCles.setAdapter(aa);
		spinnerMotsCles.setSelection(initialPosition);
	}

	@Override
	public void onClick(View v) {
		if(v==buttonClsMotCle){
			editTextIdMotCle.setText("");
			editTextMotCle.setText("");
		}
		
		if(v==buttonSupprimerMotCle){
			
			lstRecord.remove(currentRecord);
			populateSpinner();
			int nbRecords = lstRecord.size() -1;
			if(currentRecord > nbRecords){
				currentRecord--;
			}
			showRecord();	
		}
		
		if(v==buttonAjouterMotCle){
			Map<String,String> record = new HashMap<String, String>();
			record.put("id_mot_cle", editTextIdMotCle.getText().toString());
			record.put("mot_cle", editTextMotCle.getText().toString());
			
			lstRecord.add(record);
			populateSpinner();
			currentRecord = lstRecord.size()-1;
			showRecord();
			
		}
		
		if(v==buttonModifierMotCle){
			Map<String,String> record = new HashMap<String, String>();
			record.put("id_mot_cle", editTextIdMotCle.getText().toString());
			record.put("mot_cle", editTextMotCle.getText().toString());
			
			lstRecord.set(currentRecord, record);
			populateSpinner();
			
		}
		
		if(v==buttonRollbackMotCle){
			currentRecord = 0;
			xmlToList();
			populateSpinner();
			showRecord();
		}
		
		if(v==buttonCommitMotCle){
			
		}
		
	}
	
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
		currentRecord = position;
		showRecord();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	
	private String listToXml(){
		StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<root>\n");
		for (Map<String, String> item : lstRecord) {
			sb.append("<enr id_mot_cle=\"");
			sb.append(item.get("id_mot_cle"));
			sb.append("\" mot_cle=\"");
			sb.append(item.get("mot_cle"));
			sb.append("\" />\n");
			
		}
		sb.append("</root>");
		
		return sb.toString();
	}
	
	private void saveToXml(){
		String xmlData = listToXml();
		OutputStream os;
		OutputStreamWriter osw;
		
		try {
			os = contexte.openFileOutput("mots_cles.xml", MODE_PRIVATE);
			osw = new OutputStreamWriter(os);
			osw.write(xmlData);
			
			osw.close();
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	
}
