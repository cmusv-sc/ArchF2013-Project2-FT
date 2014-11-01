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
import java.util.List;

import models.Sensor;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class SensorDaoImplementation implements SensorDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;
	private DataSourceTransactionManager txManager;

	public DataSourceTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(DataSourceTransactionManager txManager) {
		this.txManager = txManager;
	}
	@Override
	public boolean addSensor(String sensorTypeName, String deviceUri, String sensorName, String sensorUserDefinedFields) {
//		Find SensorTypeId by SensorTypeName, return false if SensorTypeId is not found
		try{
			final String SQL_GET_SENSOR_TYPE_ID = "select sensor_type_id from cmu.course_sensor_type where sensor_type_name = ?";
			int sensorTypeId = simpleJdbcTemplate.queryForInt(SQL_GET_SENSOR_TYPE_ID, sensorTypeName);
		
		
	//		Find DeviceId by URI, return false if uri is not found
			int deviceId = -1;
			final String SQL_GET_DEVICE_ID = "select device_id from cmu.course_device where uri = ?";
			deviceId = simpleJdbcTemplate.queryForInt(SQL_GET_DEVICE_ID, deviceUri);
			
			final String SQL_GET_DEVICE_TYPE_ID = "select dt.device_type_id from cmu.course_device_type dt, cmu.course_device d where d.device_type_id = dt.device_type_id and d.device_id = ?";
			int deviceTypeId = simpleJdbcTemplate.queryForInt(SQL_GET_DEVICE_TYPE_ID, deviceId);
			final String SQL_GET_SENSOR_TYPE_NAMES = "select sensor_type_name from cmu.course_device_type_sensor_type dtst, cmu.course_sensor_type st where device_type_id = ? and st.sensor_type_id = dtst.sensor_type_id";
			List<String> sensorTypeNames = simpleJdbcTemplate.query(SQL_GET_SENSOR_TYPE_NAMES, new ParameterizedRowMapper<String>() {

				@Override
				public String mapRow(ResultSet rs, int arg1) throws SQLException {
					// TODO Auto-generated method stub
					return rs.getString("SENSOR_TYPE_NAME");
				}
				
			}, deviceTypeId);
			
			if (!sensorTypeNames.contains(sensorTypeName)) {
				return false;
			}
			
			final String SQL = "INSERT INTO CMU.COURSE_SENSOR (SENSOR_ID, SENSOR_TYPE_ID, DEVICE_ID, SENSOR_NAME, SENSOR_USER_DEFINED_FIELDS) VALUES (CMU.COURSE_SENSOR_ID_SEQ.NEXTVAL, ?, ?, ?, ?)";
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
	public boolean addSensor(String sensorTypeName, String deviceUri, String sensorName, String sensorUserDefinedFields, String userName) {
		TransactionDefinition def = new DefaultTransactionDefinition();
	    TransactionStatus status = txManager.getTransaction(def);
	    
		try {
			//1. check if userName exist
			final String SELECT_USER_ID = "select user_id from cmu.course_user where user_name = ?";
			int userId = simpleJdbcTemplate.queryForInt(SELECT_USER_ID, userName);
			
			//2. get deviceId
			final String SELECT_DEVICE_ID = "select device_id from cmu.course_device where uri = ?";
			int deviceId = simpleJdbcTemplate.queryForInt(SELECT_DEVICE_ID, deviceUri);
			final String SQL_GET_DEVICE_TYPE_ID = "select dt.device_type_id from cmu.course_device_type dt, cmu.course_device d where d.device_type_id = dt.device_type_id and d.device_id = ?";
			int deviceTypeId = simpleJdbcTemplate.queryForInt(SQL_GET_DEVICE_TYPE_ID, deviceId);
			final String SQL_GET_SENSOR_TYPE_NAMES = "select sensor_type_name from cmu.course_device_type_sensor_type dtst, cmu.course_sensor_type st where device_type_id = ? and st.sensor_type_id = dtst.sensor_type_id";
			List<String> sensorTypeNames = simpleJdbcTemplate.query(SQL_GET_SENSOR_TYPE_NAMES, new ParameterizedRowMapper<String>() {

				@Override
				public String mapRow(ResultSet rs, int arg1) throws SQLException {
					// TODO Auto-generated method stub
					return rs.getString("SENSOR_TYPE_NAME");
				}
				
			}, deviceTypeId);
			
			if (!sensorTypeNames.contains(sensorTypeName)) {
				return false;
			}
			
			//3. insert sensor
			final String GETSENSORTYPEID = "select sensor_type_id from cmu.course_sensor_type where sensor_type_name = ?";
			int sensorTypeId = simpleJdbcTemplate.queryForInt(GETSENSORTYPEID, sensorTypeName);
			final String SQL = "INSERT INTO CMU.COURSE_SENSOR (SENSOR_ID, SENSOR_TYPE_ID, DEVICE_ID, SENSOR_NAME, SENSOR_USER_DEFINED_FIELDS) VALUES (CMU.COURSE_SENSOR_ID_SEQ.NEXTVAL, ?, ?, ?, ?)";
			simpleJdbcTemplate.update(SQL, sensorTypeId, deviceId, sensorName, sensorUserDefinedFields);
			
			//4. inser sensor owner
			final String GET_SENSOR_ID = "select sensor_id from cmu.course_sensor where sensor_name = ?";
			int sensorId = simpleJdbcTemplate.queryForInt(GET_SENSOR_ID, sensorName);
			final String INSERT_SENSOR_OWNER = "insert into cmu.course_sensor_owner values (?, ?)";
			simpleJdbcTemplate.update(INSERT_SENSOR_OWNER, sensorId, userId);

		} catch (EmptyResultDataAccessException e) {
			txManager.rollback(status);
			return false;
		}
		
		txManager.commit(status);
		return true;
	}
	
	@Override
	public boolean updateSensor(String sensorName, String sensorUserDefinedFields) {
		final String SQL = "UPDATE CMU.COURSE_SENSOR " 
				+ "SET SENSOR_USER_DEFINED_FIELDS = ? " 
				+ "WHERE SENSOR_NAME = ?";	
		try{
			simpleJdbcTemplate.update(SQL, sensorUserDefinedFields, sensorName);
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
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}
        
	public List<Sensor> getAllSensorsReduced() {
		final String SQL = "SELECT * "
                                 + "FROM CMU.COURSE_SENSOR s, CMU.COURSE_SENSOR_TYPE st, CMU.COURSE_SENSOR_CATEGORY sc " 
                                 + "WHERE s.sensor_type_id = st.sensor_type_id and st.sensor_category_id = sc.sensor_category_id";
		try{
			List<Sensor> sensors = simpleJdbcTemplate.query(SQL, 
					ParameterizedBeanPropertyRowMapper.newInstance(Sensor.class));

			return sensors;
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	
	@Override
	public List<Sensor> getAllSensors(String userName) {
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR s, CMU.COURSE_SENSOR_TYPE st, CMU.COURSE_SENSOR_CATEGORY sc, cmu.course_sensor_owner so, cmu.course_user u "
				+ " WHERE s.sensor_id = so.sensor_id and so.user_id = u.user_id and u.user_name = ? and s.sensor_type_id = st.sensor_type_id and st.sensor_category_id = sc.sensor_category_id";
		final String SQL_GET_DEVICE_ID = "SELECT DEVICE_ID FROM CMU.COURSE_SENSOR WHERE SENSOR_NAME = ?";
		final String SQL_GET_DEVICE_URI = "SELECT URI FROM CMU.COURSE_DEVICE WHERE DEVICE_ID = ?";
		try{
			List<Sensor> sensors = simpleJdbcTemplate.query(SQL, ParameterizedBeanPropertyRowMapper.newInstance(Sensor.class), userName);
			
//			Set Sensor.deviceUri by it Device ID
			for(Sensor s : sensors){
				int deviceId = simpleJdbcTemplate.queryForInt(SQL_GET_DEVICE_ID, s.getSensorName());
				String uri = simpleJdbcTemplate.queryForObject(SQL_GET_DEVICE_URI, 
						String.class, deviceId);
				s.setDeviceUri(uri);
			}
			
			return sensors;
		}catch(EmptyResultDataAccessException e){
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

	@Override
	public Sensor getSensor(String sensorName, String userName) {
		final String SQL_GET_SENSOR = "SELECT * FROM CMU.COURSE_SENSOR s, CMU.COURSE_SENSOR_TYPE st, CMU.COURSE_SENSOR_CATEGORY sc, cmu.course_sensor_owner so, cmu.course_user u "
				+ " WHERE s.sensor_id = so.sensor_id and so.user_id = u.user_id and u.user_name = ? and s.sensor_name = ? and s.sensor_type_id = st.sensor_type_id and st.sensor_category_id = sc.sensor_category_id";
		final String SQL_GET_DEVICE_ID = "SELECT DEVICE_ID FROM CMU.COURSE_SENSOR WHERE SENSOR_NAME = ?";
		final String SQL_GET_DEVICE_URI = "SELECT URI FROM CMU.COURSE_DEVICE WHERE DEVICE_ID = ?";
		
		try{
//			Get sensors
			Sensor sensor = simpleJdbcTemplate.queryForObject(SQL_GET_SENSOR, 
					ParameterizedBeanPropertyRowMapper.newInstance(Sensor.class), 
					userName, 
					sensorName);
//			Set Sensor.deviceUri by it Device ID
			int deviceId = simpleJdbcTemplate.queryForInt(SQL_GET_DEVICE_ID, sensor.getSensorName());
			String uri = simpleJdbcTemplate.queryForObject(SQL_GET_DEVICE_URI, 
					String.class, deviceId);
			sensor.setDeviceUri(uri);
			return sensor;
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}


}
