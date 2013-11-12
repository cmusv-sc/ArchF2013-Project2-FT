package models.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import models.SensorType;

public class SensorTypeDaoImplementation implements SensorTypeDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public void addSensorType(String sensorCategoryName, String sensorTypeName,
			String manufacturer, String version, double maxValue,
			double minValue, String unit, String interpreter,
			String userDefinedFields) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SensorType getSensorType(String sensorTypeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SensorType> getAllSensorTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
