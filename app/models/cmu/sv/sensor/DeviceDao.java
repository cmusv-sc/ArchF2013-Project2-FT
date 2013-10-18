package models.cmu.sv.sensor;

import java.util.ArrayList;
import java.util.List;

public interface DeviceDao {
	
	public List<Device> getAllDevices();
	
	public ArrayList<String> getSensorType(String deviceType);
}
