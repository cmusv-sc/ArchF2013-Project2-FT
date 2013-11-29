package models.dao;

import java.util.List;

import models.SensorCategory;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class SensorCategoryDaoImplementation implements SensorCategoryDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	@Override
	public boolean addSensorCategory(String sensorCategoryName, String purpose) {
		// TODO need to use this in production for SAP HANA
		final String SQL_SEQUENCE = "SELECT CMU.COURSE_SENSOR_CATEGORY_ID_SEQ.NEXTVAL FROM DUMMY";
		int sensorCategoryId = simpleJdbcTemplate.queryForInt(SQL_SEQUENCE);
		
		// TODO need to use this in production for SAP HANA
		final String SQL = "INSERT INTO CMU.COURSE_SENSOR_CATEGORY (SENSOR_CATEGORY_ID, SENSOR_CATEGORY_NAME, PURPOSE) VALUES (?, ?, ?)";
		
		// for test only
//		final String SQL = "INSERT INTO CMU.COURSE_SENSOR_CATEGORY (SENSOR_CATEGORY_ID, SENSOR_CATEGORY_NAME, PURPOSE) VALUES (next value for CMU.COURSE_SENSOR_CATEGORY_ID_SEQ, ?, ?)";
		try{
			// TODO need to use this in production for SAP HANA
			simpleJdbcTemplate.update(SQL, sensorCategoryId, sensorCategoryName, purpose);
			// for test only
//			simpleJdbcTemplate.update(SQL, sensorCategoryName, purpose);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean updateSensorCategory(String sensorCategoryName, String purpose) {
		final String SQL = "UPDATE CMU.COURSE_SENSOR_CATEGORY " 
				+ "SET PURPOSE = ? "
				+ "WHERE SENSOR_CATEGORY_NAME = ?";
		try{
			simpleJdbcTemplate.update(SQL, purpose, sensorCategoryName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public List<SensorCategory> getAllSensorCategories() {
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR_CATEGORY";
		
		List<SensorCategory> sensorCategories = simpleJdbcTemplate.query(SQL, ParameterizedBeanPropertyRowMapper.newInstance(SensorCategory.class));
		
		return sensorCategories;
	}

	@Override
	public SensorCategory getSensorCategory(String SensorCategoryName) {
		
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR_CATEGORY WHERE SENSOR_CATEGORY_NAME = ?";
		
		SensorCategory sensorCategory = simpleJdbcTemplate.queryForObject(SQL, ParameterizedBeanPropertyRowMapper.newInstance(SensorCategory.class), SensorCategoryName);
		
		return sensorCategory;
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
