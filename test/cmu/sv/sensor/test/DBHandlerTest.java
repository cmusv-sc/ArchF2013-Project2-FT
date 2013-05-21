package cmu.sv.sensor.test;
import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import models.cmu.sv.sensor.DBHandler;
import models.cmu.sv.sensor.SensorReading;

import org.junit.BeforeClass;
import org.junit.Test;


public class DBHandlerTest {
	private static DBHandler dbHandler;
	
	@BeforeClass
	public static void testSetup() throws FileNotFoundException{
		dbHandler = new DBHandler("conf/database.properties");
	}
	
	
	@Test
	public void testMakeConnection(){
		
		assertEquals("dbHandler should be able to make connection to the server", dbHandler.makeConnection(), true);
	}
	
	@Test
	public void testAddReadings(){
		//assertEquals("The reading should be successfully added", true, dbHandler.addReading("test-1", new Long(1), "Temperature", 450.0));
		//assertEquals("The reading should not be added because of duplicate key", dbHandler.addReading("test-1", new Long(1), "Temperature", 300.0), false);
		SensorReading reading = dbHandler.searchReading("test-1", new Long(1), "Temperature");		
		
		assertEquals("The deviceID of the reading should be the same", reading.getDeviceId(), "test-1");
		assertEquals("The timeStamp of the reading should be the same", reading.getTimeStamp(), new Long(1));
		assertEquals("The SensorType of the reading should be the same", reading.getSensorType(), "Temperature");
		assertEquals("The Value of the reading should be the same", reading.getValue(), 450.0, 0.1f);
		assertEquals("The reading should be null for nonexist query", dbHandler.searchReading("test-1", new Long(2), "Temperature"), null);
		assertEquals("The reading should be null for nonexist query", dbHandler.lastReadingFromAllDevices(new Long(2), "Temperature"), null);		
		dbHandler.deleteReading(reading.getDeviceId(), reading.getTimeStamp(), reading.getSensorType());
	}

}
