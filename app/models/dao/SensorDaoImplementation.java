package models.dao;

import java.util.List;

import models.Sensor;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class SensorDaoImplementation implements SensorDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	@Override
	public boolean addSensor(int sensorTypeId, int deviceId, String sensorName, String userDefinedFields) {
//		TODO: Need to use this in production for SAP HANA
//		final String SQL_SEQUENCE = "SELECT CMU.COURSE_SENSOR_ID_SEQ.NEXTVAL FROM DUMMY";
//		int sensorId = simpleJdbcTemplate.queryForInt(SQL_SEQUENCE);
		      
		
//		Check if SensorName is duplicate
		final String SQL_CHECK = "SELECT * FROM CMU.COURSE_SENSOR WHERE SENSOR_NAME=?";
		List<Sensor> sensors = simpleJdbcTemplate.query(SQL_CHECK, 
				ParameterizedBeanPropertyRowMapper.newInstance(Sensor.class), 
				sensorName);
		if(sensors.size() > 0){
			return false;
		}
		
//		TODO: Need to use this in production for SAP HANA
//		final String SQL = "INSERT INTO CMU.COURSE_SENSOR (SENSOR_ID, SENSOR_TYPE_ID, DEVICE_ID, SENSOR_NAME, USER_DEFINED_FIELDS) VALUES (?, ?, ?, ?, ?)";		
//		Test Only
		final String SQL = "INSERT INTO CMU.COURSE_SENSOR (SENSOR_ID, SENSOR_TYPE_ID, DEVICE_ID, SENSOR_NAME, USER_DEFINED_FIELDS) VALUES (NEXT VALUE FOR CMU.COURSE_SENSOR_ID_SEQ, ?, ?, ?, ?)";
		try{
//			TODO: Need to use this in production for SAP HANA
//			simpleJdbcTemplate.update(SQL, sensorId, sensorTypeId, deviceId, sensorName, userDefinedFields);

//			Test Only
			simpleJdbcTemplate.update(SQL, sensorTypeId, deviceId, sensorName, userDefinedFields);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;		
	}

	@Override
	public Sensor getSensor(String sensorName) {
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR WHERE SENSOR_NAME=?";
		Sensor sensor = new Sensor();
		
		List<Sensor> sensors = simpleJdbcTemplate.query(SQL, 
				ParameterizedBeanPropertyRowMapper.newInstance(Sensor.class), 
				sensorName);
		if(sensors.size() > 0){
//			Returns the first item if duplicate SensorType exists, it should never happen
			sensor = sensors.get(0);
		}
		return sensor;
	}

	@Override
	public List<Sensor> getAllSensors() {
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR";
		List<Sensor> sensors = simpleJdbcTemplate.query(SQL, 
				ParameterizedBeanPropertyRowMapper.newInstance(Sensor.class));
		return sensors;
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
