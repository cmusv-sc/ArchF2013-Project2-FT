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

import java.util.Arrays;

import models.DeviceType;
import models.dao.DeviceTypeDaoImplementation;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class DeviceTypeDaoTest extends AbstractTest{
	private static DeviceTypeDaoImplementation deviceTypeDaoImplementation;
	
	@BeforeClass
	public static void subSetup() {
		deviceTypeDaoImplementation = new DeviceTypeDaoImplementation();
		deviceTypeDaoImplementation.setSimpleJdbcTemplate(jdbcTemplate);
	}
	
	@Test
	public void testAddDeviceType() {
		deviceTypeDaoImplementation.addDeviceType("testDeviceTypeName", "testManufacturer", "0.1", "testUserDefinedFields", Arrays.asList("texas instrument temp"));
		DeviceType st = deviceTypeDaoImplementation.getDeviceType("testDeviceTypeName");
		assertEquals("testDeviceTypeName", st.getDeviceTypeName());
		assertEquals("testManufacturer", st.getManufacturer());
		assertEquals("0.1", st.getVersion());
		assertEquals("testUserDefinedFields", st.getDeviceTypeUserDefinedFields());
		assertEquals(1, st.getSensorTypeNames().size());
		assertEquals("texas instrument temp", st.getSensorTypeNames().get(0));
		assertEquals(2, deviceTypeDaoImplementation.getAllDeviceTypes().size());
		
		deviceTypeDaoImplementation.addDeviceType("testDeviceTypeName2", "testManufacturer2", "0.1", "testUserDefinedFields2", Arrays.asList("temp", "light"));
		assertEquals(3, deviceTypeDaoImplementation.getAllDeviceTypes().size());
		
		
	}
	
	@Test(expected = Exception.class)
	public void negativeTestAddDeviceType() {
		deviceTypeDaoImplementation.addDeviceType("testDeviceTypeName2", "testManufacturer2", "0.1", "testUserDefinedFields2", Arrays.asList("temp", "light"));
	}
	@Test
	@Ignore
	public void testGetAllDeviceTypes() {
		
	}
}
