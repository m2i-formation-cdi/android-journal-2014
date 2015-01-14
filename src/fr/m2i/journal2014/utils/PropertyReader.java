package fr.m2i.journal2014.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class PropertyReader {
	private Properties properties;
	private Context context;

	public PropertyReader(Context context) {
		super();
		this.context = context;
		this.properties = new Properties();
	}

	public Properties getProperties(String FileName) {

		try {
			AssetManager assetManager = context.getAssets();
			InputStream inputStream = assetManager.open(FileName);
			properties.load(inputStream);

		} catch (IOException e) {
			Log.e("PropertyReader", e.toString());
		}
		return properties;
	}

}
