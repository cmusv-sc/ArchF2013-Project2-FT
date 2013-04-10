package cmu.sv.sensor;

public class SensorReading {
	private int deviceID;
	private int timeStamp;
	private String sensorType;
	private double value;
	
	public SensorReading(int deviceID, int timeStamp, String sensorType, double value){
		this.deviceID = deviceID;
		this.timeStamp = timeStamp;
		this.sensorType = sensorType;
		this.value = value;
	}
	
	public int getDeviceID(){
		return deviceID;
	}
	
	public int getTimeStamp(){
		return timeStamp;
	}
	
	public String getSensorType(){
		return sensorType;
	}
	
	public double getValue(){
		return value;
	}
}
