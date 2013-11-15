package models.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.DeviceType;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class DeviceTypeDaoImplementation implements DeviceTypeDao{

	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public List<DeviceType> getAllDeviceTypes() {
		String sqlStatement = "select * from cmu.course_device_type";
		List<DeviceType> deviceTypes = simpleJdbcTemplate.query(sqlStatement, ParameterizedBeanPropertyRowMapper.newInstance(DeviceType.class));
		
		Map<String, DeviceType> deviceTypesMap = new HashMap<String, DeviceType>();
		for (DeviceType deviceType : deviceTypes) {
			if (deviceType.getSensorTypes() == null) {
				deviceType.setSensorTypes(new LinkedList<String>());
			}
			deviceTypesMap.put(deviceType.getDeviceTypeName(), deviceType);
		}
		
		List<Map<String,Object>> deviceTypeSensorTypeMap = getAllDevicesSensorTypes();
		
		for (Map<String, Object> map : deviceTypeSensorTypeMap) {
			deviceTypesMap.get(map.get("device_type_name")).getSensorTypes().add((String)map.get("sensor_type_name"));
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
		
		if (deviceType.getSensorTypes() == null) {
			deviceType.setSensorTypes(new ArrayList<String>());
		}
		List<Map<String, Object>> deviceTypeSensorTypeMap = getDeviceSensorTypes(deviceTypeName);
		
		for (Map<String, Object> map : deviceTypeSensorTypeMap) {
			deviceType.getSensorTypes().add((String)map.get("sensor_type_name"));
		}
		
		return deviceType;
	}

	@Override
	public void addDeviceType(String deviceTypeName, String manufacturer,
			String version, String userDefinedFields,
			List<String> sensorTypes) {
		String sqlStatement = "insert into cmu.course_device_type (device_type_id, device_type_name, manufacturer, version, user_defined_fields) values (next value for cmu.COURSE_DEVICE_TYPE_ID_SEQ, ?, ?, ?, ?)"; 
		int numOfrowsAffected = simpleJdbcTemplate.update(sqlStatement, deviceTypeName,
				manufacturer,
				version,
				userDefinedFields);
		
		
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
		
		simpleJdbcTemplate.batchUpdate(sqlStatement, params);
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

}
