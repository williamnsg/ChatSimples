package Servidor;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	private static Connection connection = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;
	
	public static void connect(String dbName) {
		if(connection != null)
			return;
		
		try {
			Class.forName("org.sqlite.JDBC");
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:"+dbName);			
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 	
	}
	
	public static ResultSet query(String strQuery) {
		
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(strQuery);
			return resultSet;
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} 
		return resultSet;
	}

	public static boolean execQuery(String strQuery) {
		
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			statement.executeUpdate(strQuery);     
			statement.close();
			return true;
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} 
		return false;
			
	}
}