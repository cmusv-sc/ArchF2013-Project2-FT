package models;


public class SensorType extends SensorCategory{
	private String sensorTypeName;
	private String manufacturer;
	private String version;
	private double maximumValue;
	private double minimumValue;
	private String unit;
	private String interpreter;
	private String sensorTypeUserDefinedFields;
	
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
	public double getMaximumValue() {
		return maximumValue;
	}
//	MAX_VALUE/MIN_VALUE is used in the database, thus the setters have to be named so, instead of setMaximumValue()
	public void setMaxValue(double maxValue) {
		this.maximumValue = maxValue;
	}
	public double getMinimumValue() {
		return minimumValue;
	}
	public void setMinValue(double minValue) {
		this.minimumValue = minValue;
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
	public String getSensorTypeUserDefinedFields() {
		return sensorTypeUserDefinedFields;
	}
	public void setSensorTypeUserDefinedFields(String userDefinedFields) {
		this.sensorTypeUserDefinedFields = userDefinedFields;
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
	
//	No more used
//	public String toCSVString() {
//		String csvString = new String();
//		csvString += sensorTypeName;
//		csvString += ",";
//		csvString += manufacturer;
//		csvString += ",";
//		csvString += version;
//		csvString += ",";
//		csvString += Double.toString(maximumValue);
//		csvString += ",";
//		csvString += Double.toString(minimumValue);
//		csvString += ",";
//		csvString += unit;
//		csvString += ",";
//		csvString += interpreter;
//		csvString += ",";
//		csvString += sensorTypeUserDefinedFields;
//		return csvString;
//	}
//	
//	public String getCSVHeader() {
//		return "sensor_type_name,manufacturer,version,maximum_value,minimum_value,unit,interpreter,user_defined_fields, sensor_category_name";
//	}
//	
//	public String toJSONString() {
//		String jsonString = new String();
//		try {
//			JSONObject obj=new JSONObject();
//			obj.put("sensor_type_name",  sensorTypeName);
//			obj.put("manufacturer", manufacturer);
//			obj.put("version", version);
//			obj.put("maximum_value", maximumValue);
//			obj.put("minimum_value", minimumValue);
//			obj.put("unit", unit);
//			obj.put("interpreter", interpreter);
//			obj.put("user_defined_fields", sensorTypeUserDefinedFields);
//			jsonString = obj.toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return jsonString;
//	}
}
