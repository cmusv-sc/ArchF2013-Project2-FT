package helper;

import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Timestamp;

public class Utils {
	public static String convertTimestampToReadable(Long timestamp) {
		Timestamp t = new Timestamp(timestamp);
		return t.toString();
	}

	public static String getJSONString(String deviceId, String time, String sensorType, double value) {
		String result = new String();
		try {
			JSONObject obj = new JSONObject();
			obj.put("device_id",  deviceId);
			obj.put("time", time);
			obj.put("sensor_type", sensorType);
			obj.put("value", value);
			result = obj.toString();
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getCSVString(String deviceId, String time, String sensorType, double value) {
		return deviceId + "," + time + "," + sensorType + "," + String.valueOf(value);
	}
}
