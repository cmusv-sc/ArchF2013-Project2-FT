package models.dao;

import java.util.List;

import models.SensorReading;


public interface SensorReadingDao {
	
	public SensorReading searchReading(String sensorName, Long timeStamp);
	
	public boolean addReading( String sensorName, Boolean isIndoor, long timeStamp, String value, Double longitude, Double latitude, Double altitude, String locationInterpreter);

	public List<SensorReading> searchReading(String sensorName, Long startTime, Long endTime);
	
	public List<SensorReading> lastReadingFromAllDevices(Long timeStamp, String sensorType);

	public List<SensorReading> lastestReadingFromAllDevices(String sensorType);

}
