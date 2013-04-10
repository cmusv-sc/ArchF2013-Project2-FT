package cmu.sv.sensor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.*;

public class DBHandler {
	protected Connection connection = null;
	protected Properties prop = null;
	
	protected String serverIP = "";
	protected String serverPort = "";
	protected String dbUser= "";
	protected String dbPassword = "";
	
	
	public DBHandler(FileInputStream fs){
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
	public boolean makeConnection(){
		try {
			if(this.connection != null && this.connection.isValid(0))
				return true;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try { 
		
			this.connection = DriverManager.getConnection( "jdbc:sap://" + serverIP + ":" + serverPort + "/?autocommit=false",dbUser,dbPassword); 
			return true;

		} catch (SQLException e) {
			System.err.println("Connection Failed. User/Passwd Error?");
			return false;

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
	public boolean addReading(int deviceId, int timeStamp, String sensorType, double value){
		this.makeConnection();
		PreparedStatement preparedStatement;
		try {
			preparedStatement = this.connection.prepareStatement("INSERT INTO CMU_SENSOR( deviceID, timeStamp, sensorType, value) VALUES(?, ?, ?, ?)");
			preparedStatement.setInt(1, deviceId);
			preparedStatement.setInt(2, timeStamp);
			preparedStatement.setString(3, sensorType);
			preparedStatement.setDouble(4, value);
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean deleteReading(int deviceId, int timeStamp){
		this.makeConnection();
		PreparedStatement preparedStatement;
		try{
			preparedStatement = this.connection.prepareStatement("DELETE CMU_SENSOR WHERE deviceID=? AND timeStamp=?");
			preparedStatement.setInt(1, deviceId);
			preparedStatement.setInt(2, timeStamp);
			preparedStatement.executeUpdate();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public SensorReading searchReading(int deviceId, int timeStamp){
		this.makeConnection();
		PreparedStatement preparedStatement;
		try{
			preparedStatement = this.connection.prepareStatement("SELECT * FROM CMU_SENSOR WHERE deviceID=? AND timeStamp=?");
			preparedStatement.setInt(1, deviceId);
			preparedStatement.setInt(2, timeStamp);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			deviceId = resultSet.getInt(1);
			timeStamp = resultSet.getInt(2);
			String sensorType = resultSet.getString(3);
			double value = resultSet.getDouble(4);
			return new SensorReading(deviceId, timeStamp, sensorType, value);
			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	
}
