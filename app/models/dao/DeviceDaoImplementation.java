package models.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import models.Device;
import models.Location;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class DeviceDaoImplementation implements DeviceDao{
	
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
		// TODO need to use this in production for SAP HANA
		final String SQL_DEVICE_ID_SEQUENCE = "SELECT CMU.COURSE_DEVICE_ID_SEQ.NEXTVAL FROM DUMMY";
		int deviceId = simpleJdbcTemplate.queryForInt(SQL_DEVICE_ID_SEQUENCE);
		
		final String SQL_SELECT_DEVICE_TYPE_ID = "SELECT DEVICE_TYPE_ID FROM CMU.COURSE_DEVICE_TYPE WHERE DEVICE_TYPE_NAME = ?";
		int deviceTypeId = simpleJdbcTemplate.queryForInt(SQL_SELECT_DEVICE_TYPE_ID, deviceTypeName);
		
		Date date = new Date();
		long time = date.getTime();
		Timestamp timestamp = new Timestamp(time);
		
		// TODO need to use this in production for SAP HANA
		final String SQL_INSERT_DEVICE = "INSERT INTO CMU.COURSE_DEVICE (DEVICE_ID, DEVICE_TYPE_ID, URI, REGISTRATION_TIMESTAMP, DEVICE_USER_DEFINED_FIELDS) VALUES (?, ?, ?, ?, ?)";
		
		// for test only
//		final String SQL_INSERT_DEVICE = "INSERT INTO CMU.COURSE_DEVICE (DEVICE_ID, DEVICE_TYPE_ID, URI, REGISTRATION_TIMESTAMP, USER_DEFINED_FIELDS) VALUES (next value for CMU.COURSE_DEVICE_ID_SEQ, ?, ?, ?, ?)";
		
		
		final String SQL_LOCATION_ID_SEQUENCE = "SELECT CMU.COURSE_LOCATION_ID_SEQ.NEXTVAL FROM DUMMY";
		int locationId = simpleJdbcTemplate.queryForInt(SQL_LOCATION_ID_SEQUENCE);
		
		// TODO need to use this in production for SAP HANA
		final String SQL_INSERT_LOCATION = "INSERT INTO CMU.COURSE_LOCATION (LOCATION_ID, LONGITUDE, LATITUDE, ALTITUDE, REPRESENTATION) VALUES (?, ?, ?, ?, ?)";
		
		// for test only
//		final String SQL_INSERT_LOCATION = "INSERT INTO CMU.COURSE_LOCATION (LOCATION_ID, LONGITUDE, LATITUDE, ALTITUDE, REPRESENTATION) VALUES (next value for CMU.COURSE_LOCATION_ID_SEQ, ?, ?, ?, ?)";
		
		
		// TODO need to use this in production for SAP HANA
		final String SQL_INSERT_DEVICE_LOCATION = "INSERT INTO CMU.COURSE_DEVICE_LOCATION (DEVICE_ID, LOCATION_ID, TIMESTAMP, USER_DEFINED_FIELDS, IS_ACTIVE) VALUES (?, ?, ?, ?, ?)";
		
		
		// for test only
//		final String SQL_SELECT_DEVICE_ID = "SELECT DEVICE_ID FROM CMU.COURSE_DEVICE WHERE URI = ?";
		
		
//		final String SQL_SELECT_LOCATION_ID = "SELECT DEVICE_ID FROM CMU.COURSE_LOCATION WHERE LONGITUDE = ? AND LATITUDE = ? AND ALTITUDE = ?";
		
		
		try{
			// TODO need to use this in production for SAP HANA
			simpleJdbcTemplate.update(SQL_INSERT_DEVICE, deviceId, deviceTypeId, uri, timestamp, userDefinedFields);
			
			// for test only
//			simpleJdbcTemplate.update(SQL_INSERT_DEVICE, deviceTypeId, uri, timestamp, userDefinedFields);
			
			
			// TODO need to use this in production for SAP HANA
			simpleJdbcTemplate.update(SQL_INSERT_LOCATION, locationId, longitude, latitude, altitude, representation);
			
			// for test only
//			simpleJdbcTemplate.update(SQL_INSERT_LOCATION, longitude, latitude, altitude, representation);
			
			// no deviceId & locationId?
			// can we use the parameter userDefinedFields for COURSE_DEVICE_LOCATION.USER_DEFINED_FIELDS
//			int deviceId = simpleJdbcTemplate.queryForInt(SQL_SELECT_DEVICE_ID, uri);
//			int locationId = simpleJdbcTemplate.queryForInt(SQL_SELECT_LOCATION_ID, longitude, latitude, altitude);
			simpleJdbcTemplate.update(SQL_INSERT_DEVICE_LOCATION, deviceId, locationId, timestamp, userDefinedFields, "Y");
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
		
	}

	@Override
	public List<Device> getAllDevices() {
		final String SQL = "SELECT * FROM CMU.COURSE_DEVICE d, CMU.COURSE_DEVICE_TYPE dt WHERE d.DEVICE_TYPE_ID = dt.DEVICE_TYPE_ID";
		final String SQL_SELECT_DEVICE_LOCATION = "SELECT LOCATION_ID FROM CMU.COURSE_DEVICE_LOCATION WHERE DEVICE_ID = ? AND IS_ACTIVE = 'Y'";
		final String SQL_SELECT_DEVICE_ID = "SELECT DEVICE_ID FROM CMU.COURSE_DEVICE WHERE URI = ?";
		final String SQL_SELECT_LOCATION = "SELECT * FROM CMU.COURSE_LOCATION WHERE LOCATION_ID = ?";
		
		List<Device> devices = simpleJdbcTemplate.query(SQL, ParameterizedBeanPropertyRowMapper.newInstance(Device.class));
		
		Iterator<Device> iter = devices.iterator();
		while(iter.hasNext()) {
			Device device = iter.next();
			
			int deviceId = simpleJdbcTemplate.queryForInt(SQL_SELECT_DEVICE_ID, device.getUri());
			int locationId = simpleJdbcTemplate.queryForInt(SQL_SELECT_DEVICE_LOCATION, deviceId);
			
			Location location = simpleJdbcTemplate.queryForObject(SQL_SELECT_LOCATION, ParameterizedBeanPropertyRowMapper.newInstance(Location.class), locationId);
			
			device.setLocation(location);
		}
		
		return devices;
	}

	@Override
	public Device getDevice(String uri) {
		
		final String SQL_SELECT_DEVICE = "SELECT * FROM CMU.COURSE_DEVICE d, CMU.COURSE_DEVICE_TYPE dt WHERE d.DEVICE_TYPE_ID = dt.DEVICE_TYPE_ID AND URI = ?";
		final String SQL_SELECT_DEVICE_LOCATION = "SELECT LOCATION_ID FROM CMU.COURSE_DEVICE_LOCATION WHERE DEVICE_ID = ? AND IS_ACTIVE = 'Y'";
		final String SQL_SELECT_DEVICE_ID = "SELECT DEVICE_ID FROM CMU.COURSE_DEVICE WHERE URI = ?";
		final String SQL_SELECT_LOCATION = "SELECT * FROM CMU.COURSE_LOCATION WHERE LOCATION_ID = ?";
		
		Device device = simpleJdbcTemplate.queryForObject(SQL_SELECT_DEVICE, ParameterizedBeanPropertyRowMapper.newInstance(Device.class), uri);

		int deviceId = simpleJdbcTemplate.queryForInt(SQL_SELECT_DEVICE_ID, device.getUri());
		int locationId = simpleJdbcTemplate.queryForInt(SQL_SELECT_DEVICE_LOCATION, deviceId);
		
		Location location = simpleJdbcTemplate.queryForObject(SQL_SELECT_LOCATION, ParameterizedBeanPropertyRowMapper.newInstance(Location.class), locationId);
		
		device.setLocation(location);
		
		return device;
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

	//TODO to test
	@Override
	public Device updateDevice(String deviceUri, Device newDevice) {
		TransactionDefinition def = new DefaultTransactionDefinition();
	    TransactionStatus status = txManager.getTransaction(def);
	    
	    //1. set current (device, location) as not active
	    final String UPDATE_LOCATION_AS_INACTIVE = "update cmu.course_device_location set is_active = 'false' where is_active = 'true'";

		try {
			int num = simpleJdbcTemplate.update(UPDATE_LOCATION_AS_INACTIVE);
			if (num < 1) {
				txManager.rollback(status);
				return null;
			}
			
		} catch(DataAccessException e) {
			txManager.rollback(status);
			return null;
		}
	    //2. add new location
		final String ADD_NEW_LOCATION = "insert into cmu.course_location values(cmu.course_location_id_sq.nextVal, ?, ?, ?, ?)";
		try {
			int num = simpleJdbcTemplate.update(ADD_NEW_LOCATION, newDevice.getLocation().getLongitude(), newDevice.getLocation().getLatitude(), newDevice.getLocation().getAltitude(), newDevice.getLocation().getRepresentation());
			if (num < 1) {
				txManager.rollback(status);
				return null;
			}
			
		} catch(DataAccessException e) {
			txManager.rollback(status);
			return null;
		}
		
		//2. add new location
		final String SELECT_NEW_LOCATION_ID = "select location_id from cmu.course_location where longitude = ? and latitude = ? and altitude = ?)";
		final String SELECT_DEVICE_ID = "select device_id from cmu.course_device where device_uri = ?)";
		int locationId = simpleJdbcTemplate.queryForInt(SELECT_NEW_LOCATION_ID, newDevice.getLocation().getLongitude(), newDevice.getLocation().getLatitude(), newDevice.getLocation().getAltitude(), newDevice.getLocation().getRepresentation());
		int deviceId = simpleJdbcTemplate.queryForInt(SELECT_DEVICE_ID, deviceUri);
		final String ADD_NEW_DEVICE_LOCATION = "insert into cmu.course_device_location values(?,?,?,?,?)";
		try {
			int num = simpleJdbcTemplate.update(ADD_NEW_DEVICE_LOCATION, deviceId, locationId, new Timestamp(new Date().getTime()), newDevice.getUserDefinedFields(), "true");
			if (num < 1) {
				txManager.rollback(status);
				return null;
			}
			
		} catch(DataAccessException e) {
			txManager.rollback(status);
			return null;
		}
		txManager.commit(status);
		return getDevice(deviceUri);
	}

}
