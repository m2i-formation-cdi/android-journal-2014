package fr.m2i.journal2014.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnexion {

	private static String dbHost = "jdbc:mysql://172.26.10.54:3306/journal2014";
	private static String dbUser = "p";
	private static String dbPass = "b";

	public static Connection connect() throws ClassNotFoundException,
			SQLException {
		Connection cn;
		Class.forName("com.mysql.jdbc.Driver");
		cn = DriverManager.getConnection(dbHost, dbUser, dbPass);
		return cn;
	}
}
