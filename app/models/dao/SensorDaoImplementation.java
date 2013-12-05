package models.dao;

import java.util.List;

import models.Sensor;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class SensorDaoImplementation implements SensorDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	@Override
	public boolean addSensor(String sensorTypeName, String deviceUri, String sensorName, String sensorUserDefinedFields) {
		final String GETSENSORTYPEID = "select sensor_type_id from cmu.course_sensor_type where sensor_type_name = ?";
		int sensorTypeId = simpleJdbcTemplate.queryForInt(GETSENSORTYPEID, sensorTypeName);
		
		final String GETDEVICEID = "select device_id from cmu.course_device where uri = ?";
		int deviceId = simpleJdbcTemplate.queryForInt(GETDEVICEID, deviceUri);
		
		final String SQL = "INSERT INTO CMU.COURSE_SENSOR (SENSOR_ID, SENSOR_TYPE_ID, DEVICE_ID, SENSOR_NAME, SENSOR_USER_DEFINED_FIELDS) VALUES (CMU.COURSE_SENSOR_ID_SEQ.NEXTVAL, ?, ?, ?, ?)";
		try{
//			TODO: Need to use this in production for SAP HANA
			simpleJdbcTemplate.update(SQL, sensorTypeId, deviceId, sensorName, sensorUserDefinedFields);
//			Test Only
//			simpleJdbcTemplate.update(SQL, sensorTypeId, deviceId, sensorName, sensorUserDefinedFields);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;		
	}
	
	@Override
	public boolean updateSensor(String sensorTypeName, String deviceUri, String sensorName, String sensorUserDefinedFields) {
		final String GETSENSORTYPEID = "select sensor_type_id from cmu.course_sensor_type where sensor_type_name = ?";
		int sensorTypeId = simpleJdbcTemplate.queryForInt(GETSENSORTYPEID, sensorTypeName);
		
		final String GETDEVICEID = "select device_id from cmu.course_device where uri = ?";
		int deviceId = simpleJdbcTemplate.queryForInt(GETDEVICEID, deviceUri);
		
		final String SQL = "UPDATE CMU.COURSE_SENSOR " 
				+ "SET SENSOR_TYPE_ID = ?, DEVICE_ID = ?, SENSOR_USER_DEFINED_FIELDS = ? " 
				+ "WHERE SENSOR_NAME = ?";	
		try{
			simpleJdbcTemplate.update(SQL, sensorTypeId, deviceId, sensorUserDefinedFields, sensorName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;		
	}

	@Override
	public Sensor getSensor(String sensorName) {
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR s, CMU.COURSE_SENSOR_TYPE st, CMU.COURSE_SENSOR_CATEGORY sc WHERE s.sensor_name = ? and s.sensor_type_id = st.sensor_type_id and st.sensor_category_id = sc.sensor_category_id";
		final String SQL_GET_DEVICE_ID = "SELECT DEVICE_ID FROM CMU.COURSE_SENSOR WHERE SENSOR_NAME = ?";
		final String SQL_GET_DEVICE_URI = "SELECT URI FROM CMU.COURSE_DEVICE WHERE DEVICE_ID = ?";
		
		try{
//			Get sensors
			List<Sensor> sensors = simpleJdbcTemplate.query(SQL, 
					ParameterizedBeanPropertyRowMapper.newInstance(Sensor.class), 
					sensorName);
			if(sensors.size() == 0) return null;
			
//			Set Sensor.deviceUri by it Device ID
			Sensor s = sensors.get(0);
			int deviceId = simpleJdbcTemplate.queryForInt(SQL_GET_DEVICE_ID, s.getSensorName());
			String uri = simpleJdbcTemplate.queryForObject(SQL_GET_DEVICE_URI, 
					String.class, deviceId);
			s.setDeviceUri(uri);
			return s;
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<Sensor> getAllSensors() {
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR s, CMU.COURSE_SENSOR_TYPE st, CMU.COURSE_SENSOR_CATEGORY sc WHERE s.sensor_type_id = st.sensor_type_id and st.sensor_category_id = sc.sensor_category_id";
		final String SQL_GET_DEVICE_ID = "SELECT DEVICE_ID FROM CMU.COURSE_SENSOR WHERE SENSOR_NAME = ?";
		final String SQL_GET_DEVICE_URI = "SELECT URI FROM CMU.COURSE_DEVICE WHERE DEVICE_ID = ?";
		try{
			List<Sensor> sensors = simpleJdbcTemplate.query(SQL, 
					ParameterizedBeanPropertyRowMapper.newInstance(Sensor.class));
			
//			Set Sensor.deviceUri by it Device ID
			for(Sensor s : sensors){
				int deviceId = simpleJdbcTemplate.queryForInt(SQL_GET_DEVICE_ID, s.getSensorName());
				String uri = simpleJdbcTemplate.queryForObject(SQL_GET_DEVICE_URI, 
						String.class, deviceId);
				s.setDeviceUri(uri);
			}
			
			return sensors;
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public boolean deleteSensor(String sensorName) {
		final String SQL_DELETE_SENSOR = "DELETE FROM CMU.COURSE_SENSOR "
						+ "WHERE SENSOR_NAME = ?";
		try{
			simpleJdbcTemplate.update(SQL_DELETE_SENSOR, sensorName);
		}catch(Exception e){
			return false;
		}
		return true;
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
