package models;

public class Sensor {
	private String sensorName;
	private String userDefinedFields;
	private int sensorTypeId;
	private int deviceId;
	
	public String getPrintName() {
		return sensorName;
	}
	public void setPrintName(String printName) {
		this.sensorName = printName;
	}
	public String getUserDefinedFields() {
		return userDefinedFields;
	}
	public void setUserDefinedFields(String userDefinedFields) {
		this.userDefinedFields = userDefinedFields;
	}
	
	public int getSensorTypeId(){
		return sensorTypeId;
	}
	
	public void setSensorTypeId(int typeId){
		this.sensorTypeId = typeId;
	}
	
	public int getDeviceId(){
		return deviceId;
	}
	
	public void setDeviceId(int id){
		this.deviceId = id;
	}
}
