package models.cmu.sv.sensor;

public class SensorReading {
	private Long deviceId;
	private Long timeStamp;
	private String sensorType;
	private double value;
	
	public SensorReading(Long deviceId, Long timeStamp, String sensorType, double value){
		this.deviceId = deviceId;
		this.timeStamp = timeStamp;
		this.sensorType = sensorType;
		this.value = value;
	}
	
	public Long getDeviceId(){
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
