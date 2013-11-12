package models.dao;

import java.util.List;

import models.Device;

public interface DeviceDao {
	public void addDevice(String deviceTypeName, String uri, String userDefinedFields, double longitude, double latitude, double altitude, String representation);
	public List<Device> getAllDevices();
	public Device getDevice(String uri);
}
