package models;


public class Sensor extends SensorType{
	private String sensorName;
	private String sensorUserDefinedFields;
	private String deviceUri;
	
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
	public String getDeviceUri() {
		return deviceUri;
	}
	public void setDeviceUri(String deviceUri) {
		this.deviceUri = deviceUri;
	}
	
}
