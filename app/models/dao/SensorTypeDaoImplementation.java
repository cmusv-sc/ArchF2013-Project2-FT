package models.dao;

import java.util.ArrayList;
import java.util.List;

import models.SensorCategory;
import models.SensorType;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class SensorTypeDaoImplementation implements SensorTypeDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public boolean addSensorType(String sensorTypeName,
			String manufacturer, String version, double maxValue,
			double minValue, String unit, String interpreter,
			String userDefinedFields, String sensorCategoryName) {
//		Check if SensorTypeName is duplicate
		final String SQL_CHECK = "SELECT * FROM CMU.COURSE_SENSOR_TYPE WHERE SENSOR_TYPE_NAME=?";
		List<SensorType> types = simpleJdbcTemplate.query(SQL_CHECK, 
				ParameterizedBeanPropertyRowMapper.newInstance(SensorType.class), 
				sensorTypeName);
		if(types.size() > 0){
			return false;
		}
		
//		TODO: Need to use this in production for SAP HANA
		final String SQL_SEQUENCE = "SELECT CMU.COURSE_SENSOR_TYPE_ID_SEQ.NEXTVAL FROM DUMMY";
		int sensorTypeId = simpleJdbcTemplate.queryForInt(SQL_SEQUENCE);
		
//		TODO: Need to use this in production for SAP HANA
		int temporarySensorCategoryId = -1;
		final String SQL = "INSERT INTO CMU.COURSE_SENSOR_TYPE (SENSOR_TYPE_ID, SENSOR_TYPE_NAME, MANUFACTURER, VERSION, MAX_VALUE, MIN_VALUE, UNIT, INTERPRETER, USER_DEFINED_FIELDS, SENSOR_CATEGORY_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";		
//		Test Only
//		final String SQL = "INSERT INTO CMU.COURSE_SENSOR_TYPE (SENSOR_TYPE_ID, SENSOR_TYPE_NAME, MANUFACTURER, VERSION, MAX_VALUE, MIN_VALUE, UNIT, INTERPRETER, USER_DEFINED_FIELDS, SENSOR_CATEGORY_ID) VALUES (NEXT VALUE FOR CMU.COURSE_SENSOR_TYPE_ID_SEQ, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try{
			
//			TODO: Need to use this in production for SAP HANA
			simpleJdbcTemplate.update(SQL, sensorTypeId, sensorTypeName, 
					manufacturer, version, maxValue, minValue, unit, 
					interpreter, userDefinedFields, temporarySensorCategoryId);

//			Test Only
//			simpleJdbcTemplate.update(SQL, sensorTypeName, 
//					manufacturer, version, maxValue, minValue, unit, 
//					interpreter, userDefinedFields, sensorCategoryId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
//		TODO: Set the SensorCategoryId by SensorCategoryName
		int sensorCategoryId = 0;
		final String SQL_FIND_CATEGORY_ID =
				"SELECT SENSOR_CATEGORY_ID"
				+ "FROM CMU.COURSE_SENSOR_CATEGORY "
				+ "WHERE SENSOR_CATEGORY_NAME = ?";
		final String SQL_UPDATE_CATEGORY_ID =
				"UPDATE CMU.COURSE_SENSOR_TYPE "
						+ "SET SENSOR_CATEGORY_ID = ? "
						+ "WHERE SENSOR_TYPE_NAME = ?";
		List<Object[]> params = new ArrayList<Object[]>();
		params.add(new Object[] {sensorCategoryId});
		try{
			sensorCategoryId = simpleJdbcTemplate.queryForInt(SQL_FIND_CATEGORY_ID, sensorCategoryName);
			simpleJdbcTemplate.batchUpdate(SQL_UPDATE_CATEGORY_ID, params);
		}catch(Exception e){
			return false;
		}
		
		return true;
	}

	@Override
	public SensorType getSensorType(String sensorTypeName) {
		List<SensorType> types = getAllSensorTypes();
		
		return (types.size() == 0)? null : types.get(0);
	}

	@Override
	public List<SensorType> getAllSensorTypes() {
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR_TYPE";
		List<SensorType> sensorType = simpleJdbcTemplate.query(SQL, 
				ParameterizedBeanPropertyRowMapper.newInstance(SensorType.class));

//		TODO: Get SensorCategoryName by its ID
		
		return sensorType;
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
