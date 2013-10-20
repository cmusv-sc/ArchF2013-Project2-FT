package models.dao;

import java.util.List;

import models.Device;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class DeviceDaoImplementation implements DeviceDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	public void setSimpleJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
		this.simpleJdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Device> getAllDevices() {
		final String SQL = "SELECT \"DEVICEID\", \"DEVICETYPE\", \"DEVICEAGENT\", \"LOCATION\" FROM CMU.DEVICE";
		List<Device> devices = simpleJdbcTemplate.query(SQL, ParameterizedBeanPropertyRowMapper.newInstance(Device.class));
		return devices;
	}

	@Override
	public List<String> getSensorType(String deviceType) {
		// TODO Auto-generated method stub
		return null;
	}

}
