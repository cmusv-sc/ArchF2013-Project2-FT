package models.cmu.sv.sensor;

import java.sql.ResultSet;

import org.json.JSONException;
import org.json.JSONObject;

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
		
	public double getValue(){
		return value;
	}
		
	public String toCSVString() {
		return deviceId + "," + timeStamp + "," + sensorType + "," + String.valueOf(value);
	}
	
	public String toJSONString() {
		String jsonString = new String();
		try {
			JSONObject obj=new JSONObject();
			obj.put("device_id",  deviceId);
			obj.put("timestamp", timeStamp);
			obj.put("sensor_type", sensorType);
			obj.put("value", value);
			jsonString = obj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonString;
	}
	
	public ResultSet executeSQL(String sql){
		if(dbHandler == null){
			dbHandler = new DBHandler("conf/database.properties");
		}
		return dbHandler.runQuery(sql);
	}

	public boolean save(){
		if(dbHandler == null){
			dbHandler = new DBHandler("conf/database.properties");
		}
		return dbHandler.addReading(deviceId, timeStamp, sensorType, value);

	}
}
