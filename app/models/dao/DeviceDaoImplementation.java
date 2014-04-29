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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import models.Device;
import models.Location;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class DeviceDaoImplementation implements DeviceDao {

	private SimpleJdbcTemplate simpleJdbcTemplate;
	private DataSourceTransactionManager txManager;

	public DataSourceTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(DataSourceTransactionManager txManager) {
		this.txManager = txManager;
	}

	@Override
	public boolean addDevice(String deviceTypeName, String uri,
			String userDefinedFields, double longitude, double latitude,
			double altitude, String representation) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txManager.getTransaction(def);

		final String SQL_SELECT_DEVICE_TYPE_ID = "SELECT DEVICE_TYPE_ID FROM CMU.COURSE_DEVICE_TYPE WHERE DEVICE_TYPE_NAME = ?";
		int deviceTypeId = simpleJdbcTemplate.queryForInt(
				SQL_SELECT_DEVICE_TYPE_ID, deviceTypeName);

		Date date = new Date();
		long time = date.getTime();
		Timestamp timestamp = new Timestamp(time);

		final String SQL_INSERT_DEVICE = "INSERT INTO CMU.COURSE_DEVICE (DEVICE_ID, DEVICE_TYPE_ID, URI, REGISTRATION_TIMESTAMP, DEVICE_USER_DEFINED_FIELDS) VALUES (cmu.course_device_id_seq.nextval, ?, ?, ?, ?)";

		final String SQL_INSERT_LOCATION = "INSERT INTO CMU.COURSE_LOCATION (LOCATION_ID, LONGITUDE, LATITUDE, ALTITUDE, REPRESENTATION) VALUES (cmu.course_location_id_seq.nextval, ?, ?, ?, ?)";

		final String SQL_INSERT_DEVICE_LOCATION = "INSERT INTO CMU.COURSE_DEVICE_LOCATION (DEVICE_ID, LOCATION_ID, TIMESTAMP, USER_DEFINED_FIELDS, IS_ACTIVE) VALUES (?, ?, ?, ?, ?)";

		final String SQL_SELECT_DEVICE_ID = "select device_id from cmu.course_device where uri = ?";
		final String SQL_SELECT_LOCATION_ID = "select location_id from cmu.course_location where longitude = ? and latitude = ? and altitude = ?";

		try {
			simpleJdbcTemplate.update(SQL_INSERT_DEVICE, deviceTypeId, uri,
					timestamp, userDefinedFields);

			List<Integer> locationIds = simpleJdbcTemplate.query(
					SQL_SELECT_LOCATION_ID,
					new ParameterizedRowMapper<Integer>() {

						@Override
						public Integer mapRow(ResultSet rs, int arg1)
								throws SQLException {
							return rs.getInt("LOCATION_ID");
						}

					}, longitude, latitude, altitude);
			int locationId = -1;
			if (locationIds.size() == 0) {
				simpleJdbcTemplate.update(SQL_INSERT_LOCATION, longitude,
						latitude, altitude, representation);
				locationId = simpleJdbcTemplate.queryForInt(
						SQL_SELECT_LOCATION_ID, longitude, latitude, altitude);
			} else {
				locationId = locationIds.get(0);
			}

			int deviceId = simpleJdbcTemplate.queryForInt(SQL_SELECT_DEVICE_ID,
					uri);
			simpleJdbcTemplate.update(SQL_INSERT_DEVICE_LOCATION, deviceId,
					locationId, timestamp, userDefinedFields, "TRUE");

		} catch (Exception e) {
			txManager.rollback(status);
			return false;
		}

		txManager.commit(status);

		return true;

	}

	@Override
	public List<Device> getAllDevices() {
		final String SQL = "SELECT * FROM CMU.COURSE_DEVICE d, CMU.COURSE_DEVICE_TYPE dt WHERE d.DEVICE_TYPE_ID = dt.DEVICE_TYPE_ID";
		final String SQL_SELECT_DEVICE_LOCATION = "SELECT LOCATION_ID FROM CMU.COURSE_DEVICE_LOCATION WHERE DEVICE_ID = ? AND IS_ACTIVE = 'TRUE'";
		final String SQL_SELECT_DEVICE_ID = "SELECT DEVICE_ID FROM CMU.COURSE_DEVICE WHERE URI = ?";
		final String SQL_SELECT_LOCATION = "SELECT * FROM CMU.COURSE_LOCATION WHERE LOCATION_ID = ?";
		final String SQL_SELECT_SENSOR_NAMES = "SELECT SENSOR_NAME FROM CMU.COURSE_SENSOR WHERE DEVICE_ID = ?";
		final String SQL_SELECT_DEVICE_TYPE_ID = "SELECT DEVICE_TYPE_ID FROM CMU.COURSE_DEVICE WHERE DEVICE_ID = ?";
		final String SQL_SELECT_SENSOR_TYPE_NAMES = "SELECT SENSOR_TYPE_NAME FROM CMU.COURSE_SENSOR_TYPE ST, CMU.COURSE_DEVICE_TYPE_SENSOR_TYPE DTST WHERE DTST.DEVICE_TYPE_ID = ? AND DTST.SENSOR_TYPE_ID = ST.SENSOR_TYPE_ID";

		List<Device> devices = null;
		try {
			devices = simpleJdbcTemplate.query(SQL,
					ParameterizedBeanPropertyRowMapper
							.newInstance(Device.class));

			Iterator<Device> iter = devices.iterator();
			while (iter.hasNext()) {
				Device device = iter.next();

				int deviceId = simpleJdbcTemplate.queryForInt(
						SQL_SELECT_DEVICE_ID, device.getUri());
				int locationId = simpleJdbcTemplate.queryForInt(
						SQL_SELECT_DEVICE_LOCATION, deviceId);
				int deviceTypeId = simpleJdbcTemplate.queryForInt(
						SQL_SELECT_DEVICE_TYPE_ID, deviceId);
				List<String> sensorTypeNames = simpleJdbcTemplate.query(
						SQL_SELECT_SENSOR_TYPE_NAMES,
						new ParameterizedRowMapper<String>() {
							@Override
							public String mapRow(ResultSet rs, int row)
									throws SQLException {
								return rs.getString("SENSOR_TYPE_NAME");
							}
						}, deviceTypeId);
				device.setSensorTypeNames(sensorTypeNames);
				Location location = simpleJdbcTemplate.queryForObject(
						SQL_SELECT_LOCATION, ParameterizedBeanPropertyRowMapper
								.newInstance(Location.class), locationId);
				List<String> sensorNames = simpleJdbcTemplate.query(
						SQL_SELECT_SENSOR_NAMES,
						new ParameterizedRowMapper<String>() {

							@Override
							public String mapRow(ResultSet rs, int row)
									throws SQLException {
								return rs.getString("SENSOR_NAME");
							}

						}, deviceId);
				device.setLocation(location);
				device.setSensorNames(sensorNames);
			}
		} catch (Exception e) {
			return null;
		}

		return devices;
	}
	
	@Override
	public Device getDevice(String uri) {

		final String SQL_SELECT_DEVICE = "SELECT * FROM CMU.COURSE_DEVICE d, CMU.COURSE_DEVICE_TYPE dt WHERE d.DEVICE_TYPE_ID = dt.DEVICE_TYPE_ID AND URI = ?";
		final String SQL_SELECT_DEVICE_LOCATION = "SELECT LOCATION_ID FROM CMU.COURSE_DEVICE_LOCATION WHERE DEVICE_ID = ? AND IS_ACTIVE = 'TRUE'";
		final String SQL_SELECT_DEVICE_ID = "SELECT DEVICE_ID FROM CMU.COURSE_DEVICE WHERE URI = ?";
		final String SQL_SELECT_LOCATION = "SELECT * FROM CMU.COURSE_LOCATION WHERE LOCATION_ID = ?";
		final String SQL_SELECT_SENSOR_NAMES = "SELECT SENSOR_NAME FROM CMU.COURSE_SENSOR WHERE DEVICE_ID = ?";
		final String SQL_SELECT_DEVICE_TYPE_ID = "SELECT DEVICE_TYPE_ID FROM CMU.COURSE_DEVICE WHERE DEVICE_ID = ?";
		final String SQL_SELECT_SENSOR_TYPE_NAMES = "SELECT SENSOR_TYPE_NAME FROM CMU.COURSE_SENSOR_TYPE ST, CMU.COURSE_DEVICE_TYPE_SENSOR_TYPE DTST WHERE DTST.DEVICE_TYPE_ID = ? AND DTST.SENSOR_TYPE_ID = ST.SENSOR_TYPE_ID";

		Device device = null;
		try {
			device = simpleJdbcTemplate.queryForObject(SQL_SELECT_DEVICE,
					ParameterizedBeanPropertyRowMapper
							.newInstance(Device.class), uri);

			int deviceId = simpleJdbcTemplate.queryForInt(SQL_SELECT_DEVICE_ID,
					device.getUri());
			int locationId = simpleJdbcTemplate.queryForInt(
					SQL_SELECT_DEVICE_LOCATION, deviceId);

			int deviceTypeId = simpleJdbcTemplate.queryForInt(
					SQL_SELECT_DEVICE_TYPE_ID, deviceId);
			List<String> sensorTypeNames = simpleJdbcTemplate.query(
					SQL_SELECT_SENSOR_TYPE_NAMES,
					new ParameterizedRowMapper<String>() {
						@Override
						public String mapRow(ResultSet rs, int row)
								throws SQLException {
							return rs.getString("SENSOR_TYPE_NAME");
						}
					}, deviceTypeId);
			device.setSensorTypeNames(sensorTypeNames);

			Location location = simpleJdbcTemplate.queryForObject(
					SQL_SELECT_LOCATION, ParameterizedBeanPropertyRowMapper
							.newInstance(Location.class), locationId);

			List<String> sensorNames = simpleJdbcTemplate.query(
					SQL_SELECT_SENSOR_NAMES,
					new ParameterizedRowMapper<String>() {

						@Override
						public String mapRow(ResultSet rs, int row)
								throws SQLException {
							return rs.getString("SENSOR_NAME");
						}

					}, deviceId);
			device.setLocation(location);
			device.setSensorNames(sensorNames);
		} catch (Exception e) {
			return null;
		}

		return device;
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

	// TODO to test
	@Override
	public Device updateDevice(String deviceUri, Device newDevice) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txManager.getTransaction(def);

		final String SELECT_NEW_LOCATION_ID = "select location_id from cmu.course_location where longitude = ? and latitude = ? and altitude = ?";
		final String ADD_NEW_LOCATION = "insert into cmu.course_location values(cmu.course_location_id_seq.nextVal, ?, ?, ?, ?)";
		
		// device user defined field should also be updated
		final String UPDATE_DEVICE = "update cmu.course_device set DEVICE_USER_DEFINED_FIELDS = ? where uri = ?";

		// update teh device location's representation
		final String UPDATE_LOCATION_REPRESENTATION = "update cmu.course_location set representation = ? where location_id = ?";
		
		
		try {

			List<Integer> locationIds = simpleJdbcTemplate.query(
					SELECT_NEW_LOCATION_ID,
					new ParameterizedRowMapper<Integer>() {

						@Override
						public Integer mapRow(ResultSet rs, int arg1)
								throws SQLException {
							return rs.getInt("LOCATION_ID");
						}

					}, newDevice.getLocation().getLongitude(), newDevice
							.getLocation().getLatitude(), newDevice
							.getLocation().getAltitude());
			int locationId = -1;
			if (locationIds.size() == 0) {
				simpleJdbcTemplate.update(ADD_NEW_LOCATION, newDevice
						.getLocation().getLongitude(), newDevice.getLocation()
						.getLatitude(), newDevice.getLocation().getAltitude(),
						newDevice.getLocation().getRepresentation());
				locationId = simpleJdbcTemplate.queryForInt(
						SELECT_NEW_LOCATION_ID, newDevice.getLocation()
								.getLongitude(), newDevice.getLocation()
								.getLatitude(), newDevice.getLocation()
								.getAltitude());
			} else {
				locationId = locationIds.get(0);
				// update location representation
				simpleJdbcTemplate.update(UPDATE_LOCATION_REPRESENTATION, newDevice.getLocation().getRepresentation(),
						locationId);
			}

			final String SELECT_DEVICE_ID = "select device_id from cmu.course_device where uri = ?";
			int deviceId = simpleJdbcTemplate.queryForInt(SELECT_DEVICE_ID,
					deviceUri);
			final String UPDATE_LOCATION_AS_INACTIVE = "update cmu.course_device_location set is_active = 'FALSE' where is_active = 'TRUE' and device_id = ?";
			simpleJdbcTemplate.update(UPDATE_LOCATION_AS_INACTIVE, deviceId);

			final String ADD_NEW_DEVICE_LOCATION = "insert into cmu.course_device_location values(?,?,?,?,?)";
			simpleJdbcTemplate.update(ADD_NEW_DEVICE_LOCATION, deviceId,
					locationId, new Timestamp(new Date().getTime()),
					newDevice.getDeviceUserDefinedFields(), "TRUE");
			
			// update the device itself
			simpleJdbcTemplate.update(UPDATE_DEVICE,
					newDevice.getDeviceUserDefinedFields(),
					deviceUri);

		} catch (DataAccessException e) {
			e.printStackTrace();
			txManager.rollback(status);
			return null;
		}
		txManager.commit(status);
		return getDevice(deviceUri);
	}

	@Override
	public boolean deleteDevice(String deviceName) {
		// delete the foreign key first
		final String SQL_DELETE_DEVICE_LOCATION = "DELETE FROM CMU.COURSE_DEVICE_LOCATION "
				+ "WHERE DEVICE_ID = (SELECT DEVICE_ID FROM CMU.COURSE_DEVICE WHERE URI = ?)";

		final String SQL_DELETE_DEVICE = "DELETE FROM CMU.COURSE_DEVICE "
				+ "WHERE URI = ?";
		try {
			simpleJdbcTemplate.update(SQL_DELETE_DEVICE_LOCATION, deviceName);
			simpleJdbcTemplate.update(SQL_DELETE_DEVICE, deviceName);
		} catch (Exception e) {
			return false;
		}
		return true;

	}

}
