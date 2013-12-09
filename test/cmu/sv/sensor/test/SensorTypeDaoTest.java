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
import models.SensorType;
import models.dao.SensorTypeDaoImplementation;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
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
		
		sensorTypeDaoImplementation.addSensorType("testSensorTypeName1", "testManufacturer", "0.1", 100, 1, "Fahrenheit", "testInterpreter", "testUserDefinedFields", "testSensorCategory");
		SensorType st = sensorTypeDaoImplementation.getSensorType("testSensorTypeName1");
		assertEquals("testSensorTypeName1", st.getSensorTypeName());
		assertEquals("testManufacturer", st.getManufacturer());
		assertEquals("0.1", st.getVersion());
//		assertEquals(100.0, st.getMaxValue(), 0.0);
//		assertEquals(1.0, st.getMinValue(), 0.0);
		assertEquals("Fahrenheit", st.getUnit());
		assertEquals("testInterpreter", st.getInterpreter());
//		assertEquals("testUserDefinedFields", st.getSensorUserDefinedFields());
		assertEquals("testSensorCategory", st.getSensorCategoryName());
		
//		Negative test for adding the same sensor_category_name
//		testGetAllSensorTypes() is executed first, followed by testGetSensorType()
		sensorTypeDaoImplementation.addSensorType("testSensorTypeName1", "testManufacturer2", "0.1", 100, 1, "Fahrenheit", "testInterpreter2", "testUserDefinedFields2", "testSensorCategory");
		
		int post = sensorTypeDaoImplementation.getAllSensorTypes().size();
		assertEquals(1, post - prev);
	}
	
	@Test
	public void testGetSensorType() {
		sensorTypeDaoImplementation.addSensorType("testSensorTypeName2", "testManufacturer", "0.1", 100, 1, "Fahrenheit", "testInterpreter", "testUserDefinedFields", "testSensorCategory");
		SensorType st = sensorTypeDaoImplementation.getSensorType("testSensorTypeName2");
		assertEquals("testSensorTypeName2", st.getSensorTypeName());
		assertEquals("testManufacturer", st.getManufacturer());
		assertEquals("0.1", st.getVersion());
//		assertEquals(100.0, st.getMaxValue(), 0.0);
//		assertEquals(1.0, st.getMinValue(), 0.0);
		assertEquals("Fahrenheit", st.getUnit());
		assertEquals("testInterpreter", st.getInterpreter());
//		assertEquals("testUserDefinedFields", st.getSensorUserDefinedFields());
		assertEquals("testSensorCategory", st.getSensorCategoryName());
	}
	
	@Test
	public void testGetAllSensorTypes() {
		int prev = sensorTypeDaoImplementation.getAllSensorTypes().size();
		
		sensorTypeDaoImplementation.addSensorType("testSensorTypeName3", "testManufacturer", "0.1", 100, 1, "Fahrenheit", "testInterpreter", "testUserDefinedFields", "testSensorCategory");
		sensorTypeDaoImplementation.addSensorType("testSensorTypeName4", "testManufacturer", "0.1", 100, 1, "Fahrenheit", "testInterpreter", "testUserDefinedFields", "testSensorCategory");
		sensorTypeDaoImplementation.addSensorType("testSensorTypeName5", "testManufacturer", "0.1", 100, 1, "Fahrenheit", "testInterpreter", "testUserDefinedFields", "testSensorCategory");

		int post = sensorTypeDaoImplementation.getAllSensorTypes().size();
		assertEquals(3, post - prev);
	}
}
