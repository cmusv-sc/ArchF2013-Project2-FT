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
