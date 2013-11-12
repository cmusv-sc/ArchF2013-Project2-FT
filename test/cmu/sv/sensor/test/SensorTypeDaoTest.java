package cmu.sv.sensor.test;

import static org.junit.Assert.assertEquals;
import models.SensorType;
import models.dao.SensorTypeDaoImplementation;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class SensorTypeDaoTest extends AbstractTest{
	private static SensorTypeDaoImplementation sensorTypeDaoImplementation;
	
	@BeforeClass
	public static void subSetup() {
		sensorTypeDaoImplementation = new SensorTypeDaoImplementation();
		sensorTypeDaoImplementation.setSimpleJdbcTemplate(jdbcTemplate);
	}
	
	@Test
	public void testAddSensorType() {
		sensorTypeDaoImplementation.addSensorType("temp", "testSensorTypeName", "testManufacturer", "0.1", 100, 1, "Fahrenheit", "testInterpreter", "testUserDefinedFields");
		SensorType st = sensorTypeDaoImplementation.getSensorType("testSensorTypeName");
		assertEquals("testSensorTypeName", st.getSensorTypeName());
		assertEquals("testManufacturer", st.getManufactuere());
		assertEquals("0.1", st.getVersion());
		assertEquals(100.0, st.getMaxValue());
		assertEquals(1.0, st.getMinValue());
		assertEquals("Fahrenheit", st.getUnit());
		assertEquals("testInterpreter", st.getInterpreter());
		assertEquals("testUserDefinedFields", st.getUserDefinedFields());
		assertEquals(2, sensorTypeDaoImplementation.getAllSensorTypes().size());
		
		sensorTypeDaoImplementation.addSensorType("temp", "testSensorTypeName2", "testManufacturer2", "0.1", 100, 1, "Fahrenheit", "testInterpreter2", "testUserDefinedFields2");
		assertEquals(3, sensorTypeDaoImplementation.getAllSensorTypes().size());
		
		//negative test for adding the same sensor_category_name
		sensorTypeDaoImplementation.addSensorType("temp", "testSensorTypeName1", "testManufacturer2", "0.1", 100, 1, "Fahrenheit", "testInterpreter2", "testUserDefinedFields2");
		assertEquals(3, sensorTypeDaoImplementation.getAllSensorTypes().size());
	}
	
	@Test
	@Ignore
	public void testGetSensorType() {
		
	}
	@Test
	@Ignore
	public void testGetAllSensorTypes() {
		
	}
}
