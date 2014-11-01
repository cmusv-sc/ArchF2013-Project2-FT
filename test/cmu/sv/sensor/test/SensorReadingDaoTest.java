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

import java.util.List;

import models.OldSensorReading;
import models.dao.OldSensorReadingDaoImplementation;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SensorReadingDaoTest extends AbstractTest{
	/*
	private static OldSensorReadingDaoImplementation sensorReadingDao;
	
	@BeforeClass
	public static void subSetup() {
		sensorReadingDao = new OldSensorReadingDaoImplementation();
		sensorReadingDao.setSimpleJdbcTemplate(jdbcTemplate);
	}
	
	@Test
	public void testLastestReadingFromAllDevices() {
		List<OldSensorReading> readings = sensorReadingDao.lastestReadingFromAllDevices("light");
		assertEquals("device1,1383927322,light,200.0", readings.get(0).toCSVString());
		assertEquals("device2,1383927322,light,200.0", readings.get(1).toCSVString());
		
		readings = sensorReadingDao.lastestReadingFromAllDevices("temp");
		assertEquals("device1,1383927322,temp,16.0", readings.get(0).toCSVString());
		assertEquals("device2,1383927322,temp,16.0", readings.get(1).toCSVString());
	}
	
	@Test
	public void testSearchSensorReadingInSpecificTimestamp() {
		OldSensorReading reading = sensorReadingDao.searchReading("device1", new Long(1383927322), "light");
		assertEquals("device1,1383927322,light,200.0", reading.toCSVString());
		reading = sensorReadingDao.searchReading("device2", new Long(1383927322), "light");
		assertEquals("device2,1383927322,light,200.0", reading.toCSVString());
		
		reading = sensorReadingDao.searchReading("device1", new Long(1383927322), "temp");
		assertEquals("device1,1383927322,temp,16.0", reading.toCSVString());
	}
	
	@Test
	public void testSearchSensorReadingInTimestampRange() {
		List<OldSensorReading> readings = sensorReadingDao.searchReading("device1", new Long(1383927321), new Long(1383927322), "light");
		assertEquals("device1,1383927322,light,200.0", readings.get(0).toCSVString());
		assertEquals("device1,1383927321,light,200.0", readings.get(1).toCSVString());
		
		readings = sensorReadingDao.searchReading("device2", new Long(1383927321), new Long(1383927322), "temp");
		assertEquals("device2,1383927322,temp,16.0", readings.get(0).toCSVString());
		assertEquals("device2,1383927321,temp,15.0", readings.get(1).toCSVString());
	}
	
	//Please pass this test
	@Test
	public void testAddSensorReading() {
		sensorReadingDao.addReading("device3", new Long(1383927321), "temp", 15.0);
		sensorReadingDao.addReading("device3", new Long(1383927321), "light", 200.0);
		
		OldSensorReading reading = sensorReadingDao.searchReading("device3", new Long(1383927321), "temp");
		assertEquals("device3,1383927321,temp,15.0", reading.toCSVString());
		reading = sensorReadingDao.searchReading("device3", new Long(1383927321), "light");
		assertEquals("device3,1383927321,light,200.0", reading.toCSVString());
	}
	*/
}
