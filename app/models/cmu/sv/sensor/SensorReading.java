package models.cmu.sv.sensor;

import java.sql.ResultSet;

public class SensorReading {
	private static DBHandler dbHandler = null;
	private String deviceId;
	private Long timeStamp;
	private String sensorType;
	private double value;
	
	public SensorReading(){
		dbHandler = new DBHandler("conf/database.properties");
		
		
	}
	public SensorReading(String deviceId, Long timeStamp, String sensorType, double value){
		this.deviceId = deviceId;
		this.timeStamp = timeStamp;
		this.sensorType = sensorType;
		this.value = value;
	}
	
	public String getDeviceId(){
		return deviceId;
	}
	
	public Long getTimeStamp(){
		return timeStamp;
	}
	
	public String getSensorType(){
		return sensorType;
	}
	
	public ResultSet executeSQL(String sql){
		if(dbHandler == null){
			dbHandler = new DBHandler("conf/database.properties");
		}
		return dbHandler.runQuery(sql);
	}
	
	public double getValue(){
		return value;
	}
	public boolean save(){
		if(dbHandler == null){
			dbHandler = new DBHandler("conf/database.properties");
		}
		return dbHandler.addReading(deviceId, timeStamp, sensorType, value);

	}
}
