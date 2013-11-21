package models;

import org.json.JSONException;
import org.json.JSONObject;

public class Device {
	private String uri;
	private String userDefinedFields;
	private Location location;
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

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

}
