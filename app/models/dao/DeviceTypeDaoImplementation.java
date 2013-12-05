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

public class DeviceTypeDaoImplementation implements DeviceTypeDao{

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
		List<DeviceType> deviceTypes = simpleJdbcTemplate.query(sqlStatement, ParameterizedBeanPropertyRowMapper.newInstance(DeviceType.class));
		
		Map<String, DeviceType> deviceTypesMap = new HashMap<String, DeviceType>();
		for (DeviceType deviceType : deviceTypes) {
			if (deviceType.getSensorTypeNames() == null) {
				deviceType.addSensorTypes(new LinkedList<String>());
			}
			deviceTypesMap.put(deviceType.getDeviceTypeName(), deviceType);
		}
		
		List<Map<String,Object>> deviceTypeSensorTypeMap = getAllDevicesSensorTypes();
		
		for (Map<String, Object> map : deviceTypeSensorTypeMap) {
			deviceTypesMap.get(map.get("DEVICE_TYPE_NAME")).getSensorTypeNames().add((String)map.get("SENSOR_TYPE_NAME"));
		}
		
		List<DeviceType> result = new ArrayList<DeviceType>();
		for (Map.Entry<String, DeviceType> entry : deviceTypesMap.entrySet()) {
			result.add(entry.getValue());
		}
		
		return result;
	}

	@Override
	public DeviceType getDeviceType(String deviceTypeName) {
		String sqlStatement = "select * from cmu.course_device_type where device_type_name = ?";
		DeviceType deviceType = simpleJdbcTemplate.queryForObject(sqlStatement, ParameterizedBeanPropertyRowMapper.newInstance(DeviceType.class), deviceTypeName);
		
		if (deviceType.getSensorTypeNames() == null) {
			deviceType.addSensorTypes(new ArrayList<String>());
		}
		List<Map<String, Object>> deviceTypeSensorTypeMap = getDeviceSensorTypes(deviceTypeName);
		
		for (Map<String, Object> map : deviceTypeSensorTypeMap) {
			deviceType.getSensorTypeNames().add((String)map.get("SENSOR_TYPE_NAME"));
		}
		
		return deviceType;
	}

	@Override
	public boolean addDeviceType(String deviceTypeName, String manufacturer,
			String version, String userDefinedFields,
			List<String> sensorTypes) {
		TransactionDefinition def = new DefaultTransactionDefinition();
	    TransactionStatus status = txManager.getTransaction(def);
	    
	    
		String sqlStatement = "insert into cmu.course_device_type (device_type_id, device_type_name, manufacturer, version, user_defined_fields) values (cmu.COURSE_DEVICE_TYPE_ID_SEQ.nextval, ?, ?, ?, ?)"; 
		
		try {
			simpleJdbcTemplate.update(sqlStatement,deviceTypeName, manufacturer, version, userDefinedFields);
		}catch(DataAccessException e) {
			txManager.rollback(status);
			return false;
		}
		
		sqlStatement = "insert into cmu.course_device_type_sensor_type (device_type_id, sensor_type_id) "
				+ "select device_type_id, sensor_type_id "
				+ "from cmu.course_device_type d, cmu.course_sensor_type s "
				+ "where d.device_type_name = ? and s.sensor_type_name = ?";
		
		List<Object[]> params = new ArrayList<Object[]>();
		for (String sensorType : sensorTypes) {
			params.add(new Object[] {
					deviceTypeName,
					sensorType
			});
		}

		int[] returnVals = simpleJdbcTemplate.batchUpdate(sqlStatement, params);
		
		for (int i = 0; i < params.size(); i++) {
			if (returnVals[i] != 1) {
				txManager.rollback(status);
				return false;
			}
		}
		
		txManager.commit(status);
		return true;
		
	}
	
	private List<Map<String,Object>> getAllDevicesSensorTypes() {
		final String SQL = "select d.device_type_name, s.sensor_type_name from cmu.course_device_type d "
				+ "inner join cmu.course_device_type_sensor_type ds on d.device_type_id = ds.device_type_id "
				+ "inner join cmu.course_sensor_type s on ds.sensor_type_id = s.sensor_type_id";
		return simpleJdbcTemplate.queryForList(SQL);
	}
	
	private List<Map<String,Object>> getDeviceSensorTypes(String deviceTypeName) {
		final String SQL = "select d.device_type_name, s.sensor_type_name "
				+ "from cmu.course_device_type d, cmu.course_device_type_sensor_type ds, cmu.course_sensor_type s "
				+ "where d.device_type_name = ? and "
				+ "d.device_type_id = ds.device_type_id and "
				+ "ds.sensor_type_id = s.sensor_type_id";
		return simpleJdbcTemplate.queryForList(SQL, deviceTypeName);
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

	//TODO
	@Override
	public DeviceType updateDeviceType(String deviceTypeName, DeviceType newDeviceType) {
		TransactionDefinition def = new DefaultTransactionDefinition();
	    TransactionStatus status = txManager.getTransaction(def);
	    
		final String UPDATE_DEVICE_TYPE = "update cmu.course_device_type set manufacturer = ?,device_type_user_defined_fields = ? where device_type_name = ?";

		try {
			int num = simpleJdbcTemplate.update(UPDATE_DEVICE_TYPE, newDeviceType.getManufacturer(), newDeviceType.getDeviceTypeUserDefinedFields(), deviceTypeName);
			if (num < 1) {
				txManager.rollback(status);
				return null;
			}
			
		} catch(DataAccessException e) {
			txManager.rollback(status);
			return null;
		}
		
		
		txManager.commit(status);
		return getDeviceType(deviceTypeName);
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

}
