package cmu.sv.sensor.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import models.DeviceType;
import models.dao.DeviceTypeDaoImplementation;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class DeviceTypeDaoTest extends AbstractTest{
	private static DeviceTypeDaoImplementation deviceTypeDaoImplementation;
	
	@BeforeClass
	public static void subSetup() {
		deviceTypeDaoImplementation = new DeviceTypeDaoImplementation();
		deviceTypeDaoImplementation.setSimpleJdbcTemplate(jdbcTemplate);
	}
	
	@Test
	public void testAddDeviceType() {
		deviceTypeDaoImplementation.addDeviceType("testDeviceTypeName", "testManufacturer", "0.1", "testUserDefinedFields", Arrays.asList("temp", "light"));
		DeviceType st = deviceTypeDaoImplementation.getDeviceType("testDeviceTypeName");
		assertEquals("testDeviceTypeName", st.getDeviceTypeName());
		assertEquals("testManufacturer", st.getManufacturer());
		assertEquals("0.1", st.getVersion());
		assertEquals("testUserDefinedFields", st.getUserDefinedFields());
		assertEquals(2, deviceTypeDaoImplementation.getAllDeviceTypes().size());
		
		deviceTypeDaoImplementation.addDeviceType("testDeviceTypeName2", "testManufacturer2", "0.1", "testUserDefinedFields2", Arrays.asList("temp", "light"));
		assertEquals(3, deviceTypeDaoImplementation.getAllDeviceTypes().size());
		
		//negative test for adding the same sensor_category_name
		deviceTypeDaoImplementation.addDeviceType("testDeviceTypeName2", "testManufacturer2", "0.1", "testUserDefinedFields2", Arrays.asList("temp", "light"));
		assertEquals(3, deviceTypeDaoImplementation.getAllDeviceTypes().size());
	}
	
	@Test
	@Ignore
	public void testGetDeviceType() {
		
	}
	@Test
	@Ignore
	public void testGetAllDeviceTypes() {
		
	}
}
