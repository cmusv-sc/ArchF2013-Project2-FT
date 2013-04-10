package cmu.sv.sensor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DBHandler {
	protected Connection connection = null;
	protected Properties prop = null;
	
	protected String serverIP = "";
	protected String serverPort = "";
	protected String dbUser= "";
	protected String dbPassword = "";
	
	DBHandler(String filePath) throws FileNotFoundException{
		this(new FileInputStream(filePath));
	}
	
	DBHandler(FileInputStream fs){
		this.prop = new Properties();
		try {
			
			this.prop.load(fs);
			this.serverIP = prop.getProperty("serverip");
			this.serverPort = prop.getProperty("serverport");
			this.dbUser = prop.getProperty("dbuser");
			this.dbPassword = prop.getProperty("dbpassword");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Unable to read the database properties");
			return;
		}
	}
	protected void makeConnection(){
		try {
			if(this.connection != null && this.connection.isValid(0))
				return;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try { 
		
			this.connection = DriverManager.getConnection( "jdbc:sap://" + serverIP + ":" + serverPort + "/?autocommit=false",dbUser,dbPassword); 

		} catch (SQLException e) {
			System.err.println("Connection Failed. User/Passwd Error?");
			return;

		}
	
	}
	public void closeConnection(){
		try {
			if(this.connection!=null && !this.connection.isClosed()){
				this.connection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.connection = null;
		
	}
	public boolean addReadings(int deviceId, int timeStamp, String sensorType, double value){
		this.makeConnection();
		PreparedStatement preparedStatement;
		try {
			preparedStatement = this.connection.prepareStatement("INSERT INTO CMU_SENSOR( deviceID, timeStamp, sensorType, value) VALUES(?, ?, ?, ?)");
			preparedStatement.setInt(1, deviceId);
			preparedStatement.setInt(2, timeStamp);
			preparedStatement.setString(3, sensorType);
			preparedStatement.setDouble(3, value);
			//stmt.executeQuery();
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	
}
