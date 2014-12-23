package fr.m2i.journal2014;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Categorie extends Activity{

	TextView txtViewCategorie;
	List<Map<String,String>> lstCategorie;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categorie);
		
		txtViewCategorie = (TextView) findViewById(R.id.textViewCategories);
		
		xmlToList();
		
		txtViewCategorie.setText(this.listToString());
	}
	
	private void xmlToList(){
		lstCategorie = new ArrayList<Map<String,String>>();
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
							item.put("id", value);
							value = xpp.getAttributeValue(null, "categorie");
							item.put("categorie", value);
							lstCategorie.add(item);
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
	
	private String listToString(){
		StringBuilder sb = new StringBuilder();
		
		for (Iterator iterator = lstCategorie.iterator(); iterator.hasNext();) {
			Map<String, String> item = (Map<String, String>) iterator.next();
			sb.append(item.get("id"));
			sb.append(" : ");
			sb.append(item.get("categorie"));
			sb.append("\n");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
}
