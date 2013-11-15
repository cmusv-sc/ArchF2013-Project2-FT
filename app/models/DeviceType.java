package models;

import java.util.List;

public class DeviceType {
	private String deviceTypeName;
	private String manufacturer;
	private String version;
	private String userDefinedFields;
	private List<String> sensorTypes;
	public String getDeviceTypeName() {
		return deviceTypeName;
	}
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
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
	public String getUserDefinedFields() {
		return userDefinedFields;
	}
	public void setUserDefinedFields(String userDefinedFields) {
		this.userDefinedFields = userDefinedFields;
	}
	public List<String> getSensorTypes() {
		return sensorTypes;
	}
	public void setSensorTypes(List<String> sensorTypes) {
		this.sensorTypes = sensorTypes;
	}

}
