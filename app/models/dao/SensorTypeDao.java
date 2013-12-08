package models.dao;

import java.util.List;

import models.SensorType;

public interface SensorTypeDao {
	public boolean addSensorType(String sensorTypeName, String manufacturer, String version, double maxValue, double minValue, String unit, String interpreter, String userDefinedFields, String sensorCategoryName);
	public boolean updateSensorType(String sensorTypeName, String userDefinedFields);
	public SensorType getSensorType(String sensorTypeName);
	public List<SensorType> getAllSensorTypes();
	public boolean deleteSensorType(String sensorTypeName);
}