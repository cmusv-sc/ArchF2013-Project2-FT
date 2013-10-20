package models;

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
	
	public void setDeviceId(String id){
		this.deviceId = id;
	}
	
	public String getDeviceType(){
		return deviceType;
	}
	
	public void setDeviceType(String type){
		this.deviceType = type;
	}
	
	public String getDeviceAgent(){
		return deviceAgent;
	}
	
	public void setDeviceAgent(String agent){
		this.deviceAgent = agent;
	}
		
	public String getDeviceLocation(){
		return deviceLocation;
	}
	
	public void setDeviceLocation(String location){
		this.deviceLocation = location;
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
	
	public String executeSQL(String sql, int number_of_result_columns){
		if(dbHandler == null){
			dbHandler = new DBHandler("conf/database.properties");
		}
		return dbHandler.runQuery(sql, number_of_result_columns);
	}

	public boolean save(){
		if(dbHandler == null){
			dbHandler = new DBHandler("conf/database.properties");
		}
		return dbHandler.addDevice(deviceId, deviceType, deviceAgent, deviceLocation);

	}
}
