package models.cmu.sv.sensor;

public class SensorReading {
	private int deviceId;
	private int timeStamp;
	private String sensorType;
	private double value;
	
	public SensorReading(int deviceId, int timeStamp, String sensorType, double value){
		this.deviceId = deviceId;
		this.timeStamp = timeStamp;
		this.sensorType = sensorType;
		this.value = value;
	}
	
	public int getDeviceId(){
		return deviceId;
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
