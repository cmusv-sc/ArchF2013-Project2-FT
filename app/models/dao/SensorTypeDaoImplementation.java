package models.dao;

import java.util.List;

import models.SensorType;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class SensorTypeDaoImplementation implements SensorTypeDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public boolean addSensorType(int sensorCategoryId, String sensorTypeName,
			String manufacturer, String version, double maxValue,
			double minValue, String unit, String interpreter,
			String userDefinedFields) {
//		TODO: Need to use this in production for SAP HANA
//		final String SQL_SEQUENCE = "SELECT CMU.COURSE_SENSOR_TYPE_ID_SEQ.NEXTVAL FROM DUMMY";
//		int sensorTypeId = simpleJdbcTemplate.queryForInt(SQL_SEQUENCE);
		      
		
//		Check if SensorTypeName is duplicate
		final String SQL_CHECK = "SELECT * FROM CMU.COURSE_SENSOR_TYPE WHERE SENSOR_TYPE_NAME=?";
		List<SensorType> types = simpleJdbcTemplate.query(SQL_CHECK, 
				ParameterizedBeanPropertyRowMapper.newInstance(SensorType.class), 
				sensorTypeName);
		if(types.size() > 0){
			return false;
		}
		
//		TODO: Need to use this in production for SAP HANA
//		final String SQL = "INSERT INTO CMU.COURSE_SENSOR_TYPE (SENSOR_TYPE_ID, SENSOR_TYPE_NAME, MANUFACTURER, VERSION, MAX_VALUE, MIN_VALUE, UNIT, INTERPRETER, USER_DEFINED_FIELDS, SENSOR_CATEGORY_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";		
//		Test Only
		final String SQL = "INSERT INTO CMU.COURSE_SENSOR_TYPE (SENSOR_TYPE_ID, SENSOR_TYPE_NAME, MANUFACTURER, VERSION, MAX_VALUE, MIN_VALUE, UNIT, INTERPRETER, USER_DEFINED_FIELDS, SENSOR_CATEGORY_ID) VALUES (NEXT VALUE FOR CMU.COURSE_SENSOR_TYPE_ID_SEQ, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try{
			
//			TODO: Need to use this in production for SAP HANA
//			simpleJdbcTemplate.update(SQL, sensorTypeId, sensorTypeName, 
//					manufacturer, version, maxValue, minValue, unit, 
//					interpreter, userDefinedFields, sensorCategoryId);

//			Test Only
			simpleJdbcTemplate.update(SQL, sensorTypeName, 
					manufacturer, version, maxValue, minValue, unit, 
					interpreter, userDefinedFields, sensorCategoryId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public SensorType getSensorType(String sensorTypeName) {
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR_TYPE WHERE SENSOR_TYPE_NAME=?";
		SensorType sensorType = new SensorType();
		
		List<SensorType> types = simpleJdbcTemplate.query(SQL, 
				ParameterizedBeanPropertyRowMapper.newInstance(SensorType.class), 
				sensorTypeName);
		if(types.size() > 0){
//			Returns the first item if duplicate SensorType exists, it should never happen
			sensorType = types.get(0);
		}
		return sensorType;
	}

	@Override
	public List<SensorType> getAllSensorTypes() {
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR_TYPE";
		List<SensorType> sensorType = simpleJdbcTemplate.query(SQL, 
				ParameterizedBeanPropertyRowMapper.newInstance(SensorType.class));
		return sensorType;
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}