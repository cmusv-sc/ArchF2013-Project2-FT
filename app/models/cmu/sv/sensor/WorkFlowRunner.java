package models.cmu.sv.sensor;

import java.util.Date;

public class WorkFlowRunner {
		private String virtualDeviceID;
		private String topic;
		private MessageBusHandler handler; 
		public WorkFlowRunner(String virtualDeviceID, String topic){
			this.virtualDeviceID = virtualDeviceID;
			this.topic = topic;
			this.handler = new MessageBusHandler();
		}
		
		public boolean notifyVirtualDevice(double value){
			Long timeStamp = new Date().getTime();
			SensorReading reading = new SensorReading(this.virtualDeviceID, timeStamp, topic, value);
			return handler.publish(reading);
		}
}
