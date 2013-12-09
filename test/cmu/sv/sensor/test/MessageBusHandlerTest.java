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
import models.MessageBusHandler;
import models.OldSensorReading;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
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
		OldSensorReading reading = new OldSensorReading("test-Device", new Long(1), "test-topic", 1.0);
		assertEquals(true, handler.publish(reading));
		
	}
}
