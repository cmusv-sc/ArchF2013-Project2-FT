package models.dao;

import java.util.List;

import models.SensorReading;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class SensorReadingDaoImplementation implements SensorReadingDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	public void setSimpleJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
		this.simpleJdbcTemplate = jdbcTemplate;
	}

	@Override
	public SensorReading searchReading(String deviceId, Long timeStamp, String sensorType) {
		final String SQL = "SELECT * FROM CMU.CMU_SENSOR WHERE deviceID=? AND timeStamp<=? AND sensorType=? ORDER BY timeStamp DESC LIMIT 1";
		SensorReading sensorReading = simpleJdbcTemplate.queryForObject(SQL, ParameterizedBeanPropertyRowMapper.newInstance(SensorReading.class), deviceId, timeStamp, sensorType);
		return sensorReading;
	}

	@Override
	public boolean addReading(String deviceId, Long timeStamp, String sensorType, Double value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SensorReading> searchReading(String deviceId, Long startTime, Long endTime, String sensorType) {
		final String SQL = "SELECT TIMESTAMP, VALUE FROM CMU.CMU_SENSOR" 
				+ " WHERE DEVICEID = ? AND TIMESTAMP >= ? AND TIMESTAMP <= ? AND SENSORTYPE = ? ORDER BY TIMESTAMP DESC";
		List<SensorReading> sensorReadings = simpleJdbcTemplate.query(SQL, ParameterizedBeanPropertyRowMapper.newInstance(SensorReading.class), deviceId, startTime, endTime, sensorType);
		return sensorReadings;
	}

	@Override
	public List<SensorReading> lastReadingFromAllDevices(Long timeStamp, String sensorType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SensorReading> lastestReadingFromAllDevices(String sensorType) {
//		final String SQL = "SELECT DEVICEID, TIMESTAMP, VALUE FROM " +
//			"(SELECT * FROM CMU.CMU_SENSOR AS a " + 
//			"INNER JOIN " +
//			"(SELECT " +
//			"DEVICEID as device_id," +
//			"max(TIMESTAMP) as max_timestamp " +
//			"FROM CMU.CMU_SENSOR " +
//			"WHERE SENSORTYPE = ? " + // 1st parameter - sensorType
//			"GROUP BY DEVICEID" +
//			") b "+
//			"ON a.DEVICEID = b.device_id AND a.TIMESTAMP = b.max_timestamp WHERE a.SENSORTYPE = ?)";
		
		final String SQL ="SELECT \"DEVICEID\", \"TIMESTAMP\", \"VALUE\" FROM " +
        "(SELECT * FROM \"CMU\".\"CMU_SENSOR\" AS a " + 
        "INNER JOIN " +
        "(SELECT " +
        "\"DEVICEID\" as device_id," +
        "max(\"TIMESTAMP\") as max_timestamp " +
        "FROM \"CMU\".\"CMU_SENSOR\" " +
        "WHERE \"SENSORTYPE\" = ? " + // 1st parameter - sensorType
        "GROUP BY \"DEVICEID\"" +
        ") b "+
        "ON "+
        "a.\"DEVICEID\" = b.device_id AND " +
        "a.\"TIMESTAMP\" = b.max_timestamp " +
        "WHERE a.\"SENSORTYPE\" = ?)";
		
		List<SensorReading> sensorReadings = simpleJdbcTemplate.query(SQL, ParameterizedBeanPropertyRowMapper.newInstance(SensorReading.class), sensorType, sensorType);
		return sensorReadings;
	}

	

}
