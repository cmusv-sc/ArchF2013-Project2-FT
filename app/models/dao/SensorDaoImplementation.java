package models.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import models.Sensor;

public class SensorDaoImplementation implements SensorDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	@Override
	public void addSensor(String sensorName, String sensorTypeName,
			String deviceUri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Sensor getSensor(String sensorName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sensor> getAllSensors() {
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
