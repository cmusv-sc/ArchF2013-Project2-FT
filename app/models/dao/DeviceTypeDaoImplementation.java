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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.DeviceType;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class DeviceTypeDaoImplementation implements DeviceTypeDao {

	private SimpleJdbcTemplate simpleJdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private DataSourceTransactionManager txManager;

	public DataSourceTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(DataSourceTransactionManager txManager) {
		this.txManager = txManager;
	}

	@Override
	public List<DeviceType> getAllDeviceTypes() {
		String sqlStatement = "select * from cmu.course_device_type";
		try {
			List<DeviceType> deviceTypes = simpleJdbcTemplate.query(
					sqlStatement, ParameterizedBeanPropertyRowMapper
							.newInstance(DeviceType.class));

			Map<String, DeviceType> deviceTypesMap = new HashMap<String, DeviceType>();
			for (DeviceType deviceType : deviceTypes) {
				if (deviceType.getSensorTypeNames() == null) {
					deviceType.addSensorTypes(new LinkedList<String>());
				}
				deviceTypesMap.put(deviceType.getDeviceTypeName(), deviceType);
			}

			List<Map<String, Object>> deviceTypeSensorTypeMap = getAllDevicesSensorTypes();

			for (Map<String, Object> map : deviceTypeSensorTypeMap) {
				deviceTypesMap.get(map.get("DEVICE_TYPE_NAME"))
						.getSensorTypeNames()
						.add((String) map.get("SENSOR_TYPE_NAME"));
			}

			List<DeviceType> result = new ArrayList<DeviceType>();
			for (Map.Entry<String, DeviceType> entry : deviceTypesMap
					.entrySet()) {
				result.add(entry.getValue());
			}

			return result;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public DeviceType getDeviceType(String deviceTypeName) {
		String sqlStatement = "select * from cmu.course_device_type where device_type_name = ?";

		try {
			DeviceType deviceType = simpleJdbcTemplate.queryForObject(
					sqlStatement, ParameterizedBeanPropertyRowMapper
							.newInstance(DeviceType.class), deviceTypeName);

			if (deviceType.getSensorTypeNames() == null) {
				deviceType.addSensorTypes(new ArrayList<String>());
			}
			List<Map<String, Object>> deviceTypeSensorTypeMap = getDeviceSensorTypes(deviceTypeName);

			for (Map<String, Object> map : deviceTypeSensorTypeMap) {
				deviceType.getSensorTypeNames().add(
						(String) map.get("SENSOR_TYPE_NAME"));
			}

			return deviceType;

		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public boolean addDeviceType(String deviceTypeName, String manufacturer,
			String version, String userDefinedFields, List<String> sensorTypes) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txManager.getTransaction(def);

		String sqlStatement = "insert into cmu.course_device_type values (cmu.COURSE_DEVICE_TYPE_ID_SEQ.nextval, ?, ?, ?, ?)";

		try {
			simpleJdbcTemplate.update(sqlStatement, deviceTypeName,
					manufacturer, version, userDefinedFields);
		} catch (DataAccessException e) {
			txManager.rollback(status);
			return false;
		}

		sqlStatement = "insert into cmu.course_device_type_sensor_type (device_type_id, sensor_type_id) "
				+ "select device_type_id, sensor_type_id "
				+ "from cmu.course_device_type d, cmu.course_sensor_type s "
				+ "where d.device_type_name = ? and s.sensor_type_name = ?";

		List<Object[]> params = new ArrayList<Object[]>();
		for (String sensorType : sensorTypes) {
			params.add(new Object[] { deviceTypeName, sensorType });
		}

		try {
			int[] returnVals = simpleJdbcTemplate.batchUpdate(sqlStatement,
					params);

			for (int i = 0; i < params.size(); i++) {
				if (returnVals[i] != 1) {
					txManager.rollback(status);
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}

		txManager.commit(status);
		return true;

	}

	private List<Map<String, Object>> getAllDevicesSensorTypes()
			throws Exception {
		final String SQL = "select d.device_type_name, s.sensor_type_name from cmu.course_device_type d "
				+ "inner join cmu.course_device_type_sensor_type ds on d.device_type_id = ds.device_type_id "
				+ "inner join cmu.course_sensor_type s on ds.sensor_type_id = s.sensor_type_id";
		try {
			return simpleJdbcTemplate.queryForList(SQL);
		} catch (Exception e) {
			throw e;
		}
	}

	private List<Map<String, Object>> getDeviceSensorTypes(String deviceTypeName)
			throws Exception {
		final String SQL = "select d.device_type_name, s.sensor_type_name "
				+ "from cmu.course_device_type d, cmu.course_device_type_sensor_type ds, cmu.course_sensor_type s "
				+ "where d.device_type_name = ? and "
				+ "d.device_type_id = ds.device_type_id and "
				+ "ds.sensor_type_id = s.sensor_type_id";
		try {
			return simpleJdbcTemplate.queryForList(SQL, deviceTypeName);
		} catch (Exception e) {
			throw e;
		}
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

	// TODO
	@Override
	public DeviceType updateDeviceType(String deviceTypeName,
			DeviceType newDeviceType) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txManager.getTransaction(def);

		final String UPDATE_DEVICE_TYPE = "update cmu.course_device_type set manufacturer = ?,device_type_user_defined_fields = ? where device_type_name = ?";

		try {
			int num = simpleJdbcTemplate.update(UPDATE_DEVICE_TYPE,
					newDeviceType.getManufacturer(),
					newDeviceType.getDeviceTypeUserDefinedFields(),
					deviceTypeName);
			if (num < 1) {
				txManager.rollback(status);
				return null;
			}

		} catch (DataAccessException e) {
			txManager.rollback(status);
			return null;
		}

		txManager.commit(status);
		return getDeviceType(deviceTypeName);
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public void setNamedParameterJdbcTemplate(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public boolean deleteDeviceType(String deviceTypeName) {
		// delete the foreign key first
		final String SQL_DELETE_DEVICE_TYPE_SENSOR_TYPE = "DELETE FROM CMU.COURSE_DEVICE_TYPE_SENSOR_TYPE "
				+ "WHERE DEVICE_TYPE_ID = (SELECT DEVICE_TYPE_ID FROM CMU.COURSE_DEVICE_TYPE WHERE DEVICE_TYPE_NAME = ?)";
		
		final String SQL_DELETE_DEVICE_TYPE = "DELETE FROM CMU.COURSE_DEVICE_TYPE "
				+ "WHERE DEVICE_TYPE_NAME = ?";
		try {
			simpleJdbcTemplate.update(SQL_DELETE_DEVICE_TYPE_SENSOR_TYPE, deviceTypeName);
			simpleJdbcTemplate.update(SQL_DELETE_DEVICE_TYPE, deviceTypeName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
