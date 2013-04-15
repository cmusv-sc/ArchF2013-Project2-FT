package models.cmu.sv.sensor;

public class SensorReading {
	private String deviceId;
	private Long timeStamp;
	private String sensorType;
	private double value;
	
	public SensorReading(String deviceId, Long timeStamp, String sensorType, double value){
		this.deviceId = deviceId;
		this.timeStamp = timeStamp;
		this.sensorType = sensorType;
		this.value = value;
	}
	
	public String getDeviceId(){
		return deviceId;
	}
	
	public Long getTimeStamp(){
		return timeStamp;
	}
	
	public String getSensorType(){
		return sensorType;
	}
	
	public double getValue(){
		return value;
	}
}
