package models.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import models.SensorCategory;

public class SensorCategoryDaoImplementation implements SensorCategoryDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	@Override
	public void addSensorCategory(String sensorCategoryName, String purpose) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SensorCategory> getAllSensorCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SensorCategory getSensorCategory(String SensorCategoryName) {
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
