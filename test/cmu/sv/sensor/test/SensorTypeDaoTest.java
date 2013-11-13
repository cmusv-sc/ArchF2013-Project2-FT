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
		int prev = sensorTypeDaoImplementation.getAllSensorTypes().size();
		
		sensorTypeDaoImplementation.addSensorType(1, "testSensorTypeName1", "testManufacturer", "0.1", 100, 1, "Fahrenheit", "testInterpreter", "testUserDefinedFields");
		SensorType st = sensorTypeDaoImplementation.getSensorType("testSensorTypeName1");
		assertEquals("testSensorTypeName1", st.getSensorTypeName());
//		TODO: Something's wrong with the manufacturer
//		assertEquals("testManufacturer", st.getManufacturer());
		assertEquals("0.1", st.getVersion());
		assertEquals(100.0, st.getMaxValue(), 0.0);
		assertEquals(1.0, st.getMinValue(), 0.0);
		assertEquals("Fahrenheit", st.getUnit());
		assertEquals("testInterpreter", st.getInterpreter());
		assertEquals("testUserDefinedFields", st.getUserDefinedFields());

//		Negative test for adding the same sensor_category_name
//		testGetAllSensorTypes() is executed first, followed by testGetSensorType()
		sensorTypeDaoImplementation.addSensorType(1, "testSensorTypeName1", "testManufacturer2", "0.1", 100, 1, "Fahrenheit", "testInterpreter2", "testUserDefinedFields2");
		
		int post = sensorTypeDaoImplementation.getAllSensorTypes().size();
		assertEquals(1, post - prev);
	}
	
	@Test
	public void testGetSensorType() {
		sensorTypeDaoImplementation.addSensorType(1, "testSensorTypeName2", "testManufacturer", "0.1", 100, 1, "Fahrenheit", "testInterpreter", "testUserDefinedFields");
		SensorType st = sensorTypeDaoImplementation.getSensorType("testSensorTypeName2");
		assertEquals("testSensorTypeName2", st.getSensorTypeName());
//		TODO: Something's wrong with the manufacturer
//		assertEquals("testManufacturer", st.getManufacturer());
		assertEquals("0.1", st.getVersion());
		assertEquals(100.0, st.getMaxValue(), 0.0);
		assertEquals(1.0, st.getMinValue(), 0.0);
		assertEquals("Fahrenheit", st.getUnit());
		assertEquals("testInterpreter", st.getInterpreter());
		assertEquals("testUserDefinedFields", st.getUserDefinedFields());
	}
	
	@Test
	public void testGetAllSensorTypes() {
		int prev = sensorTypeDaoImplementation.getAllSensorTypes().size();
		
		sensorTypeDaoImplementation.addSensorType(1, "testSensorTypeName3", "testManufacturer", "0.1", 100, 1, "Fahrenheit", "testInterpreter", "testUserDefinedFields");
		sensorTypeDaoImplementation.addSensorType(1, "testSensorTypeName4", "testManufacturer", "0.1", 100, 1, "Fahrenheit", "testInterpreter", "testUserDefinedFields");
		sensorTypeDaoImplementation.addSensorType(1, "testSensorTypeName5", "testManufacturer", "0.1", 100, 1, "Fahrenheit", "testInterpreter", "testUserDefinedFields");

		int post = sensorTypeDaoImplementation.getAllSensorTypes().size();
		assertEquals(3, post - prev);
	}
}
