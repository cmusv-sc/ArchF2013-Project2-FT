package models.cmu.sv.sensor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

public class DeviceDaoImplementation implements DeviceDao{
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public ArrayList<Device> getAllDevices() {
		final String SQL = "SELECT \"DEVICEID\", \"DEVICETYPE\", \"DEVICEAGENT\", \"LOCATION\" FROM CMU.DEVICE";
	    
//		ArrayList<Device> devices = (ArrayList<Device>)jdbcTemplate.queryForObject(SQL, new DeviceMapper());
		ArrayList<Device> devices = (ArrayList<Device>)jdbcTemplate.query(SQL, new DeviceMapper());
		return devices;
	}

	@Override
	public ArrayList<String> getSensorType(String deviceType) {
		// TODO Auto-generated method stub
		return null;
	}

}
