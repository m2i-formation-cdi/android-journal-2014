package fr.m2i.journal2014.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import android.content.Context;
import fr.m2i.journal2014.utils.PropertyReader;

public class DbConnexion {

	/*
	private static String dbHost = "jdbc:mysql://172.26.10.54:3306/journal2014";
	private static String dbUser = "p";
	private static String dbPass = "b";
*/
	public static Connection connect(Context context) throws ClassNotFoundException,
			SQLException {
		Connection cn;
		Class.forName("com.mysql.jdbc.Driver");
		
		PropertyReader propReader = new PropertyReader(context);
		Properties p = propReader.getProperties("params.properties");
		
		String dbHost = p.getProperty("DbProtocol");
		dbHost += p.getProperty("DbIP") + ":" + p.getProperty("DbPort");
		dbHost += "/" + p.getProperty("DbName");
		
		String dbUser = p.getProperty("DbUser");
		String dbPass = p.getProperty("DbPass");
		cn = DriverManager.getConnection(dbHost, dbUser, dbPass);
		cn.setAutoCommit(true);
		return cn;
	}
}
