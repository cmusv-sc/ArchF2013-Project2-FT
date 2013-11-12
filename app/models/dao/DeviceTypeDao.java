package models.dao;

import java.util.List;

import models.DeviceType;

public interface DeviceTypeDao {
	public void addDeviceType(String deviceTypeName, String manufacturer, String version, String userDefinedFields, List<String> sensorTypes);
	public List<DeviceType> getAllDeviceTypes();
	public DeviceType getDeviceType(String deviceTypeName);
}
