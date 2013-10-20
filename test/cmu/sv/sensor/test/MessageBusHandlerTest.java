package cmu.sv.sensor.test;

import static org.junit.Assert.assertEquals;
import models.MessageBusHandler;
import models.SensorReading;

import org.junit.BeforeClass;
import org.junit.Test;

public class MessageBusHandlerTest {

	private static MessageBusHandler handler; 
	@BeforeClass
	public static void testSetup(){
		handler = new MessageBusHandler();
	}
	
	@Test
	public void testIsTopicExists() {
		assertEquals(true, handler.isTopicExists("SwitchPower_StatusReport"));
		assertEquals(false, handler.isTopicExists("SwitchPower2_StatusReport"));
	}
	@Test
	public void testAddTopic(){
		assertEquals(true, handler.addTopic("test-topic"));
	}
	@Test
	public void testPublishData(){
		SensorReading reading = new SensorReading("test-Device", new Long(1), "test-topic", 1.0);
		assertEquals(true, handler.publish(reading));
		
	}
}
