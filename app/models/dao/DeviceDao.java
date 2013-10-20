package models.dao;

import java.util.List;

import models.Device;

public interface DeviceDao {
	
	public List<Device> getAllDevices();
	
	public List<String> getSensorType(String deviceType);
}
