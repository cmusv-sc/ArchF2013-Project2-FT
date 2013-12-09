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
import models.SensorCategory;
import models.dao.SensorCategoryDaoImplementation;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SensorCategoryDaoTest extends AbstractTest{
	private static SensorCategoryDaoImplementation sensorCategoryDaoImplementation;
	
	@BeforeClass
	public static void subSetup() {
		sensorCategoryDaoImplementation = new SensorCategoryDaoImplementation();
		sensorCategoryDaoImplementation.setSimpleJdbcTemplate(jdbcTemplate);
	}
	
	@Test
	public void testAddSensorCategory() {
		String purpose = "for test";
		sensorCategoryDaoImplementation.addSensorCategory("testSensorCategoryName", purpose);
		SensorCategory sc = sensorCategoryDaoImplementation.getSensorCategory("testSensorCategoryName");
		assertEquals(purpose, sc.getPurpose());
		assertEquals(2, sensorCategoryDaoImplementation.getAllSensorCategories().size());
		
		sensorCategoryDaoImplementation.addSensorCategory("testSensorCategoryName2", "for test 2");
		assertEquals(3, sensorCategoryDaoImplementation.getAllSensorCategories().size());
		
		//negative test for adding the same sensor_category_name
		sensorCategoryDaoImplementation.addSensorCategory("testSensorCategoryName", "for test 2");
		assertEquals(3, sensorCategoryDaoImplementation.getAllSensorCategories().size());
	}
	
	@Test
	@Ignore
	public void testGetSensorCategory() {
		
	}
	@Test
	@Ignore
	public void testGetAllSensorCategory() {
		
	}
}
