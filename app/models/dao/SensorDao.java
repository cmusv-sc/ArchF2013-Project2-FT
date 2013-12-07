package models.dao;

import java.util.List;

import models.Sensor;

public interface SensorDao {
	public boolean addSensor(String sensorTypeName, String deviceUrl, String sensorName, String userDefinedFields);
	public boolean updateSensor(String sensorName, String userDefinedFields);
	public Sensor getSensor(String sensorName);
	public List<Sensor> getAllSensors();
	public boolean deleteSensor(String sensorName);
}
