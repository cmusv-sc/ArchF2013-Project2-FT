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

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Device extends DeviceType {
	private String uri;
	private String deviceUserDefinedFields;
	private Location location;
	private List<String> sensorNames;
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDeviceUserDefinedFields() {
		return deviceUserDefinedFields;
	}

	public void setDeviceUserDefinedFields(String deviceUserDefinedFields) {
		this.deviceUserDefinedFields = deviceUserDefinedFields;
	}
		
	public String getCSVHeader() {
		return "uri,device_type,device_agent,device_location\n";
	}
	
	public String toCSVString() {
		return uri + "," + deviceUserDefinedFields;
	}
	
	public String toJSONString() {
		String jsonString = new String();
		try {
			JSONObject obj=new JSONObject();
			obj.put("device_agent", uri);
			obj.put("device_location", deviceUserDefinedFields);
			jsonString = obj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonString;
	}

	public List<String> getSensorNames() {
		return sensorNames;
	}

	public void setSensorNames(List<String> sensorNames) {
		this.sensorNames = sensorNames;
	}

}
