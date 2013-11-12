package models;

import org.json.JSONException;
import org.json.JSONObject;

public class Device {
	private static DBHandler dbHandler = null;
	private String uri;
	private String userDefinedFields;
	private String representation;
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUserDefinedFields() {
		return userDefinedFields;
	}

	public void setUserDefinedFields(String userDefinedFields) {
		this.userDefinedFields = userDefinedFields;
	}

	public Device(){
		dbHandler = new DBHandler("conf/database.properties");	
	}
	
	public Device(String deviceId, String deviceType, String deviceAgent, String deviceLocation){
		this.uri = deviceAgent;
		this.userDefinedFields = deviceLocation;
	}
	
	
	public String getDeviceAgent(){
		return uri;
	}
	
	public void setDeviceAgent(String agent){
		this.uri = agent;
	}
		
	public String getLocation(){
		return userDefinedFields;
	}
	
	public void setLocation(String location){
		this.userDefinedFields = location;
	}
		
	public String getCSVHeader() {
		return "uri,device_type,device_agent,device_location\n";
	}
	
	public String toCSVString() {
		return uri + "," + userDefinedFields;
	}
	
	public String toJSONString() {
		String jsonString = new String();
		try {
			JSONObject obj=new JSONObject();
			obj.put("device_agent", uri);
			obj.put("device_location", userDefinedFields);
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

	public String getRepresentation() {
		return representation;
	}

	public void setRepresentation(String representation) {
		this.representation = representation;
	}

//	publisc boolean save(){
//		if(dbHandler == null){
//			dbHandler = new DBHandler("conf/database.properties");
//		}
//		return dbHandler.addDevice(deviceId, deviceTypeName, uri, userDefinedFields);

//	}
}
