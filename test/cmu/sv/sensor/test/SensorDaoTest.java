package cmu.sv.sensor.test;

import static org.junit.Assert.assertEquals;
import models.Sensor;
import models.dao.SensorDaoImplementation;

import org.junit.BeforeClass;
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
		int prev = sensorDaoImplementation.getAllSensors().size();
		
		sensorDaoImplementation.addSensor(1, 1, "testSensorName1", "testUserDefinedFields");
		Sensor st = sensorDaoImplementation.getSensor("testSensorName1");
		assertEquals(1, st.getSensorTypeId());
		assertEquals(1, st.getDeviceId());
		assertEquals("testUserDefinedFields", st.getUserDefinedFields());
		
		int post = sensorDaoImplementation.getAllSensors().size();
		assertEquals(1, post - prev);
		
		sensorDaoImplementation.addSensor(1, 1, "testSensorName2", "testUserDefinedFields");
		post = sensorDaoImplementation.getAllSensors().size();
		assertEquals(2, post - prev);
		
		//negative test for adding the same sensor_category_name
		sensorDaoImplementation.addSensor(1, 1, "testSensorName1", "testUserDefinedFields");
		post = sensorDaoImplementation.getAllSensors().size();
		assertEquals(2, post - prev);
	}
	
	@Test
	public void testGetSensor() {
		sensorDaoImplementation.addSensor(1, 1, "testSensorName3", "testUserDefinedFields");
		Sensor st = sensorDaoImplementation.getSensor("testSensorName3");
		assertEquals(1, st.getSensorTypeId());
		assertEquals(1, st.getDeviceId());
		assertEquals("testUserDefinedFields", st.getUserDefinedFields());
	}
	
	@Test
	public void testGetAllSensor() {
		int prev = sensorDaoImplementation.getAllSensors().size();
		
		sensorDaoImplementation.addSensor(1, 1, "testSensorName4", "testUserDefinedFields");
		sensorDaoImplementation.addSensor(1, 1, "testSensorName5", "testUserDefinedFields");
		sensorDaoImplementation.addSensor(1, 1, "testSensorName6", "testUserDefinedFields");

		int post = sensorDaoImplementation.getAllSensors().size();
		assertEquals(3, post - prev);
	}
}
