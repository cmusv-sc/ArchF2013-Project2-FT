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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Device;
import models.SensorReading;

import org.apache.commons.lang.ArrayUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.google.protobuf.ServiceException;

public class SensorReadingDaoImplementation implements SensorReadingDao {
	private SimpleJdbcTemplate simpleJdbcTemplate;

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
		this.simpleJdbcTemplate = jdbcTemplate;
	}

	@Override
	public SensorReading searchReading(String sensorName, Long timeStamp) {
		final String SQL = "SELECT * FROM CMU.COURSE_DISCRETE_SENSOR_READING DSR, CMU.COURSE_SENSOR S WHERE DSR.SENSOR_ID=S.SENSOR_ID AND S.SENSOR_NAME=? AND DSR.timeStamp<=? ORDER BY timeStamp DESC LIMIT 1";
		try {
			SensorReading sensorReading = simpleJdbcTemplate.queryForObject(
					SQL, ParameterizedBeanPropertyRowMapper
							.newInstance(SensorReading.class), sensorName,
					new Timestamp(timeStamp));
			return sensorReading;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public SensorReading searchReading(String deviceUri, String sensorTypeName,
			Long timeStamp) {
		final String SQL = "SELECT * FROM CMU.COURSE_DISCRETE_SENSOR_READING DSR, CMU.COURSE_SENSOR S WHERE DSR.SENSOR_ID=S.SENSOR_ID AND S.SENSOR_NAME=? AND DSR.timeStamp<=? ORDER BY timeStamp DESC LIMIT 1";
		try {
			String sensorName = getSensorName(deviceUri, sensorTypeName);
			SensorReading sensorReading = simpleJdbcTemplate.queryForObject(
					SQL, ParameterizedBeanPropertyRowMapper
							.newInstance(SensorReading.class), sensorName,
					new Timestamp(timeStamp));
			return sensorReading;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean addReading(String sensorName, Boolean isIndoor,
			long timestamp, String value, Double longitude, Double latitude,
			Double altitude, String locationInterpreter) throws MasterNotRunningException, ZooKeeperConnectionException, ServiceException, IOException {
		Configuration config = HBaseConfiguration.create();
		String hbaseZookeeperQuorum="squirtle.sv.cmu.edu";
		int hbaseZookeeperClientPort= 2181;
		config.set("hbase.zookeeper.quorum", hbaseZookeeperQuorum);
		config.setInt("hbase.zookeeper.property.clientPort", hbaseZookeeperClientPort);
		HBaseAdmin.checkHBaseAvailable(config);


		HTable table = new HTable(config, "reading");
		insertData(config, table, "bo.com", "temprature", sensorName, timestamp, value, isIndoor, longitude, latitude, altitude, locationInterpreter);
		
		try {
			final String FETCH_SENSOR_ID = "SELECT SENSOR_ID FROM CMU.COURSE_SENSOR S WHERE S.SENSOR_NAME = ?";
			int sensorId = simpleJdbcTemplate.queryForInt(FETCH_SENSOR_ID,
					sensorName);

			final String SQL = "INSERT INTO CMU.COURSE_DISCRETE_SENSOR_READING (SENSOR_ID, IS_INDOOR, LOCATION_INTERPRETER, TIMESTAMP, VALUE, LONGITUDE, LATITUDE, ALTITUDE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			int num = simpleJdbcTemplate.update(SQL, sensorId,
					String.valueOf(isIndoor), locationInterpreter,
					new Timestamp(timestamp), value, longitude, latitude,
					altitude);
			if (num == 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<SensorReading> searchReading(String sensorName, Long startTime,
			Long endTime) {
		final String SQL = "SELECT SENSOR_NAME, IS_INDOOR, LOCATION_INTERPRETER, TIMESTAMP, VALUE, LONGITUDE, LATITUDE, ALTITUDE FROM CMU.COURSE_DISCRETE_SENSOR_READING DSR, CMU.COURSE_SENSOR S"
				+ " WHERE S.SENSOR_NAME = ? AND DSR.SENSOR_ID = S.SENSOR_ID AND TIMESTAMP >= ? AND TIMESTAMP <= ? ORDER BY TIMESTAMP DESC";
		try {
			List<SensorReading> sensorReadings = simpleJdbcTemplate.query(SQL,
					ParameterizedBeanPropertyRowMapper
							.newInstance(SensorReading.class), sensorName,
					new Timestamp(startTime), new Timestamp(endTime));
			return sensorReadings;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<SensorReading> searchReading(String deviceUri,
			String sensorTypeName, Long startTime, Long endTime) {
		final String SQL = "SELECT SENSOR_NAME, IS_INDOOR, LOCATION_INTERPRETER, TIMESTAMP, VALUE, LONGITUDE, LATITUDE, ALTITUDE FROM CMU.COURSE_DISCRETE_SENSOR_READING DSR, CMU.COURSE_SENSOR S"
				+ " WHERE S.SENSOR_NAME = ? AND DSR.SENSOR_ID = S.SENSOR_ID AND TIMESTAMP >= ? AND TIMESTAMP <= ? ORDER BY TIMESTAMP DESC";
		try {
			String sensorName = getSensorName(deviceUri, sensorTypeName);
			List<SensorReading> sensorReadings = simpleJdbcTemplate.query(SQL,
					ParameterizedBeanPropertyRowMapper
							.newInstance(SensorReading.class), sensorName,
					new Timestamp(startTime), new Timestamp(endTime));
			return sensorReadings;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<SensorReading> lastReadingFromAllDevices(Long endTime) {
		final String SQL2 = "SELECT SENSOR_NAME, IS_INDOOR, TIMESTAMP, LOCATION_INTERPRETER, VALUE, LONGITUDE, LATITUDE, ALTITUDE "+
				"FROM CMU.COURSE_SENSOR CS, CMU.COURSE_DISCRETE_SENSOR_READING SR "+
				"INNER JOIN "+
				"(SELECT DSR.SENSOR_ID AS ID, DSR.TIMESTAMP AS TMSTP "+
				 	"FROM CMU.COURSE_DISCRETE_SENSOR_READING DSR "+
				 	"WHERE DSR.TIMESTAMP <= ? AND DSR.TIMESTAMP >= ? "+
				 	") TMP  "+
				"ON SR.SENSOR_ID = TMP.ID AND SR.TIMESTAMP = TMP.TMSTP  "+
				"WHERE CS.SENSOR_ID = SR.SENSOR_ID";
		List<SensorReading> sensorReadings = simpleJdbcTemplate.query(SQL2,
				ParameterizedBeanPropertyRowMapper
						.newInstance(SensorReading.class), new Timestamp(endTime), new Timestamp(endTime-60000));
		return sensorReadings;
	}

	@Override
	public List<SensorReading> latestReadingFromAllDevicesBySensorType(
			String sensorType) {
		final String SQL2 = "SELECT SENSOR_NAME, IS_INDOOR, TIMESTAMP, LOCATION_INTERPRETER, VALUE, LONGITUDE, LATITUDE, ALTITUDE FROM CMU.COURSE_SENSOR CS, CMU.COURSE_DISCRETE_SENSOR_READING SR INNER JOIN (SELECT DSR.SENSOR_ID AS ID, MAX(DSR.TIMESTAMP) AS TMSTP FROM CMU.COURSE_DISCRETE_SENSOR_READING DSR, CMU.COURSE_SENSOR S, CMU.COURSE_DEVICE D, CMU.COURSE_DEVICE_TYPE_SENSOR_TYPE DTST, CMU.COURSE_SENSOR_TYPE ST WHERE DTST.SENSOR_TYPE_ID = ST.SENSOR_TYPE_ID AND ST.SENSOR_TYPE_NAME = ? AND D.DEVICE_TYPE_ID = DTST.DEVICE_TYPE_ID AND S.DEVICE_ID = D.DEVICE_ID AND S.SENSOR_TYPE_ID = ST.SENSOR_TYPE_ID AND DSR.SENSOR_ID = S.SENSOR_ID GROUP BY DSR.SENSOR_ID) TMP ON SR.SENSOR_ID = TMP.ID AND SR.TIMESTAMP = TMP.TMSTP WHERE CS.SENSOR_ID = SR.SENSOR_ID";

		List<SensorReading> sensorReadings = simpleJdbcTemplate.query(SQL2,
				ParameterizedBeanPropertyRowMapper
						.newInstance(SensorReading.class), sensorType);
		return sensorReadings;
	}

	private String getSensorName(String deviceUri, String sensorTypeName)
			throws Exception {
		final String GET_SENSOR_NAME = "SELECT S.SENSOR_NAME FROM CMU.COURSE_DEVICE D, CMU.COURSE_SENSOR S, CMU.COURSE_SENSOR_TYPE ST WHERE D.DEVICE_ID=S.DEVICE_ID AND S.SENSOR_TYPE_ID=ST.SENSOR_TYPE_ID AND D.URI=? AND ST.SENSOR_TYPE_NAME=?";
		return simpleJdbcTemplate.queryForObject(GET_SENSOR_NAME, String.class,
				deviceUri, sensorTypeName);
	}

	@Override
	public List<SensorReading> latestReadingFromAllDevices(List<Device> devices) {
		if (devices == null || devices.size() == 0) {
			return null;
		}
		Set<String> deviceUris = new HashSet<String>();

		for (Device d : devices) {
			deviceUris.add(d.getUri());
		}

		final String SQL2 = "SELECT SENSOR_NAME, IS_INDOOR, TIMESTAMP, LOCATION_INTERPRETER, VALUE, LONGITUDE, LATITUDE, ALTITUDE FROM CMU.COURSE_SENSOR CS, CMU.COURSE_DISCRETE_SENSOR_READING SR INNER JOIN (SELECT DSR.SENSOR_ID AS ID, MAX(DSR.TIMESTAMP) AS TMSTP FROM CMU.COURSE_DISCRETE_SENSOR_READING DSR, CMU.COURSE_SENSOR S, CMU.COURSE_DEVICE D WHERE S.DEVICE_ID = D.DEVICE_ID AND DSR.SENSOR_ID = S.SENSOR_ID AND D.URI IN (%s) GROUP BY DSR.SENSOR_ID) TMP ON SR.SENSOR_ID = TMP.ID AND SR.TIMESTAMP = TMP.TMSTP WHERE CS.SENSOR_ID = SR.SENSOR_ID";
		String SQL = String.format(SQL2, preparePlaceHolders(devices.size()));
		List<SensorReading> sensorReadings = simpleJdbcTemplate
				.query(SQL, ParameterizedBeanPropertyRowMapper
						.newInstance(SensorReading.class), deviceUris.toArray());
		return sensorReadings;
	}

	private String preparePlaceHolders(int length) {
		StringBuilder builder = new StringBuilder(length * 2 - 1);
		for (int i = 0; i < length; i++) {
			if (i > 0)
				builder.append(',');
			builder.append('?');
		}
		return builder.toString();
	}

	private void insertData(Configuration config, HTable table, String deviceUri, String sensorType, String sensorName, long ts, 
			String value,
			boolean isIndoor, double lon, double lat, double alt, String locationInterpreter) throws IOException, NoSuchAlgorithmException {
		
		byte[] keyBytes = generateKeyBytes(deviceUri, sensorType, sensorName, ts);
		
		Put p = new Put(keyBytes);
		p.add(Bytes.toBytes("data"), Bytes.toBytes("value"), Bytes.toBytes(value));
		p.add(Bytes.toBytes("info"), Bytes.toBytes("isIndoor"), Bytes.toBytes(isIndoor));
		p.add(Bytes.toBytes("info"), Bytes.toBytes("longitude"), Bytes.toBytes(lon));
		p.add(Bytes.toBytes("info"), Bytes.toBytes("latitude"), Bytes.toBytes(lat));
		p.add(Bytes.toBytes("info"), Bytes.toBytes("altitude"), Bytes.toBytes(alt));
		p.add(Bytes.toBytes("info"), Bytes.toBytes("locationInterpreter"), Bytes.toBytes(locationInterpreter));




		table.put(p);
	}
	
	private byte[] generateKeyBytes(String deviceUri, String sensorType, String sensorName, long ts) throws NoSuchAlgorithmException {
		byte[] deviceUriBytes = MessageDigest.getInstance("MD5").digest(deviceUri.getBytes()) ;
		byte[] sensorTypeBytes = MessageDigest.getInstance("MD5").digest(sensorType.getBytes()) ;
		byte[] sensorNameBytes = MessageDigest.getInstance("MD5").digest(sensorName.getBytes()) ;
		byte[] tsBytes = MessageDigest.getInstance("MD5").digest(ByteBuffer.allocate(Long.SIZE).putLong(ts).array()) ;

		return ArrayUtils.addAll(ArrayUtils.addAll(deviceUriBytes, sensorTypeBytes), ArrayUtils.addAll(sensorNameBytes, tsBytes));
	}
}
