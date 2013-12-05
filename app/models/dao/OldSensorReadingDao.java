package models.dao;

import java.util.List;

import models.OldSensorReading;


public interface OldSensorReadingDao {
	
	public OldSensorReading searchReading(String deviceId, Long timeStamp, String sensorType);
	
	public boolean addReading(String deviceId, Long timeStamp, String sensorType, Double value);

	public List<OldSensorReading> searchReading(String deviceId, Long startTime, Long endTime, String sensorType);
	
	public List<OldSensorReading> lastReadingFromAllDevices(Long timeStamp, String sensorType);

	public List<OldSensorReading> lastestReadingFromAllDevices(String sensorType);

}
