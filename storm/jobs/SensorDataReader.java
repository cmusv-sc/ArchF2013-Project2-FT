package storm.jobs;

public interface SensorDataReader {
	String getNextSensorId();
	String getNextSensorData();
	boolean hasNextSensorData();
}
