package fr.m2i.journal2014.models;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import fr.m2i.journal2014.R;

public class DAOFichier {

	private String fileName;
	private int fileRessource;
	private String fieldSeparator = ";";

	private String[] fieldsName;
	private List<Map<String, String>> records;
	private boolean firstLineContainsLabel = true;

	private Context context;
	private int sourceFileMode;

	private int nbRecords;
	
	private static final int RESSOURCE = 1;
	private static final int FILE = 2;
	

	// ---------------------------------------------------------------------------
	// CONSTRUCTEURS
	// ---------------------------------------------------------------------------
	public DAOFichier(Context context, int fileRessource) throws IOException {
		super();
		this.fileRessource = fileRessource;
		this.context = context;
		this.sourceFileMode = DAOFichier.RESSOURCE;
		
	}

	public DAOFichier(Context context, String fileName) throws IOException {
		super();
		this.context = context;
		this.fileName = fileName;
		this.sourceFileMode = DAOFichier.FILE;
		
	}
	
	public void loadData() throws IOException{
		this.getData();
	}
	
	private InputStream getInputStream() throws FileNotFoundException{
		InputStream is;
		if(sourceFileMode == DAOFichier.FILE){
			is = context.openFileInput(this.fileName);
		} else {
			is = context.getResources().openRawResource(this.fileRessource);
		}
		return is;
	}
	
	private OutputStream getOutputStream() throws Exception{
		OutputStream os;
		if(sourceFileMode == DAOFichier.FILE){
			os = context.openFileOutput(this.fileName, Context.MODE_PRIVATE);
		} else {
			throw new Exception("Ecriture impossible sur un fichier de ressources");
		}
		return os;
	}
	
	
	
	/*
	public DAOFichier(InputStream is) throws IOException {
		super();
		inputStream = is;
		this.getData();
	}*/

	// ---------------------------------------------------------------------------
	// LECTURE DES DONNEES
	// ---------------------------------------------------------------------------
	private void getData() throws IOException {
		InputStream is = this.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader bfr = new BufferedReader(isr);
		String line;
		String[] cols;

		records = new ArrayList<Map<String,String>>();
		Map<String, String> item;
		boolean isFirstLine = true;

		while ((line = bfr.readLine()) != null) {
			cols = line.split(this.fieldSeparator);
			if (isFirstLine) {
				this.getFieldsName(cols);
				if (! firstLineContainsLabel) {
					item = this.getRecord(cols);
					records.add(item);
				}
			} else if(cols.length == fieldsName.length){
				item = this.getRecord(cols);
				records.add(item);
			}
			isFirstLine = false;

		}
		bfr.close();
		isr.close();
		is.close();

		this.nbRecords = records.size();
	}

	/**
	 * Récupération des entêtes de colonne
	 * 
	 * @param record
	 * @return
	 */
	private String[] getFieldsName(String[] record) {
		if (firstLineContainsLabel) {
			fieldsName = record;
		} else {
			fieldsName = new String[record.length];
			
			for (int i = 0; i < fieldsName.length; i++) {
				StringBuilder sb = new StringBuilder();
				sb.append("col");
				sb.append(String.valueOf(i + 1));
				fieldsName[i] = sb.toString();
			}
		}
		return fieldsName;

	}

	/**
	 * Récupération d'un enregistrement
	 * 
	 * @param data
	 * @return
	 */
	private Map<String, String> getRecord(String[] data) {
		Map<String, String> item = new LinkedHashMap<String, String>();
		for (int i = 0; i < this.fieldsName.length; i++) {
			item.put(fieldsName[i], data[i]);
		}
		return item;
	}

	// ---------------------------------------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------------------------------------
	public boolean getFirstLineContainsLabel() {
		return firstLineContainsLabel;
	}

	public void setfirstLineContainsLabel(boolean firstLineContainsLabel) {
		this.firstLineContainsLabel = firstLineContainsLabel;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public int getNbRecords() {
		return nbRecords;
	}

	public void setNbRecords(int nbRecords) {
		this.nbRecords = nbRecords;
	}

	// ---------------------------------------------------------------------------
	// DAO
	// ---------------------------------------------------------------------------

	public Map<String, String> getRecordByIndex(int index) {
		Map<String, String> record;
		if (index >= 0 && index < this.nbRecords) {
			record = records.get(index);
		} else {
			throw new IndexOutOfBoundsException();
		}
		return record;
	}
	
	public List<Map<String,String>> getAll(){
		return this.records;
	}
	
	public void deleteByIndex(int index){
		if (index > 0 && index < this.nbRecords) {
			records.remove(index);
			this.nbRecords = records.size()-1;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
	
	public void addRecord(String[] data){
		Map<String,String> record = getRecord(data);
		records.add(record);
		this.nbRecords = records.size(); 
	}
	
	public void addRecord(Map<String,String> record){
		records.add(record);
		this.nbRecords = records.size(); 
	}
	
	public void updateRecord(String[] data, int index){
		Map<String,String> record = getRecord(data);
		records.set(index, record);
	}
	
	public void updateRecord(Map<String,String> record, int index){
		records.set(index, record);
	}
	
	public void rollback(){
		try {
			this.getData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getColumnAsArray(String columnName) throws Exception{
		String[] data = new String[records.size()];
		for (int i = 0; i < records.size(); i++) {
			Map<String,String> item = this.getRecordByIndex(i);
			if(item.containsKey(columnName)){
				data[i] = item.get(columnName);
			} else {
				throw new Exception("The key " + columnName + " does not exists");
			}
		}
		
		return data;
	}
	
	public void commit() throws Exception{
		OutputStream os = this.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < records.size(); i++) {
			sb.append(recordToCSV(i));
			sb.append("\n");
		}
		sb.deleteCharAt(sb.length()-1);
		
		osw.write(sb.toString());	
	}
	
	private String recordToCSV(int index){
		StringBuilder sb = new StringBuilder();
		Map<String,String> record = records.get(index);
		String value;
		for (int i = 0; i < fieldsName.length; i++) {
			value = record.get(fieldsName[i]);
			sb.append(value);
			sb.append(fieldSeparator);
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	

}
