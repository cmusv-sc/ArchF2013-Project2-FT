package cmu.sv.sensor.test;

import static org.junit.Assert.assertEquals;
import models.Sensor;
import models.dao.SensorDaoImplementation;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class SensorDaoTest extends AbstractTest{
	private static SensorDaoImplementation sensorDaoImplementation;
	
	@BeforeClass
	public static void subSetup() {
		sensorDaoImplementation = new SensorDaoImplementation();
		sensorDaoImplementation.setSimpleJdbcTemplate(jdbcTemplate);
	}
	
	@Test
	public void testAddSensor() {
		sensorDaoImplementation.addSensor("testSensorName", "motorola temp", "devece1.sv.cmu.edu");
		Sensor st = sensorDaoImplementation.getSensor("testSensorName");
		assertEquals("testUserDefinedFields", st.getUserDefinedFields());
		assertEquals(2, sensorDaoImplementation.getAllSensors().size());
		
		sensorDaoImplementation.addSensor("testSensorName2", "motorola temp", "devece1.sv.cmu.edu");
		assertEquals(3, sensorDaoImplementation.getAllSensors().size());
		
		//negative test for adding the same sensor_category_name
		sensorDaoImplementation.addSensor("testSensorName", "motorola temp", "devece1.sv.cmu.edu");
		assertEquals(3, sensorDaoImplementation.getAllSensors().size());
	}
	
	@Test
	@Ignore
	public void testGetSensor() {
		
	}
	@Test
	@Ignore
	public void testGetAllSensor() {
		
	}
}
