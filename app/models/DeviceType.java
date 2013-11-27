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
	private List<String> sensorTypes;
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
	public List<String> getSensorTypes() {
		return sensorTypes;
	}
	public void addSensorTypes(List<String> sensorTypes) {
		this.sensorTypes = sensorTypes;
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
			
			if (sensorTypes.size() > 0) {
				Object[] content = new Object[sensorTypes.size()];
				int i = 0;
				for (String sensorType : sensorTypes) {
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
