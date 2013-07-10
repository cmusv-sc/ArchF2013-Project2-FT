package models.cmu.sv.sensor;

import java.sql.ResultSet;

import org.json.JSONException;
import org.json.JSONObject;

public class Device {
	private static DBHandler dbHandler = null;
	private String deviceId;	
	private String deviceType;
	private String deviceAgent;
	private String deviceLocation;
	
	
	public Device(){
		dbHandler = new DBHandler("conf/database.properties");	
	}
	
	public Device(String deviceId, String deviceType, String deviceAgent, String deviceLocation){
		this.deviceId = deviceId;
		this.deviceType = deviceType;
		this.deviceAgent = deviceAgent;
		this.deviceLocation = deviceLocation;
	}
	
	public String getDeviceId(){
		return deviceId;
	}
	
	public String getDeviceType(){
		return deviceType;
	}
	
	public String getDeviceAgent(){
		return deviceAgent;
	}
		
	public String getDeviceLocation(){
		return deviceLocation;
	}
		
	public String getCSVHeader() {
		return "uri,device_type,device_agent,device_location\n";
	}
	
	public String toCSVString() {
		return deviceId + "," + deviceType + "," + deviceAgent + "," + deviceLocation;
	}
	
	public String toJSONString() {
		String jsonString = new String();
		try {
			JSONObject obj=new JSONObject();
			obj.put("uri",  deviceId);
			obj.put("device_type", deviceType);
			obj.put("device_agent", deviceAgent);
			obj.put("device_location", deviceLocation);
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
		return dbHandler.addDevice(deviceId, deviceType, deviceAgent, deviceLocation);

	}
}
