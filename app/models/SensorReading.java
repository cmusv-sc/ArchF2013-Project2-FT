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
package models;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

public class SensorReading {
	private String sensorName;
	private Boolean isIndoor;
	private Timestamp timeStamp;
	private String locationInterpreter;
	private String value;
	private Double longitude;
	private Double latitude;
	private Double altitude;
	
	
		
	public String getCSVHeader() {
		return "sensorName,timestamp,value\n";
	}
	
	public String toCSVString() {
		return sensorName + "," + timeStamp.getTime() + "," + String.valueOf(value);
	}
	
	public String toJSONString() {
		String jsonString = new String();
		try {
			JSONObject obj=new JSONObject();
			obj.put("sensorName",  sensorName);
			obj.put("timestamp", timeStamp);
			obj.put("value", value);
			jsonString = obj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonString;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public Boolean getIsIndoor() {
		return isIndoor;
	}

	public void setIsIndoor(Boolean isIndoor) {
		this.isIndoor = isIndoor;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getLocationInterpreter() {
		return locationInterpreter;
	}

	public void setLocationInterpreter(String locationInterpreter) {
		this.locationInterpreter = locationInterpreter;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getAltitude() {
		return altitude;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

}
