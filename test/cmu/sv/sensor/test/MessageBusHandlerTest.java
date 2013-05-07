package cmu.sv.sensor.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import models.cmu.sv.sensor.DBHandler;
import models.cmu.sv.sensor.MessageBusHandler;

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

}
