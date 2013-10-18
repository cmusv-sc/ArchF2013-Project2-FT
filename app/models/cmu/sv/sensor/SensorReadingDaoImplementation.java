package models.cmu.sv.sensor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

public class SensorReadingDaoImplementation implements SensorReadingDao{
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public SensorReading searchReading(String deviceId, Long timeStamp,
			String sensorType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addReading(String deviceId, Long timeStamp,
			String sensorType, double value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<SensorReading> searchReading(String deviceId,
			Long startTime, long endTime, String sensorType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SensorReading> lastReadingFromAllDevices(Long timeStamp,
			String sensorType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SensorReading> lastestReadingFromAllDevices(
			String sensorType) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
