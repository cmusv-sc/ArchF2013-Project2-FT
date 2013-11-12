package models.dao;

import java.util.List;

import models.OldDevice;

public interface OldDeviceDao {
	
	public List<OldDevice> getAllDevices();
	
	public List<String> getSensorType(String deviceType);
}
