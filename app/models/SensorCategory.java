package models;

import org.json.JSONException;
import org.json.JSONObject;

public class SensorCategory {
	private String sensorCategoryName;
	private String purpose;
	public String getSensorCategoryName() {
		return sensorCategoryName;
	}
	public void setSensorCategoryName(String sensorCategoryName) {
		this.sensorCategoryName = sensorCategoryName;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	public String toCSVString() {
		return "TODO";
	}
	
	public String getCSVHeader() {
		return "sensor_category_name,purpose\n";
	}
	
	public String toJSONString() {
		String jsonString = new String();
		try {
			JSONObject obj=new JSONObject();
			obj.put("sensor_category_name",  sensorCategoryName);
			obj.put("purpose", purpose);
			
			jsonString = obj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonString;
	}
	
}
