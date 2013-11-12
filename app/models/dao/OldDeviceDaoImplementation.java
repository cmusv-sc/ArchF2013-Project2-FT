package models.dao;

import java.util.List;

import models.OldDevice;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class OldDeviceDaoImplementation implements OldDeviceDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	public void setSimpleJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
		this.simpleJdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<OldDevice> getAllDevices() {
		final String SQL = "SELECT \"DEVICEID\", \"DEVICETYPE\", \"DEVICEAGENT\", \"LOCATION\" FROM CMU.DEVICE";
		List<OldDevice> devices = simpleJdbcTemplate.query(SQL, ParameterizedBeanPropertyRowMapper.newInstance(OldDevice.class));
		return devices;
	}

	@Override
	public List<String> getSensorType(String deviceType) {
		// TODO Auto-generated method stub
		return null;
	}

}
