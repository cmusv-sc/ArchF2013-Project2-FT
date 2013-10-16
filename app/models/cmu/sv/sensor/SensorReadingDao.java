package models.cmu.sv.sensor;

import java.util.ArrayList;
import java.util.List;

public interface SensorReadingDao {
	
	public SensorReading searchReading(String deviceId, Long timeStamp, String sensorType);
	
	public boolean addReading(String deviceId, Long timeStamp, String sensorType, double value);

	public ArrayList<SensorReading> searchReading(String deviceId, Long startTime, long endTime, String sensorType);
	
	public ArrayList<SensorReading> lastReadingFromAllDevices(Long timeStamp, String sensorType);

		public ArrayList<SensorReading> lastestReadingFromAllDevices(String sensorType);

}
