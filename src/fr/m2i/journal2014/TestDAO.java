package fr.m2i.journal2014;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fr.m2i.journal2014.models.DAOFichier;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TestDAO extends Activity {

	private TextView message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_dao);
		
		StringBuilder sb = new StringBuilder();
		
		message = (TextView) findViewById(R.id.textViewTestDaoMessage);
		Map<String,String> item;
		Map<String,String> itemBackup;
		
		try {
			DAOFichier dao = new DAOFichier(getBaseContext(), "rubriques.txt");
			
			
			//Test de lecture
			itemBackup = dao.getRecordByIndex(1); 
			sb.append("Lecture de données : ");
			sb.append(itemBackup.get("rubrique"));
			sb.append("\n");
			
			//Test de suppression
			dao.deleteByIndex(1);
			item = dao.getRecordByIndex(1);
			sb.append("Suppression du 2e enregistrement \n");
			sb.append("Lecture de données : ");
			sb.append(item.get("rubrique"));
			sb.append("\n"); 
			
			
			//Test d'ajout
			Map<String,String> record = new HashMap<String, String>();
			record.put("id_rubrique", "8");
			record.put("rubrique", "TEST AJOUT");
			sb.append(dao.getNbRecords());
			sb.append(" enregistrements\n");
			dao.addRecord(record);
			
			item = dao.getRecordByIndex(dao.getNbRecords()-1);
			sb.append("Ajout d'enregistrement \n");
			sb.append("Lecture de données : ");
			sb.append(item.get("rubrique"));
			sb.append("\n");
			sb.append(dao.getNbRecords());
			sb.append(" enregistrements");
			sb.append("\n");
			
			/*
			//test Modification
			record = dao.getRecordByIndex(1);
			record.put("rubrique", "TEST MODIF");
			dao.updateRecord(record, 1);
			item = dao.getRecordByIndex(1);
			sb.append("Modif d'enregistrement \n");
			sb.append("Lecture de données : ");
			sb.append(item.get("rubrique"));
			sb.append("\n");
			
			//Test rollback
			dao.rollback();
			item = dao.getRecordByIndex(1);
			sb.append("Rollback enregistrement \n");
			sb.append("Lecture de données : ");
			sb.append(item.get("rubrique"));
			sb.append("\n"); 
			
			dao.commit();
			*/
			
			message.setText(sb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("Erreur", e.getMessage());
			//e.printStackTrace();
		}
	}
}
