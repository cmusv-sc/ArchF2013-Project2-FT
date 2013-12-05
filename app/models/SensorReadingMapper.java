package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class SensorReadingMapper implements RowMapper{

	@Override
	public OldSensorReading mapRow(ResultSet rs, int rowNum) throws SQLException {
		OldSensorReading sensorReading = new OldSensorReading();
		sensorReading.setDeviceId(rs.getString("deviceid"));
		sensorReading.setTimeStamp(rs.getLong("timestamp"));
		sensorReading.setSensorType(rs.getString("sensortype"));
		sensorReading.setValue(rs.getDouble("value"));
		return sensorReading;
	}
}