package controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.*;

import models.cmu.sv.sensor.DBHandler;
import models.cmu.sv.sensor.MessageBusHandler;
//import models.cmu.sv.sensor.SensorReading;
import helper.Utils;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.Timestamp;

import org.codehaus.jackson.JsonNode;

import play.mvc.Controller;
import play.mvc.Result;


public class SensorReading extends Controller {
	private static DBHandler dbHandler = null;
	private static final String ISO8601 = "ISO8601";
	private static boolean testDBHandler(){
		if(dbHandler == null){
			dbHandler = new DBHandler("conf/database.properties");
		}
		return true;
	}

	public static Result add(Boolean publish) {
		JsonNode json = request().body().asJson();
		if(json == null) {              
			return badRequest("Expecting Json data");
		}
		if(!testDBHandler()){           
			return internalServerError("database conf file not found");
		}
		
		// Parse JSON FIle
		String deviceId = json.findPath("id").getTextValue();
		Long timeStamp = json.findPath("timestamp").getLongValue();
		Iterator<String> it = json.getFieldNames();
		ArrayList<String> error = new ArrayList<String>();
		while(it.hasNext()){            
			String sensorType = it.next();  
			if(sensorType == "id" || sensorType == "timestamp") continue;
			double value = json.findPath(sensorType).getDoubleValue();
			models.cmu.sv.sensor.SensorReading reading = new models.cmu.sv.sensor.SensorReading(deviceId, timeStamp, sensorType, value);  
			if(!reading.save()){            
				//error.add(sensorType);          
				error.add(sensorType + ", " + deviceId + ", " + timeStamp.toString() + ", " + value + "\n");
			}

			if(publish){                    
				MessageBusHandler mb = new MessageBusHandler();
				if(!mb.publish(reading)){       
					error.add("publish failed");    
				}                               
			}
		}
		if(error.size() == 0){          
			System.out.println("saved");    
			return ok("saved");             
		}
		else{
			System.out.println("some not saved: " + error.toString());
			return ok("some not saved: " + error.toString());
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
			String sql = sql_json.findPath("sql").getTextValue();
			int number_of_result_columns = sql_json.findPath("number_of_result_columns").getIntValue();			
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
		models.cmu.sv.sensor.SensorReading reading = dbHandler.searchReading(deviceId, timeStamp, sensorType);
		if(reading == null){
			return notFound("no reading found");
		}
		String ret = format.equals("json") ? reading.toJSONString() : reading.toCSVString(); 
		return ok(ret);
	}

	private static Long convertTimeToTimestamp(String timeString) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy'T'HH:mm:ss");
		Long result = null;
		Date date = dateFormat.parse(timeString);
		System.out.println(date);
		Timestamp timestamp = new Timestamp(date.getTime());
		result = timestamp.getTime();
		System.out.println(result);
		return result;
	}

	// search reading at a specific readable time 
	public static Result searchAtTime(String deviceId, String time, String sensorType, String format){
		if (!ISO8601.equals(getDateFormat())) {
			Long timestamp = null;
			try {
				timestamp = Long.parseLong(time, 10);
			} catch(NumberFormatException ex) {
				return badRequest("Date format or value is incorrect, please check APIs!");
			}
			if (null == timestamp) {
				return badRequest("Date format or value is incorrect, please check APIs!");
			}
			return searchAtTimestamp(deviceId, timestamp, sensorType, format);
		} 

		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}

		response().setHeader("Access-Control-Allow-Origin", "*");
		Long timeStamp = null;
		try {
			timeStamp = convertTimeToTimestamp(time);
		} catch(ParseException ex) {
			return badRequest("Date format or value is incorrect, please check APIs!");
		}
		models.cmu.sv.sensor.SensorReading reading = dbHandler.searchReading(deviceId, timeStamp, sensorType);
		if(reading == null){
			return notFound("no reading found");
		}
		String readableTime = Utils.convertTimestampToReadable(reading.getTimeStamp());
		String ret = format.equals("json") ? 
			Utils.getJSONString(reading.getDeviceId(), readableTime, reading.getSensorType(), reading.getValue()):
			Utils.getCSVString(reading.getDeviceId(), readableTime, reading.getSensorType(), reading.getValue());
		return ok(ret);
	}

	// search readings of timestamp range [startTime, endTime]
	public static Result searchInTimestampRange(String deviceId, Long startTime, long endTime, String sensorType, String format){
		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		ArrayList<models.cmu.sv.sensor.SensorReading> readings = dbHandler.searchReading(deviceId, startTime, endTime, sensorType);
		if(readings == null || readings.isEmpty()){
			return notFound("no reading found");
		}
		//String ret = new String();
		StringBuilder strBuilder = new StringBuilder();
		if (format.equals("json"))
		{			
			for (models.cmu.sv.sensor.SensorReading reading : readings) {
				//if (ret.isEmpty())
				if (strBuilder.length() == 0)
					//ret += "[";
					strBuilder.append("[");
				else				
					//ret += ',';			
					strBuilder.append(",");
				//ret += reading.toJSONString();
				strBuilder.append(reading.toJSONString());
			}
			//ret += "]";			
			strBuilder.append("]");			
		} else {
			for (models.cmu.sv.sensor.SensorReading reading : readings) {
				//if (!ret.isEmpty())
				if (strBuilder.length() != 0)
					//ret += '\n';
					strBuilder.append("\n");
				//ret += reading.toCSVString();
				strBuilder.append(reading.toCSVString());
			}
		}

		//return ok(ret);
		return ok(strBuilder.toString());
	}

	// search readings of time range [startTime, endTime]
	public static Result searchInTimeRange(String deviceId, String startTime, String endTime, String sensorType, String format){
		if (!ISO8601.equals(getDateFormat())) {
			Long startTimestamp = null;
			Long endTimestamp = null;
			try {
				startTimestamp = Long.parseLong(startTime, 10);
				endTimestamp = Long.parseLong(endTime, 10);
			} catch(NumberFormatException ex) {
				return badRequest("Date format or value is incorrect, please check APIs!");
			}
			if (null == startTimestamp && null == endTimestamp) {
				return badRequest("Date format or value is incorrect, please check APIs!");
			}
			return searchInTimestampRange(deviceId, startTimestamp, endTimestamp, sensorType, format);
		} 
		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		Long startTimestamp = null;
		Long endTimestamp = null;
		try {
			startTimestamp = convertTimeToTimestamp(startTime);
			endTimestamp = convertTimeToTimestamp(endTime);
		} catch(ParseException ex) {
			return badRequest("Date format or value is incorrect, please check APIs!");
		}
		ArrayList<models.cmu.sv.sensor.SensorReading> readings = dbHandler.searchReading(deviceId, startTimestamp, endTimestamp, sensorType);
		if(readings == null || readings.isEmpty()){
			return notFound("no reading found");
		}
//		String ret = new String();
		StringBuilder strBuilder = new StringBuilder();
		if (format.equals("json"))
		{			
			for (models.cmu.sv.sensor.SensorReading reading : readings) {
				//if (ret.isEmpty())
				if (strBuilder.length() == 0)
					//ret += "[";
					strBuilder.append("[");
				else				
					//ret += ',';			
					strBuilder.append(",");
				//ret += reading.toJSONString();
				String readableTime = Utils.convertTimestampToReadable(reading.getTimeStamp());
				//ret += Utils.getJSONString(reading.getDeviceId(), readableTime, reading.getSensorType(), reading.getValue());
				strBuilder.append(Utils.getJSONString(reading.getDeviceId(), readableTime, reading.getSensorType(), reading.getValue()));
			}
			//ret += "]";			
			strBuilder.append("]");			
		} else {
			for (models.cmu.sv.sensor.SensorReading reading : readings) {
				//if (!ret.isEmpty())
				if (strBuilder.length() != 0)
					//ret += '\n';
					strBuilder.append("\n");
				String readableTime = Utils.convertTimestampToReadable(reading.getTimeStamp());
//				ret += Utils.getCSVString(reading.getDeviceId(), readableTime, reading.getSensorType(), reading.getValue());
				//ret += reading.toCSVString();
				strBuilder.append(Utils.getCSVString(reading.getDeviceId(), readableTime, reading.getSensorType(), reading.getValue()));
			}
		}

		return ok(strBuilder.toString());
	}

	public static Result lastReadingFromAllDevices(Long timeStamp, String sensorType, String format) {
		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}

		response().setHeader("Access-Control-Allow-Origin", "*");
		ArrayList<models.cmu.sv.sensor.SensorReading> readings = dbHandler.lastReadingFromAllDevices(timeStamp, sensorType);
		if(readings == null || readings.isEmpty()){
			return notFound("no reading found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			for (models.cmu.sv.sensor.SensorReading reading : readings) {
				if (ret.isEmpty())
					ret += "[";
				else				
					ret += ',';			
				ret += reading.toJSONString();
			}
			ret += "]";			
		} else {
			for (models.cmu.sv.sensor.SensorReading reading : readings) {
				if (!ret.isEmpty())
					ret += '\n';
				ret += reading.toCSVString();
			}
		}
		return ok(ret);
	}

	private static String getDateFormat() {
		String dateFormat = null;
		final Map<String,String[]> entries = request().queryString();
		if (!entries.isEmpty() && entries.containsKey("dateformat")) {
			dateFormat = entries.get("dateformat")[0];
		}
		return dateFormat;

	}	
	public static Result lastestReadingFromAllDevices(String sensorType, String format) {
		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}

		String dateFormat = getDateFormat();

		response().setHeader("Access-Control-Allow-Origin", "*");
		ArrayList<models.cmu.sv.sensor.SensorReading> readings = dbHandler.lastestReadingFromAllDevices(sensorType);
		if(readings == null || readings.isEmpty()){
			return notFound("no reading found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			for (models.cmu.sv.sensor.SensorReading reading : readings) {
				if (ret.isEmpty())
					ret += "[";
				else				
					ret += ',';			
				if (ISO8601.equals(dateFormat)) {
					String readableTime = Utils.convertTimestampToReadable(reading.getTimeStamp());
					String jsonString = Utils.getJSONString(reading.getDeviceId(), readableTime, reading.getSensorType(), reading.getValue());
					ret += jsonString;
				} else {
				 	ret += reading.toJSONString();
				}
			}
			ret += "]";			
		} else {
			for (models.cmu.sv.sensor.SensorReading reading : readings) {
				if (!ret.isEmpty())
					ret += '\n';
				ret += reading.toCSVString();
			}
		}
		return ok(ret);
	}	
		
}
