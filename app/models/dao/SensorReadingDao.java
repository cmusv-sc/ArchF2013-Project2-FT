package models.dao;

import java.util.List;

import models.SensorReading;


public interface SensorReadingDao {
	
	public SensorReading searchReading(String deviceId, Long timeStamp, String sensorType);
	
	public boolean addReading(String deviceId, Long timeStamp, String sensorType, Double value);

	public List<SensorReading> searchReading(String deviceId, Long startTime, Long endTime, String sensorType);
	
	public List<SensorReading> lastReadingFromAllDevices(Long timeStamp, String sensorType);

	public List<SensorReading> lastestReadingFromAllDevices(String sensorType);

}
