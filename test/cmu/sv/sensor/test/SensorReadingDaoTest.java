package cmu.sv.sensor.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import models.SensorReading;
import models.dao.SensorReadingDaoImplementation;

import org.junit.BeforeClass;
import org.junit.Test;

public class SensorReadingDaoTest extends AbstractTest{
	
	private static SensorReadingDaoImplementation sensorReadingDao;
	
	@BeforeClass
	public static void subSetup() {
		sensorReadingDao = new SensorReadingDaoImplementation();
		sensorReadingDao.setSimpleJdbcTemplate(jdbcTemplate);
	}
	
	@Test
	public void testLastestReadingFromAllDevices() {
		List<SensorReading> readings = sensorReadingDao.lastestReadingFromAllDevices("light");
		assertEquals("device1,1383927322,light,200.0", readings.get(0).toCSVString());
		assertEquals("device2,1383927322,light,200.0", readings.get(1).toCSVString());
		
		readings = sensorReadingDao.lastestReadingFromAllDevices("temp");
		assertEquals("device1,1383927322,temp,16.0", readings.get(0).toCSVString());
		assertEquals("device2,1383927322,temp,16.0", readings.get(1).toCSVString());
	}
	
	@Test
	public void testSearchSensorReadingInSpecificTimestamp() {
		SensorReading reading = sensorReadingDao.searchReading("device1", new Long(1383927322), "light");
		assertEquals("device1,1383927322,light,200.0", reading.toCSVString());
		reading = sensorReadingDao.searchReading("device2", new Long(1383927322), "light");
		assertEquals("device2,1383927322,light,200.0", reading.toCSVString());
		
		reading = sensorReadingDao.searchReading("device1", new Long(1383927322), "temp");
		assertEquals("device1,1383927322,temp,16.0", reading.toCSVString());
	}
	
	@Test
	public void testSearchSensorReadingInTimestampRange() {
		List<SensorReading> readings = sensorReadingDao.searchReading("device1", new Long(1383927321), new Long(1383927322), "light");
		assertEquals("device1,1383927322,light,200.0", readings.get(0).toCSVString());
		assertEquals("device1,1383927321,light,200.0", readings.get(1).toCSVString());
		
		readings = sensorReadingDao.searchReading("device2", new Long(1383927321), new Long(1383927322), "temp");
		assertEquals("device2,1383927322,temp,16.0", readings.get(0).toCSVString());
		assertEquals("device2,1383927321,temp,15.0", readings.get(1).toCSVString());
	}
	
	//Please pass this test
	@Test
	public void testAddSensorReading() {
		sensorReadingDao.addReading("device3", new Long(1383927321), "temp", 15.0);
		sensorReadingDao.addReading("device3", new Long(1383927321), "light", 200.0);
		
		SensorReading reading = sensorReadingDao.searchReading("device3", new Long(1383927321), "temp");
		assertEquals("device3,1383927321,temp,15.0", reading.toCSVString());
		reading = sensorReadingDao.searchReading("device3", new Long(1383927321), "light");
		assertEquals("device2,1383927321,light,200.0", reading.toCSVString());
	}

}
