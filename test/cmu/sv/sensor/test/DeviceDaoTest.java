package cmu.sv.sensor.test;

import static org.junit.Assert.assertEquals;
import models.Device;
import models.dao.DeviceDaoImplementation;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class DeviceDaoTest extends AbstractTest{
	private static DeviceDaoImplementation deviceDaoImplementation;
	
	@BeforeClass
	public static void subSetup() {
		deviceDaoImplementation = new DeviceDaoImplementation();
		deviceDaoImplementation.setSimpleJdbcTemplate(jdbcTemplate);
	}
	
	@Test
	public void testAddDevice() {
		deviceDaoImplementation.addDevice("device_type1", "testUri", "testUserDefinedFields", 10, 10, 10, "testRepresentation");
		Device st = deviceDaoImplementation.getDevice("testUri");
		assertEquals("testUri", st.getUri());
		assertEquals("testUserDefinedFields", st.getUserDefinedFields());
		assertEquals("testRepresentation", st.getRepresentation());
		assertEquals(2, deviceDaoImplementation.getAllDevices().size());
		
		deviceDaoImplementation.addDevice("device_type1", "testUri2", "testUserDefinedFields2", 10, 10, 10, "testRepresentation2");
		assertEquals(3, deviceDaoImplementation.getAllDevices().size());
		
		//negative test for adding the same sensor_category_name
		deviceDaoImplementation.addDevice("device_type1", "testUri2", "testUserDefinedFields2", 10, 10, 10, "testRepresentation2");
		assertEquals(3, deviceDaoImplementation.getAllDevices().size());
	}
	
	@Test
	@Ignore
	public void testDeviceLocation() {
		
	}
	@Test
	@Ignore
	public void testDeviceNetworkAddress() {
		
	}
}
