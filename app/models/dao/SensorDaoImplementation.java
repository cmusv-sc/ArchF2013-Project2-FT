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
//			simpleJdbcTemplate.update(SQL, sensorId, sensorTypeId, deviceId, sensorName, userDefinedFields);

//			Test Only
			simpleJdbcTemplate.update(SQL, sensorTypeId, deviceId, sensorName, sensorUserDefinedFields);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;		
	}

	@Override
	public Sensor getSensor(String sensorName) {
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR s, CMU.COURSE_SENSOR_TYPE st, CMU.COURSE_SENSOR_CATEGORY sc WHERE s.sensor_name = ? and s.sensor_type_id = st.sensor_type_id and st.sensor_category_id = sc.sensor_category_id";
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
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR s, CMU.COURSE_SENSOR_TYPE st, CMU.COURSE_SENSOR_CATEGORY sc WHERE s.sensor_type_id = st.sensor_type_id and st.sensor_category_id = sc.sensor_category_id";
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
