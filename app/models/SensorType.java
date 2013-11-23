package models;

import org.json.JSONException;
import org.json.JSONObject;

public class SensorType {
	private String sensorTypeName;
	private String manufacturer;
	private String version;
	private double maxValue;
	private double minValue;
	private String unit;
	private String interpreter;
	private String userDefinedFields;
	private String sensorCategoryName;
	
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
	public double getMinValue() {
		return minValue;
	}
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getInterpreter() {
		return interpreter;
	}
	public void setInterpreter(String interpreter) {
		this.interpreter = interpreter;
	}
	public String getUserDefinedFields() {
		return userDefinedFields;
	}
	public void setUserDefinedFields(String userDefinedFields) {
		this.userDefinedFields = userDefinedFields;
	}
	public String getSensorTypeName() {
		return sensorTypeName;
	}
	public void setSensorTypeName(String sensorTypeName) {
		this.sensorTypeName = sensorTypeName;
	}
	
	public String getSensorCategoryName() {
		return sensorCategoryName;
	}
	public void setSensorCategoryName(String categoryName) {
		this.sensorCategoryName = categoryName;
	}
	
	public String toCSVString() {
		String csvString = new String();
		csvString += sensorTypeName;
		csvString += ",";
		csvString += manufacturer;
		csvString += ",";
		csvString += version;
		csvString += ",";
		csvString += Double.toString(maxValue);
		csvString += ",";
		csvString += Double.toString(minValue);
		csvString += ",";
		csvString += unit;
		csvString += ",";
		csvString += interpreter;
		csvString += ",";
		csvString += userDefinedFields;
		csvString += ",";
		csvString += sensorCategoryName;
		return csvString;
	}
	
	public static String getCSVHeader() {
		return "sensor_type_name,manufacturer,version,maximum_value,minimum_value,unit,interpreter,user_defined_fields, sensor_category_name";
	}
	
	public String toJSONString() {
		String jsonString = new String();
		try {
			JSONObject obj=new JSONObject();
			obj.put("sensor_type_name",  sensorTypeName);
			obj.put("manufacturer", manufacturer);
			obj.put("version", version);
			obj.put("maximum_value", maxValue);
			obj.put("minimum_value", minValue);
			obj.put("unit", unit);
			obj.put("interpreter", interpreter);
			obj.put("user_defined_fields", userDefinedFields);
			obj.put("sensor_category_name", sensorCategoryName);
			jsonString = obj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonString;
	}
	
}
