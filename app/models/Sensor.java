package models;


public class Sensor extends SensorType{
	private String sensorName;
	private String sensorUserDefinedFields;
	
	public String getSensorName() {
		return sensorName;
	}
	public void setSensorName(String printName) {
		this.sensorName = printName;
	}
	public String getSensorUserDefinedFields() {
		return sensorUserDefinedFields;
	}
	public void setSensorUserDefinedFields(String userDefinedFields) {
		this.sensorUserDefinedFields = userDefinedFields;
	}
	
//	public String toJSONString() {
//		String sensorTypeJsonString = super.toJSONString();
//		String jsonString = new String();
//		try {
//			JSONObject sensorTypeJsonObj = new JSONObject(sensorTypeJsonString);
//			
//			JSONObject obj=new JSONObject();
//			obj.put("sensor_name",  sensorName);
//			obj.put("user_defined_fields", sensorUserDefinedFields);
//			Iterator it = sensorTypeJsonObj.keys();
//			while (it.hasNext()) {
//				String key = (String)it.next();
//				obj.put(key, sensorTypeJsonObj.get(key));
//			}
//			jsonString = obj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return jsonString;
//	}
}
