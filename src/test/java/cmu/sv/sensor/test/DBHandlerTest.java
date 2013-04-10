package cmu.sv.sensor.test;
import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.BeforeClass;
import org.junit.Test;

import cmu.sv.sensor.DBHandler;
import cmu.sv.sensor.SensorReading;


public class DBHandlerTest {
	private static DBHandler dbHandler;
	
	@BeforeClass
	public static void testSetup() throws FileNotFoundException{
		dbHandler = new DBHandler(new FileInputStream("src/test/conf/database.properties"));
	}
	
	
	@Test
	public void testMakeConnection(){
		
		assertEquals("dbHandler should be able to make connection to the server", dbHandler.makeConnection(), true);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testAddReadings(){
		dbHandler.addReading(1, 1, "Temperature", 450.0);
		SensorReading reading = dbHandler.searchReading(1, 1);
		
		assertEquals("The deviceID of the reading should be the same", reading.getDeviceID(), 1);
		assertEquals("The timeStamp of the reading should be the same", reading.getTimeStamp(), 1);
		assertEquals("The SensorType of the reading should be the same", reading.getSensorType(), "Temperature");
		assertEquals("The Value of the reading should be the same", reading.getValue(), 450.0, 0.1f);
	}

}
