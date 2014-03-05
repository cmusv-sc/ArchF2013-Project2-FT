/*******************************************************************************
 * Copyright (c) 2013 Carnegie Mellon University Silicon Valley. 
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available
 * under the terms of dual licensing(GPL V2 for Research/Education
 * purposes). GNU Public License v2.0 which accompanies this distribution
 * is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * 
 * Please contact http://www.cmu.edu/silicon-valley/ if you have any 
 * questions.
 ******************************************************************************/
package models.dao;

import java.util.List;

import models.OldSensorReading;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class OldSensorReadingDaoImplementation implements OldSensorReadingDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	public void setSimpleJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
		this.simpleJdbcTemplate = jdbcTemplate;
	}

	@Override
	public OldSensorReading searchReading(String deviceId, Long timeStamp, String sensorType) {
		final String SQL = "SELECT * FROM CMU.CMU_SENSOR WHERE deviceID=? AND timeStamp<=? AND sensorType=? ORDER BY timeStamp DESC LIMIT 1";
		OldSensorReading sensorReading = simpleJdbcTemplate.queryForObject(SQL, ParameterizedBeanPropertyRowMapper.newInstance(OldSensorReading.class), deviceId, timeStamp, sensorType);
		return sensorReading;
	}

	@Override
	public boolean addReading(String deviceId, Long timeStamp, String sensorType, Double value) {
		final String SQL = "INSERT INTO CMU.CMU_SENSOR (DEVICEID, TIMESTAMP, SENSORTYPE, VALUE)" 
				+ "VALUES ('" + deviceId + "','" + timeStamp.toString() + "','" + sensorType + "','" + value.toString() + "')";
		try{
			simpleJdbcTemplate.update(SQL);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public List<OldSensorReading> searchReading(String deviceId, Long startTime, Long endTime, String sensorType) {
		final String SQL = "SELECT DEVICEID, TIMESTAMP, SENSORTYPE, VALUE FROM CMU.CMU_SENSOR" 
				+ " WHERE DEVICEID = ? AND TIMESTAMP >= ? AND TIMESTAMP <= ? AND SENSORTYPE = ? ORDER BY TIMESTAMP DESC";
		List<OldSensorReading> sensorReadings = simpleJdbcTemplate.query(SQL, ParameterizedBeanPropertyRowMapper.newInstance(OldSensorReading.class), deviceId, startTime, endTime, sensorType);
		return sensorReadings;
	}

	@Override
	public List<OldSensorReading> lastReadingFromAllDevices(Long timeStamp, String sensorType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OldSensorReading> latestReadingFromAllDevices(String sensorType) {
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
		
		final String SQL ="SELECT \"DEVICEID\", \"TIMESTAMP\", \"SENSORTYPE\", \"VALUE\" FROM " +
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
		
		List<OldSensorReading> sensorReadings = simpleJdbcTemplate.query(SQL, ParameterizedBeanPropertyRowMapper.newInstance(OldSensorReading.class), sensorType, sensorType);
		return sensorReadings;
	}

	

}
