package models.cmu.sv.sensor;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DeviceMapper implements RowMapper{

	@Override
	public Device mapRow(ResultSet rs, int rowNum) throws SQLException {
		Device device = new Device();
		device.setDeviceId(rs.getString("deviceid"));
		device.setDeviceType(rs.getString("devicetype"));
		device.setDeviceAgent(rs.getString("deviceagent"));
		device.setDeviceLocation(rs.getString("location"));
		return device;
	}
}