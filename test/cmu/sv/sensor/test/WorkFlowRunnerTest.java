package cmu.sv.sensor.test;

import static org.junit.Assert.*;

import models.cmu.sv.sensor.WorkFlowRunner;

import org.junit.Test;

public class WorkFlowRunnerTest {
	@Test
	public void testThreshold(){
		WorkFlowRunner runner = new WorkFlowRunner("WFRun", "virtual_device", "Interval");
		runner.computeThreshold();
	}
	
	@Test
	public void testNotify() {
		WorkFlowRunner runner = new WorkFlowRunner("WFRun", "virtual_device", "Interval");
		assertEquals("Notify should be success", true, runner.notifyVirtualDevice(20));
	}
	
	
}
