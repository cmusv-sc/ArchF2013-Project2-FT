/*******************************************************************************
 * Copyright (c) 2013 Carnegie Mellon University Silicon Valley. 
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available
 * under the terms of dual licensing(GPL V2 for Research/Education
 * purposes). GNU Public License v2.0 which accompanies this distribution
 * is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * 
 * Please contact http://www.cmu.edu/silicon-valley/ if you have any 
 * questions.
 ******************************************************************************/
package cmu.sv.sensor.test;

import static org.junit.Assert.assertEquals;
import models.Device;
import models.dao.DeviceDaoImplementation;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
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
		assertEquals("testUserDefinedFields", st.getDeviceUserDefinedFields());
//		assertEquals("testRepresentation", st.getRepresentation());
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
