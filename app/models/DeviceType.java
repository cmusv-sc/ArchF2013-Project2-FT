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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceType {
	private String deviceTypeName;
	private String manufacturer;
	private String version;
	private String deviceTypeUserDefinedFields;
	private List<String> sensorTypeNames;
	public void setSensorTypeNames(List<String> sensorTypeNames) {
		this.sensorTypeNames = sensorTypeNames;
	}
	public String getDeviceTypeName() {
		return deviceTypeName;
	}
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDeviceTypeUserDefinedFields() {
		return deviceTypeUserDefinedFields;
	}
	public void setDeviceTypeUserDefinedFields(String deviceTypeUserDefinedFields) {
		this.deviceTypeUserDefinedFields = deviceTypeUserDefinedFields;
	}
	public List<String> getSensorTypeNames() {
		return sensorTypeNames;
	}
	public void addSensorTypes(List<String> sensorTypes) {
		this.sensorTypeNames = sensorTypes;
	}
	
	public String toCSVString() {
		return "TODO";
	}
	public String getCSVHeader() {
		return "device_type_name,manufacturer,version,user_defined_fields, sensor_type_names\n";
	}
	
	public String toJSONString() {
		String jsonString = new String();
		try {
			JSONObject obj=new JSONObject();
			obj.put("device_type_name",  deviceTypeName);
			obj.put("manufacturer", manufacturer);
			obj.put("version", version);

			obj.put("device_type_user_defined_fields", deviceTypeUserDefinedFields);
			
			if (sensorTypeNames.size() > 0) {
				Object[] content = new Object[sensorTypeNames.size()];
				int i = 0;
				for (String sensorType : sensorTypeNames) {
					content[i] = sensorType;
					i++;
				}
				JSONArray arr = new JSONArray(content);
				obj.put("sensor_type_names", arr);
			}
			
			jsonString = obj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonString;
	}

}
