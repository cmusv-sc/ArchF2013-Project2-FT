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

import helper.Wrapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.Device;
import models.SensorReading;
import models.dao.SensorReadingDao;

import org.codehaus.jackson.JsonNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import play.mvc.Controller;
import play.mvc.Result;

import com.google.gson.Gson;


public class SensorReadingController extends Controller {
	private static ApplicationContext context;
	private static SensorReadingDao sensorReadingDao;
	
	private static boolean checkDao(){
		if (context == null) {
			context = new ClassPathXmlApplicationContext("application-context.xml");
		}
		if (sensorReadingDao == null) {
			sensorReadingDao = (SensorReadingDao) context.getBean("sensorReadingDaoImplementation");
		}
		
		return true;
	}

	public static Result add() {
		JsonNode json = request().body().asJson();
		if(json == null) {              
			return badRequest("Expecting Json data");
		}
		if(!checkDao()){           
			return internalServerError("database conf file not found");
		}
		
		Gson gson = new Gson();
		
		Wrapper[] wrapperArr = gson.fromJson(request().body().asJson().toString(), Wrapper[].class);
		
		ArrayList<String> error = new ArrayList<String>();

		for (Wrapper w : wrapperArr) {
			if(!sensorReadingDao.addReading(w.sensorName, w.isIndoor, w.timestamp, w.value, w.longitude, w.latitude, w.altitude, w.locationInterpreter)){            
				error.add(w.toString() + "\n");
			}
		}
		
		if(error.size() == 0){          
			return created("saved");             
		}
		else{
			System.out.println("some not saved: " + error.toString());
			return badRequest("some not saved: " + error.toString());
		}
	}
	
	public static Result searchAtTime(String sensorName, Long timeStamp, String format){
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		SensorReading reading = sensorReadingDao.searchReading(sensorName, timeStamp);
		
		if(reading == null){
			return notFound("no reading found");
		}
		
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(reading);
		} 
		else {			
			ret = toCsv(Arrays.asList(reading));
		}

		return ok(ret);
	}

  public static Result getLatestSensorReading(String sensorName, String format){
    response().setHeader("Access-Control-Allow-Origin", "*");
    checkDao();

    long current_time = System.currentTimeMillis();

    // Since sensor data is not indexed on timestamp, getting the latest value
    // is extremely inefficient as we need to sort
    // A hack is to assume that the sensor is active and has given data within the last minute
    List<SensorReading> readings = sensorReadingDao.searchReading(sensorName, current_time-60000, current_time);

    if(readings == null){
      return notFound("No reading found");
    }

    String ret = "";
    if(format.equals("json")){
      ret = new Gson().toJson(readings.get(0));
    }
    else{
      ret = toCsv(Arrays.asList(readings.get(0)));
    }

    return ok(ret);
  }

  public static Result getLastMinuteSensorReadings(String sensorName, String format){
    response().setHeader("Access-Control-Allow-Origin", "*");
    checkDao();

    long current_time = System.currentTimeMillis();

    // Current implementation returns the latest time with a given timestamp
    List<SensorReading> readings = sensorReadingDao.searchReading(sensorName, current_time-60000, current_time);

    if(readings == null){
      return notFound("No readings found");
    }

    String ret = "";
    if(format.equals("json")){
      ret = new Gson().toJson(readings);
    }
    else{
      ret = toCsv(readings);
    }

    return ok(ret);
    
  }
	
	public static Result searchAtTimeThruDeviceUriSensorTypeName(String deviceUir, String sensorTypeName, Long timeStamp, String format){
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		SensorReading reading = sensorReadingDao.searchReading(deviceUir, sensorTypeName, timeStamp);
		
		if(reading == null){
			return notFound("no reading found");
		}
		
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(reading);
		} 
		else {			
			ret = toCsv(Arrays.asList(reading));
		}

		return ok(ret);
	}

	// search readings of timestamp range [startTime, endTime]
	public static Result searchInTimeRange(String sensorName, String startTime, String endTime, String format){
		response().setHeader("Access-Control-Allow-Origin", "*");

		if(!checkDao()){
			return internalServerError("database conf file not found");
		}
		
		List<SensorReading> readings = sensorReadingDao.searchReading(sensorName, Long.valueOf(startTime), Long.valueOf(endTime));
		if(readings == null || readings.isEmpty()){
			return notFound("no reading found");
		}
		
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(readings);
		} 
		else {			
			ret = toCsv(readings);
		}

		return ok(ret);
	}
	
	// search readings of timestamp range [startTime, endTime]
		public static Result searchInTimeRangeThruDeviceUriSensorTypeName(String deviceUir, String sensorTypeName, String startTime, String endTime, String format){
			response().setHeader("Access-Control-Allow-Origin", "*");

			if(!checkDao()){
				return internalServerError("database conf file not found");
			}
			
			List<SensorReading> readings = sensorReadingDao.searchReading(deviceUir, sensorTypeName, Long.valueOf(startTime), Long.valueOf(endTime));
			if(readings == null || readings.isEmpty()){
				return notFound("no reading found");
			}
			
			String ret = new String();
			if (format.equals("json"))
			{			
				ret = new Gson().toJson(readings);
			} 
			else {			
				ret = toCsv(readings);
			}

			return ok(ret);
		}

		/* get all device reading in last 60 seconds*/
	public static Result lastReadingFromAllDevices(Long timeStamp, String format) {
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}

		response().setHeader("Access-Control-Allow-Origin", "*");
		List<models.SensorReading> readings = sensorReadingDao.lastReadingFromAllDevices(timeStamp);
		if(readings == null || readings.isEmpty()){
			return notFound("no reading found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			for (models.SensorReading reading : readings) {
				if (ret.isEmpty())
					ret += "[";
				else				
					ret += ',';			
				ret += reading.toJSONString();
			}
			ret += "]";			
		} else {
			for (models.SensorReading reading : readings) {
				if (!ret.isEmpty())
					ret += '\n';
				else 
					ret += reading.getCSVHeader();
				ret += reading.toCSVString();
			}
		}
		return ok(ret);
	}

	public static Result latestReadingFromAllDevices(String sensorType, String format) {
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}

		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		List<SensorReading> readings = sensorReadingDao.latestReadingFromAllDevicesBySensorType(sensorType);

		if(readings == null || readings.isEmpty()){
			return notFound("no reading found");
		}
		
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(readings);
		} 
		else {			
			ret = toCsv(readings);
		}

		return ok(ret);
	}
	
	public static Result latestReadingFromDevicesByGeofence(String location, String format) {
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}

		response().setHeader("Access-Control-Allow-Origin", "*");
		List<Device> devices = DeviceController.getAllDevicesByGeofence(location);
		
		List<SensorReading> readings = sensorReadingDao.latestReadingFromAllDevices(devices);

		if(readings == null || readings.isEmpty()){
			return notFound("no reading found");
		}
		
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(readings);
		} 
		else {			
			ret = toCsv(readings);
		}

		return ok(ret);
	}
	
		
	private static String toCsv(List<SensorReading> readings) {
		StringWriter sw = new StringWriter();
		CellProcessor[] processors = new CellProcessor[] {
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional()
				};
		ICsvBeanWriter writer = new CsvBeanWriter(sw, CsvPreference.STANDARD_PREFERENCE);
		try {
			final String[] header = new String[] { "sensorName", "isIndoor", "timeStamp", "value", "longitude", "latitude", "altitude", "locationInterpreter"};
			writer.writeHeader(header);
			for (SensorReading reading : readings) {
				writer.write(reading, header, processors);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sw.getBuffer().toString();
	}
}
