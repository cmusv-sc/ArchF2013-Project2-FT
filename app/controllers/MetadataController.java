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
package controllers;
import com.fasterxml.jackson.databind.JsonNode;
import models.DBHandler;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;

//import models.cmu.sv.sensor.SensorReading;

public class MetadataController extends Controller {
	private static DBHandler dbHandler = null;
	private static boolean testDBHandler(){
		if(dbHandler == null){
			dbHandler = new DBHandler("conf/database.properties");
		}
		return true;
	}

	public static Result addDeviceType() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} 
		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}

		// Parse JSON FIle 
		String deviceTypeName= json.findPath("device_type_name").textValue();
		String manufacturer = json.findPath("manufacturer").textValue();
		String version = json.findPath("version").textValue();
		String userDefinedFields = json.findPath("user_defined_fields").textValue();
		ArrayList<String> error = new ArrayList<String>();

		boolean result = dbHandler.addDeviceType(deviceTypeName, manufacturer, version, userDefinedFields);

		if(!result){
			error.add(deviceTypeName);
		}

		if(error.size() == 0){
			System.out.println("device type saved");
			return ok("device type saved");
		}
		else{
			System.out.println("some device types not saved: " + error.toString());
			return ok("some device types not saved: " + error.toString());
		}
	}

	public static Result addDevice() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} 
		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}

		// Parse JSON FIle 
		String deviceType= json.findPath("device_type").textValue();
		String deviceAgent= json.findPath("device_agent").textValue();
		String networkAddress = json.findPath("network_address").textValue();
		String locationDescription = json.findPath("location_description").textValue();
		String latitude = json.findPath("latitude").textValue();
		String longitude = json.findPath("longitude").textValue();
		String altitude = json.findPath("altitude").textValue();
		String positionFormatSystem = json.findPath("position_format_system").textValue();
		String userDefinedFields = json.findPath("user_defined_fields").textValue();
		ArrayList<String> error = new ArrayList<String>();

		boolean result = dbHandler.addDeviceNew(deviceType, deviceAgent, networkAddress, locationDescription, latitude, longitude, altitude, positionFormatSystem, userDefinedFields);

		if(!result){
			error.add(deviceType);
		}

		if(error.size() == 0){
			System.out.println("device saved");
			return ok("device saved");
		}
		else{
			System.out.println("some devices not saved: " + error.toString());
			return ok("some devices not saved: " + error.toString());
		}
	}

	public static Result addSensorType() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} 
		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}

		// Parse JSON FIle 
		String sensorType = json.findPath("sensor_type").textValue();
		String userDefinedFields = json.findPath("user_defined_fields").textValue();
		ArrayList<String> error = new ArrayList<String>();

		boolean result = dbHandler.addSensorType(sensorType, userDefinedFields);

		if(!result){
			error.add(sensorType);
		}

		if(error.size() == 0){
			System.out.println("sensor type saved");
			return ok("sensor type saved");
		}
		else{
			System.out.println("some sensor types not saved: " + error.toString());
			return ok("some sensor types not saved: " + error.toString());
		}
	}

	public static Result addSensor() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} 
		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}

		// Parse JSON FIle 
		String printName= json.findPath("print_name").textValue();
		String sensorType= json.findPath("sensor_type").textValue();
		String deviceId = json.findPath("device_id").textValue();
		String userDefinedFields = json.findPath("user_defined_fields").textValue();
		ArrayList<String> error = new ArrayList<String>();

		boolean result = dbHandler.addSensor(printName, sensorType, deviceId, userDefinedFields);

		if(!result){
			error.add(sensorType);
		}

		if(error.size() == 0){
			System.out.println("sensor saved");
			return ok("sensor saved");
		}
		else{
			System.out.println("some sensors not saved: " + error.toString());
			return ok("some sensors not saved: " + error.toString());
		}
	}
	// query for readings
	public static Result sql_query(){
		String resultStr = "";

		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}
		try {
			response().setHeader("Access-Control-Allow-Origin", "*");
			JsonNode sql_json = request().body().asJson();
			if (sql_json == null) {
				return badRequest("Expect sql in valid json");
			}
			String sql = sql_json.findPath("sql").textValue();
			int number_of_result_columns = sql_json.findPath("number_of_result_columns").intValue();
			resultStr = dbHandler.runQuery(sql, number_of_result_columns);
		} catch(Exception e){
			e.printStackTrace();
		}
		return ok(resultStr);		
	}

	// search reading at a specific timestamp
	public static Result searchAtTimestamp(String deviceId, Long timeStamp, String sensorType, String format){
		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		models.OldSensorReading reading = dbHandler.searchReading(deviceId, timeStamp, sensorType);
		if(reading == null){
			return notFound("no reading found");
		}
		String ret = format.equals("json") ? reading.toJSONString() : reading.toCSVString(); 
		return ok(ret);
	}

}
