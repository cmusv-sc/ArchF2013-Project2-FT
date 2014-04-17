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

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import models.Device;
import models.dao.DeviceDao;

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
import views.html.defaultpages.error;

import com.google.gson.Gson;

public class DeviceController extends Controller {
	private static ApplicationContext context;
	private static DeviceDao deviceDao;
	
	private static boolean checkDao(){
		if (context == null) {
			context = new ClassPathXmlApplicationContext("application-context.xml");
		}
		if (deviceDao == null) {
			deviceDao = (DeviceDao) context.getBean("deviceDaoImplementation");
		}
		return true;
	}
	
	public static Result add() {
		JsonNode json = request().body().asJson();
		 if(json == null) {
			    return badRequest("Expecting Json data");
		 } 
		 
		 if (!checkDao()){
			 return internalServerError("database conf file not found"); 
		 }
		 
		 Gson gson = new Gson();
			
		 Device device = gson.fromJson(request().body().asJson().toString(), Device.class);
		 boolean result = deviceDao.addDevice(device.getDeviceTypeName(), device.getUri(), device.getDeviceUserDefinedFields(), device.getLocation().getLongitude(), device.getLocation().getLatitude(), device.getLocation().getAltitude(), device.getLocation().getRepresentation());
		 
		 if(!result){
			System.err.println(device.getUri() + " is not saved: " + error.toString());
			return badRequest("device not saved");
		 } 
         return created("device saved");		 
	}
	
	public static Result updateDevice(String deviceUri) {
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		
		Gson gson = new Gson();
		
		Device wrapper = gson.fromJson(request().body().asJson().toString(), Device.class);
		
		Device device = deviceDao.updateDevice(deviceUri, wrapper);
		if(device == null){
			return notFound("no devices found");
		}
		String ret = new Gson().toJson(device);		
		return ok(ret);
	}

	public static Result getAllDevices(String format) {
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		List<models.Device> devices = deviceDao.getAllDevices();
		if(devices == null || devices.isEmpty()){
			return notFound("no devices found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(devices);		
		} else {			
			ret = toCsv(devices);
		}
		return ok(ret);
	}
	
	public static Result getAllDevicesByGeofence(String location, String format) {
		if(location == null){
			return notFound("Geofence shouldn't be empty");
		}
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		List<models.Device> devices = deviceDao.getAllDevices();
		Iterator<Device> iter = devices.iterator();
		
		while (iter.hasNext()) {
			Device device = iter.next();
			
			if (!location.equals(device.getLocation().getRepresentation())){
				iter.remove();
			}
		}
		
		if(devices == null || devices.isEmpty()){
			return notFound("no devices found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(devices);		
		} else {			
			ret = toCsv(devices);
		}
		return ok(ret);
	}
	
	public static List<Device> getAllDevicesByGeofence(String location){
		if(location == null || !checkDao()){
			return null;
		}
		
		List<models.Device> devices = deviceDao.getAllDevices();
		
		Iterator<Device> iter = devices.iterator();

		while (iter.hasNext()) {
			Device device = iter.next();
			
			if (!location.equals(device.getLocation().getRepresentation())){
				iter.remove();
			}
		}
		
		return devices;
	}
	
	public static Result getDevice(String uri, String format) {
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		Device device = deviceDao.getDevice(uri);
		if(device == null){
			return notFound("no devices found");
		}
		String ret = new String();
		if (format.equals("json"))
		{							
			ret = new Gson().toJson(device);
							
		} else {						
			
				ret = toCsv(Arrays.asList(device));			
		}
		return ok(ret);
	}

	public static Result deleteDevice(String deviceName){
		if(!checkDao()){
			System.out.println("Device not deleted, database conf file not found");
			return internalServerError("Device not deleted, database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		if(deviceName == null || deviceName.length() == 0){
			System.out.println("Device not deleted, null deviceName");
			return ok("Device not deleted, null deviceName");
		}
		
		boolean result = deviceDao.deleteDevice(deviceName); 
		
		if(result){
			System.out.println("Device deleted: " + deviceName);
			return ok("Device deleted: " + deviceName);
		}else{
			System.out.println("Device not deleted: " + deviceName);
			return ok("Device not deleted: " + deviceName);
		}
	}
	
	
	private static String toCsv(List<Device> devices) {
		StringWriter sw = new StringWriter();
		CellProcessor[] processors = new CellProcessor[] {
				new Optional(),
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
			final String[] header = new String[] { "uri", "location", "sensorNames", "deviceUserDefinedFields", "deviceTypeName", "manufacturer", "version", "deviceTypeUserDefinedFields", "sensorTypeNames"};
			writer.writeHeader(header);
			for (Device device : devices) {
				writer.write(device, header, processors);
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
