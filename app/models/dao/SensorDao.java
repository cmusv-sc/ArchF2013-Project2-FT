package models.dao;

import java.util.List;

import models.Sensor;

public interface SensorDao {
	public boolean addSensor(String sensorTypeName, String deviceUri, String sensorName, String userDefinedFields);
	public boolean updateSensor(String sensorTypeName, String deviceUri, String sensorName, String userDefinedFields);
	public boolean addSensor(String sensorTypeName, String deviceUri, String sensorName, String userDefinedFields, String userName);
	public Sensor getSensor(String sensorName);
	public Sensor getSensor(String sensorName, String userName);
	public List<Sensor> getAllSensors();
	public List<Sensor> getAllSensors(String userName);
	public boolean deleteSensor(String sensorName);
}
