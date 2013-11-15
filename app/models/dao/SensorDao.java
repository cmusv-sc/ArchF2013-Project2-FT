package models.dao;

import java.util.List;

import models.Sensor;

public interface SensorDao {
	public boolean addSensor(int sensorTypeId, int deviceId, String sensorName, String userDefinedFields);
	public Sensor getSensor(String sensorName);
	public List<Sensor> getAllSensors();
}
