package models;

public class Sensor {
	private String sensorName;
	private String userDefinedFields;
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
}
