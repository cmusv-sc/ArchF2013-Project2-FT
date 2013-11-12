package models.dao;

import java.util.List;

import models.Sensor;

public interface SensorDao {
	public void addSensor(String sensorName, String sensorTypeName, String deviceUri);
	public Sensor getSensor(String sensorName);
	public List<Sensor> getAllSensors();
}
