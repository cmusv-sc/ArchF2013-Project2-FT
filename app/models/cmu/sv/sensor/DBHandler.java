package models.cmu.sv.sensor;

import java.util.ArrayList;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import play.Logger.ALogger;

public class DBHandler {
	//protected Connection connection = null;
	protected Properties prop = null;	
	protected String serverIP = "";
	protected String serverPort = "";
	protected String dbUser = "";
	protected String dbPassword = "";
		
	public DBHandler(String fileName){
		System.out.println(fileName);
		//For heroku: Use local env instead
		if(System.getenv("serverip") != null){
			this.serverIP = System.getenv("serverip");
			this.serverPort = System.getenv("serverport");
			this.dbUser = System.getenv("dbuser");
			this.dbPassword = System.getenv("dbpassword");
		} else{
			this.prop = new Properties();
			try {
				this.prop.load(new FileInputStream(fileName));
				this.serverIP = prop.getProperty("serverip");
				this.serverPort = prop.getProperty("serverport");
				this.dbUser = prop.getProperty("dbuser");
				this.dbPassword = prop.getProperty("dbpassword");			
			} catch (Exception e) {				
				System.err.println("Unable to read the database properties");
				return;
			}
		}
	}
	
	public Connection getConnection(){
		Connection connection = null;
		try { 		
			Class.forName("com.sap.db.jdbc.Driver");
			connection = DriverManager.getConnection( "jdbc:sap://" + serverIP + ":" + serverPort + "/?autocommit=false?reconnect=true",dbUser,dbPassword); 
			//PreparedStatement preparedStatement = this.connection.prepareStatement("SET SCHEMA CMU");
			//return preparedStatement.execute();
			connection.setAutoCommit(true);
			//System.out.println("DB connection to " + serverIP + " established.");			
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.err.println("Connection Failed. User/Passwd Error?");
			return null;
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return connection;
	}
	
	public String runQuery(String sql, int number_of_result_columns) {
		Connection connection = getConnection();
		if (connection == null) return null;
		// limit maximum of 1000 records if limit is not specified in the sql 
		if (!sql.toLowerCase().contains(" limit ")) {
			if (sql.trim().endsWith(";")) {
				sql = sql.trim();
				sql = sql.substring(0, sql.length() - 1);
			}
			sql += " LIMIT 1000";
		}
		ResultSet resultSet = null;
		String resultStr = "";
		try {
			System.out.println("Execute the sql on db: " + sql);
			long startQueryTime = System.nanoTime();
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			long finishQueryTime = System.nanoTime();
			if(resultSet == null){
				resultStr = "no result found";
			} else {			
				while(resultSet.next()){
					for (int i = 1; i <= number_of_result_columns; i++) {
						resultStr += resultSet.getString(i);
						if (i < number_of_result_columns)
							resultStr += ", ";
					}
					resultStr += "\n";
				}
			}
			connection.close();
			//System.out.println("Connection closed.");
			System.out.println("run query execution time=" + (finishQueryTime - startQueryTime) / 1000000 + 
					"ms.");			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return resultStr;
	}
	
	public boolean addDevice(String deviceId, String deviceType, String deviceAgent, String deviceLocation){
		Connection connection = getConnection();
		if (connection == null) return false;
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement("INSERT INTO CMU.DEVICE(DEVICEID, DEVICETYPE, DEVICEAGENT, LOCATION) VALUES(?, ?, ?, ?)");
			preparedStatement.setString(1, deviceId);
			preparedStatement.setString(2, deviceType);
			preparedStatement.setString(3, deviceAgent);
			preparedStatement.setString(4, deviceLocation);
			preparedStatement.executeUpdate();
			connection.close();
			//System.out.println("Connection closed.");
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			ALogger log = play.Logger.of(DBHandler.class);
			log.warn(e.getMessage());
			return false;
		}	
	}
	
	public ArrayList<Device> getDevice(){
		try{
			Connection connection = getConnection();
			if (connection == null) return null;
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("SELECT \"DEVICEID\", \"DEVICETYPE\", \"DEVICEAGENT\", \"LOCATION\" FROM CMU.DEVICE");	
			ResultSet resultSet = preparedStatement.executeQuery();
			ArrayList<Device> devices = new ArrayList<Device>();			
			while(resultSet.next()){			
				String rs_deviceId = resultSet.getString(1);
				String rs_deviceType = resultSet.getString(2);
				String rs_deviceAgent = resultSet.getString(3);
				String rs_location = resultSet.getString(4);
				devices.add(new Device(rs_deviceId, rs_deviceType, rs_deviceAgent, rs_location));
			}
			connection.close();
			//System.out.println("Connection closed.");
			return devices;			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}	
	
	public ArrayList<String> getSensorType(String deviceType){
		try{
			Connection connection = getConnection();
			if (connection == null) return null;
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("SELECT \"SENSOR_TYPE\" FROM CMU.SENSOR_TYPE WHERE DEVICE_TYPE=?");
			preparedStatement.setString(1, deviceType);
			ResultSet resultSet = preparedStatement.executeQuery();
			ArrayList<String> sensorTypes = new ArrayList<String>();			
			while(resultSet.next()){			
				String rs_sensorType = resultSet.getString(1);
				sensorTypes.add(new String(rs_sensorType));
			}
			connection.close();
			//System.out.println("Connection closed.");
			return sensorTypes;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}	
	
	public boolean addReading(String deviceId, Long timeStamp, String sensorType, double value){
		Connection connection = getConnection();
		if (connection == null) return false;
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement("INSERT INTO CMU.CMU_SENSOR(deviceID, timeStamp, sensorType, value) VALUES(?, ?, ?, ?)");
			preparedStatement.setString(1, deviceId);
			preparedStatement.setLong(2, timeStamp);
			preparedStatement.setString(3, sensorType);
			preparedStatement.setDouble(4, value);
			preparedStatement.executeUpdate();
			connection.close();
			//System.out.println("Connection closed.");
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			ALogger log = play.Logger.of(DBHandler.class);
			log.warn(e.getMessage());
			return false;
		}
		
	}
	
	public boolean deleteReading(String deviceID, Long timeStamp, String sensorType){
		Connection connection = getConnection();
		if (connection == null) return false;
		PreparedStatement preparedStatement;
		try{
			preparedStatement = connection.prepareStatement("DELETE FROM CMU.CMU_SENSOR WHERE deviceID=? AND timeStamp=? AND sensorType=?");
			preparedStatement.setString(1, deviceID);
			preparedStatement.setLong(2, timeStamp);
			preparedStatement.setString(3, sensorType);
			preparedStatement.executeUpdate();
			connection.close();
			//System.out.println("Connection closed.");
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public SensorReading searchReading(String deviceId, Long timeStamp, String sensorType){
		try{
			Connection connection = getConnection();
			if (connection == null) return null;
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("SELECT * FROM CMU.CMU_SENSOR WHERE deviceID=? AND timeStamp<=? AND sensorType=? ORDER BY timeStamp DESC LIMIT 1");	
			preparedStatement.setString(1, deviceId);
			preparedStatement.setLong(2, timeStamp);
			preparedStatement.setString(3, sensorType);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(!resultSet.next()){
				return null;
			}
			String rs_deviceId = resultSet.getString(1);
			Long rs_timeStamp = resultSet.getLong(2);
			String rs_sensorType = resultSet.getString(3);
			double rs_value = resultSet.getDouble(4);			
			connection.close();
			//System.out.println("Connection closed.");
			return new SensorReading(rs_deviceId, rs_timeStamp, rs_sensorType, rs_value);			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<SensorReading> searchReading(String deviceId, Long startTime, long endTime, String sensorType){		
		try{
			Connection connection = getConnection();
			if (connection == null) return null;
			System.out.println("Search reading " + deviceId + "," + startTime + "," + endTime + "," + sensorType);
			PreparedStatement preparedStatement;
			long startQueryTime = System.nanoTime();
			preparedStatement = connection.prepareStatement("SELECT \"TIMESTAMP\", \"VALUE\" FROM \"CMU\".\"CMU_SENSOR\"" 
					+ " WHERE \"DEVICEID\" = ? AND \"TIMESTAMP\" >= ? AND \"TIMESTAMP\" <= ? AND \"SENSORTYPE\" = ? ORDER BY \"TIMESTAMP\" DESC");	
			preparedStatement.setString(1, deviceId);
			preparedStatement.setLong(2, startTime);
			preparedStatement.setLong(3, endTime);
			preparedStatement.setString(4, sensorType);
			ResultSet resultSet = preparedStatement.executeQuery();
			long finishQueryTime = System.nanoTime();
			ArrayList<SensorReading> readings = new ArrayList<SensorReading>();
			while(resultSet.next()){
				Long rs_timeStamp = resultSet.getLong(1);
				double rs_value = resultSet.getDouble(2);
				readings.add(new SensorReading(deviceId, rs_timeStamp, sensorType, rs_value));
			}
			connection.close();
			System.out.println(readings.size() + " reading(s) fetched in searchReading." + 
					" queryTime=" + (finishQueryTime - startQueryTime) / 1000000 + 
					"ms.");
			return readings;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}

	// Currently this method is for heatmap etc application that requires all readings from all devices, to 
	// avoid network traffic. However, this method may not be scalable when the number of devices keeps on
	// increase.
	// TODO: Consider publisher/subscriber pattern to refind this set of queries.
	public ArrayList<SensorReading> lastReadingFromAllDevices(Long timeStamp, String sensorType){
		try{
			Connection connection = getConnection();
			if (connection == null) return null;
			PreparedStatement preparedStatement;			
			long startTime = System.nanoTime();
			preparedStatement = connection.prepareStatement("SELECT \"DEVICEID\", \"TIMESTAMP\", \"VALUE\" FROM " +
				"(SELECT * FROM \"CMU\".\"CMU_SENSOR\" AS a " + 
				"INNER JOIN " +
				"(SELECT " +
				"\"DEVICEID\" as device_id," +
				"max(\"TIMESTAMP\") as max_timestamp " +
				"FROM \"CMU\".\"CMU_SENSOR\" " +
				"WHERE \"SENSORTYPE\" = ? " + // 1st parameter - sensorType
				"AND ? / 1000 - \"TIMESTAMP\" / 1000 >= 0 " + // 2nd parameter - timeStamp
				"AND ? / 1000 - \"TIMESTAMP\" / 1000 <= 210 " + // 3rd parameter - timeStamp
				"GROUP BY \"DEVICEID\"" +
				") b "+
				"ON "+
				"a.\"DEVICEID\" = b.device_id AND " +
				"a.\"TIMESTAMP\" = b.max_timestamp " +
				"WHERE a.\"SENSORTYPE\" = ?)"); // 4th parameter - sensorType
			preparedStatement.setString(1, sensorType);
			preparedStatement.setLong(2, timeStamp);
			preparedStatement.setLong(3, timeStamp);
			preparedStatement.setString(4, sensorType);
			ResultSet resultSet = preparedStatement.executeQuery();
			long finishQueryTime = System.nanoTime();
			ArrayList<SensorReading> readings = new ArrayList<SensorReading>();			
			while(resultSet.next()){
				String rs_deviceId = resultSet.getString(1);
				Long rs_timeStamp = resultSet.getLong(2);
				double rs_value = resultSet.getDouble(3);
				readings.add(new SensorReading(rs_deviceId, rs_timeStamp, sensorType, rs_value));
			}
			System.out.println(readings.size() + " reading(s) fetched in last_readings_from_all_devices." + 
					" queryTime=" + (finishQueryTime - startTime) / 1000000 + 
					"ms.");
			connection.close();
			//System.out.println("Connection closed.");
			return readings;			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}	
	
	public ArrayList<SensorReading> lastestReadingFromAllDevices(String sensorType){
		try{
			Connection connection = getConnection();
			if (connection == null) return null;
			PreparedStatement preparedStatement;			
			long startTime = System.nanoTime();
			preparedStatement = connection.prepareStatement("SELECT \"DEVICEID\", \"TIMESTAMP\", \"VALUE\" FROM " +
				"(SELECT * FROM \"CMU\".\"CMU_SENSOR\" AS a " + 
				"INNER JOIN " +
				"(SELECT " +
				"\"DEVICEID\" as device_id," +
				"max(\"TIMESTAMP\") as max_timestamp " +
				"FROM \"CMU\".\"CMU_SENSOR\" " +
				"WHERE \"SENSORTYPE\" = ? " + // 1st parameter - sensorType
				"GROUP BY \"DEVICEID\"" +
				") b "+
				"ON "+
				"a.\"DEVICEID\" = b.device_id AND " +
				"a.\"TIMESTAMP\" = b.max_timestamp " +
				"WHERE a.\"SENSORTYPE\" = ?)"); // 2nd parameter - sensorType
			preparedStatement.setString(1, sensorType);
			preparedStatement.setString(2, sensorType);
			ResultSet resultSet = preparedStatement.executeQuery();
			long finishQueryTime = System.nanoTime();
			ArrayList<SensorReading> readings = new ArrayList<SensorReading>();			
			while(resultSet.next()){
				String rs_deviceId = resultSet.getString(1);
				Long rs_timeStamp = resultSet.getLong(2);
				double rs_value = resultSet.getDouble(3);
				readings.add(new SensorReading(rs_deviceId, rs_timeStamp, sensorType, rs_value));
			}
			System.out.println(readings.size() + " reading(s) fetched in lastest_readings_from_all_devices." + 
					" queryTime=" + (finishQueryTime - startTime) / 1000000 + 
					"ms.");
			connection.close();
			//System.out.println("Connection closed.");
			return readings;			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}	
	
}
