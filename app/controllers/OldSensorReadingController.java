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

import helper.Utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.MessageBusHandler;
import models.OldSensorReading;
import models.dao.OldSensorReadingDao;

import org.codehaus.jackson.JsonNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import play.mvc.Controller;
import play.mvc.Result;
//import models.cmu.sv.sensor.SensorReading;


public class OldSensorReadingController extends Controller {
	private static final String ISO8601 = "ISO8601";
	private static ApplicationContext context;
	private static OldSensorReadingDao sensorReadingDao;
	
	private static boolean checkDao(){
		if (context == null) {
			context = new ClassPathXmlApplicationContext("application-context.xml");
		}
		if (sensorReadingDao == null) {
			sensorReadingDao = (OldSensorReadingDao) context.getBean("sensorReadingDaoImplementation");
		}
		
		return true;
	}

	public static Result add(Boolean publish) throws Exception {
		JsonNode json = request().body().asJson();
		if(json == null) {              
			return badRequest("Expecting Json data");
		}
		if(!checkDao()){           
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
			if(!sensorReadingDao.addReading(deviceId, timeStamp, sensorType, value)){            
				error.add(sensorType + ", " + deviceId + ", " + timeStamp.toString() + ", " + value + "\n");
			}

			if(publish){                    
				MessageBusHandler mb = new MessageBusHandler();
				if(!mb.publish(new models.OldSensorReading(deviceId, timeStamp, sensorType, value))){       
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
	
	// search reading at a specific timestamp
	public static Result searchAtTimestamp(String deviceId, Long timeStamp, String sensorType, String format){
		response().setHeader("Access-Control-Allow-Origin", "*");
//		SensorReading reading = dbHandler.searchReading(deviceId, timeStamp, sensorType);
		checkDao();
		OldSensorReading reading = sensorReadingDao.searchReading(deviceId, timeStamp, sensorType);
		
		if(reading == null){
			return notFound("no reading found");
		}
		
		String ret = null;
		
		if (format.equals("csv")) {
			ret = reading.getCSVHeader();
			ret += reading.toCSVString();
		} else {
			ret = reading.toJSONString();
		}
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
		
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}

		response().setHeader("Access-Control-Allow-Origin", "*");
		Long timeStamp = null;
		try {
			timeStamp = convertTimeToTimestamp(time);
		} catch(ParseException ex) {
			return badRequest("Date format or value is incorrect, please check APIs!");
		}
		models.OldSensorReading reading = sensorReadingDao.searchReading(deviceId, timeStamp, sensorType);
		if(reading == null){
			return notFound("no reading found");
		}
		String readableTime = Utils.convertTimestampToReadable(reading.getTimeStamp());
//		String ret = format.equals("json") ? 
//			Utils.getJSONString(reading.getDeviceId(), readableTime, reading.getSensorType(), reading.getValue()):
//			Utils.getCSVString(reading.getDeviceId(), readableTime, reading.getSensorType(), reading.getValue());
		String ret = null;
		if (format.equals("csv")) {
			ret = reading.getCSVHeader();
			ret += Utils.getCSVString(reading.getDeviceId(), readableTime, reading.getSensorType(), reading.getValue());
		} else {
			ret = Utils.getJSONString(reading.getDeviceId(), readableTime, reading.getSensorType(), reading.getValue());
		}
		return ok(ret);
	}

	// search readings of timestamp range [startTime, endTime]
	public static Result searchInTimestampRange(String deviceId, Long startTime, Long endTime, String sensorType, String format){
		response().setHeader("Access-Control-Allow-Origin", "*");

		if(!checkDao()){
			return internalServerError("database conf file not found");
		}
		
		List<OldSensorReading> readings = sensorReadingDao.searchReading(deviceId, startTime, endTime, sensorType);
		if(readings == null || readings.isEmpty()){
			return notFound("no reading found");
		}
		StringBuilder strBuilder = new StringBuilder();
		if (format.equals("json")) {			
			for (models.OldSensorReading reading : readings) {
				if (strBuilder.length() == 0){
					strBuilder.append("[");
				}else{
					strBuilder.append(",");
				}				
				strBuilder.append(reading.toJSONString());
			}
			strBuilder.append("]");			
		} else {
			for (models.OldSensorReading reading : readings) {
				if (strBuilder.length() != 0){
					strBuilder.append("\n");
				}else{
					strBuilder.append(reading.getCSVHeader());
				} 
				strBuilder.append(reading.toCSVString());
			}
		}

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
		
		if(!checkDao()){
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
		List<models.OldSensorReading> readings = sensorReadingDao.searchReading(deviceId, startTimestamp, endTimestamp, sensorType);
		if(readings == null || readings.isEmpty()){
			return notFound("no reading found");
		}
		StringBuilder strBuilder = new StringBuilder();
		if (format.equals("json"))
		{			
			for (models.OldSensorReading reading : readings) {
				if (strBuilder.length() == 0)
					strBuilder.append("[");
				else				
					strBuilder.append(",");
				String readableTime = Utils.convertTimestampToReadable(reading.getTimeStamp());
				strBuilder.append(Utils.getJSONString(reading.getDeviceId(), readableTime, reading.getSensorType(), reading.getValue()));
			}
			strBuilder.append("]");			
		} else {
			for (models.OldSensorReading reading : readings) {
				if (strBuilder.length() != 0)
					strBuilder.append("\n");
				else 
					strBuilder.append(reading.getCSVHeader());
				String readableTime = Utils.convertTimestampToReadable(reading.getTimeStamp());
				strBuilder.append(Utils.getCSVString(reading.getDeviceId(), readableTime, reading.getSensorType(), reading.getValue()));
			}
		}

		return ok(strBuilder.toString());
	}

	public static Result lastReadingFromAllDevices(Long timeStamp, String sensorType, String format) {
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}

		response().setHeader("Access-Control-Allow-Origin", "*");
		List<models.OldSensorReading> readings = sensorReadingDao.lastReadingFromAllDevices(timeStamp, sensorType);
		if(readings == null || readings.isEmpty()){
			return notFound("no reading found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			for (models.OldSensorReading reading : readings) {
				if (ret.isEmpty())
					ret += "[";
				else				
					ret += ',';			
				ret += reading.toJSONString();
			}
			ret += "]";			
		} else {
			for (models.OldSensorReading reading : readings) {
				if (!ret.isEmpty())
					ret += '\n';
				else 
					ret += reading.getCSVHeader();
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
	public static Result latestReadingFromAllDevices(String sensorType, String format) {
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}
		String dateFormat = getDateFormat();

		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		List<OldSensorReading> readings = sensorReadingDao.latestReadingFromAllDevices(sensorType);

		if(readings == null || readings.isEmpty()){
			return notFound("no reading found");
		}
		StringBuilder sb = new StringBuilder();
		if (format.equals("json")) {			
			for (models.OldSensorReading reading : readings) {
				if (sb.length() == 0) {
					sb.append("[");
				} else {
					sb.append(",");
				}		
				if (ISO8601.equals(dateFormat)) {
					String readableTime = Utils.convertTimestampToReadable(reading.getTimeStamp());
					String jsonString = Utils.getJSONString(reading.getDeviceId(), readableTime, reading.getSensorType(), reading.getValue());
					sb.append(jsonString);
				} else {
				 	sb.append(reading.toJSONString());
				}
			}
			sb.append("]");
		} else {
			for (models.OldSensorReading reading : readings) {
				if (sb.length() > 0) {
					sb.append('\n');
				} else {
					sb.append(reading.getCSVHeader());
				}
				sb.append(reading.toCSVString());
			}
		}
		return ok(sb.toString());
	}	
		
}
