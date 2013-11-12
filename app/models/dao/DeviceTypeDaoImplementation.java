package models.dao;

import java.util.List;

import models.DeviceType;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class DeviceTypeDaoImplementation implements DeviceTypeDao{

	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public List<DeviceType> getAllDeviceTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceType getDeviceType(String deviceTypeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addDeviceType(String deviceTypeName, String manufacturer,
			String version, String userDefinedFields,
			List<String> sensorTypes) {
		// TODO Auto-generated method stub
		
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
