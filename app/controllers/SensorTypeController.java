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
import java.util.List;

import models.SensorType;
import models.dao.SensorTypeDao;

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

public class SensorTypeController extends Controller {
	private static ApplicationContext context;
	private static SensorTypeDao sensorTypeDao;
	
	private static boolean checkDao(){
		try{
			if (context == null) {
				context = new ClassPathXmlApplicationContext("application-context.xml");
			}
			if (sensorTypeDao == null) {
				sensorTypeDao = (SensorTypeDao) context.getBean("sensorTypeDaoImplementation");
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static Result addSensorType() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			System.out.println("Sensor type not saved, expecting Json data");
			return badRequest("Sensor type not saved, expecting Json data");
		} 
		if(!checkDao()){
			System.out.println("Sensor type not saved, database conf file not found");
			return internalServerError("Sensor type not saved, database conf file not found");
		}

		// Parse JSON File
		String sensorTypeName = json.findPath("sensorTypeName").getTextValue();
		String manufacturer = json.findPath("manufacturer").getTextValue();
		String version = json.findPath("version").getTextValue();
		Double maxValue = json.findPath("maximumValue").getDoubleValue();
		Double minValue = json.findPath("minimumValue").getDoubleValue();
		String unit = json.findPath("unit").getTextValue();
		String interpreter = json.findPath("interpreter").getTextValue();
		String sensorTypeUserDefinedFields = json.findPath("sensorTypeUserDefinedFields").getTextValue();
		String sensorCategoryName = json.findPath("sensorCategoryName").getTextValue();
		
		if(sensorTypeName == null || sensorTypeName.length() == 0){
			System.out.println("Sensor type not saved, null sensorTypeName");
			return ok("Sensor type not saved, null sensorTypeName");
		}
		
		if(sensorCategoryName == null || sensorCategoryName.length() == 0){
			System.out.println("Sensor type not saved: null sensorCategoryName");
			return ok("Sensor type not saved: null sensorCategoryName");
		}

		boolean result = sensorTypeDao.addSensorType(sensorTypeName, manufacturer, version, maxValue, minValue, unit, interpreter, sensorTypeUserDefinedFields, sensorCategoryName);

		if(result){
			System.out.println("Sensor type saved: " + sensorTypeName);
			return ok("Sensor type saved: " + sensorTypeName);
		}else{
			System.out.println("Sensor type not saved: " + sensorTypeName);
			return ok("Sensor type not saved: " + sensorTypeName);
		}
	}
	
	public static Result updateSensorType() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			System.out.println("Sensor type is not updated, expecting Json data");
			return badRequest("Sensor type is not updated, expecting Json data");
		}
		if(!checkDao()){
			System.out.println("Sensor type not updated, database conf file not found");
			return internalServerError("Sensor type not updated, database conf file not found");
		}

		// Parse JSON File
		String sensorTypeName = json.findPath("sensorTypeName").getTextValue();
		String userDefinedFields = json.findPath("sensorTypeUserDefinedFields").getTextValue();
		
		if(sensorTypeName == null || sensorTypeName.length() == 0){
			System.out.println("Sensor type not updated, null sensorTypeName");
			return ok("Sensor type not updated, null sensorTypeName");
		}
		if(userDefinedFields == null){
			System.out.println("Sensor type not updated, null sensorTypeUserDefinedFields: " + sensorTypeName);
			return ok("Sensor type not updated, null sensorTypeUserDefinedFields: " + sensorTypeName);
		}
		
		if(sensorTypeDao.getSensorType(sensorTypeName) == null){
			System.out.println("Sensor type not updated, sensor type does not exist: " + sensorTypeName);
			return ok("Sensor type not updated, sensor type does not exist: " + sensorTypeName); 
		}

		boolean result = sensorTypeDao.updateSensorType(sensorTypeName, userDefinedFields);

		if(result){
			System.out.println("Sensor type updated: " + sensorTypeName);
			return ok("Sensor type updated: " + sensorTypeName);
		}else{
			System.out.println("Sensor type not updated: " + sensorTypeName);
			return ok("Sensor type not updated: " + sensorTypeName);
		}
	}
	
	public static Result getSensorType(String sensorTypeName, String format) {
		if(sensorTypeName == null || sensorTypeName.length() == 0){
			System.out.println("Sensor type not found, null sensorTypeName");
			return ok("Sensor type not found, null sensorTypeName");
		}
		if(!checkDao()){
			System.out.println("Sensor type not found, database conf file not found");
			return internalServerError("Sensor type not found, database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		SensorType sensorType = sensorTypeDao.getSensorType(sensorTypeName);
		if(sensorType == null){
			System.out.println("Sensor type not found: " + sensorTypeName);
			return notFound("Sensor type not found: " + sensorTypeName);
		}
		
		String ret = new String();
		if (format.equals("json")) {
			ret = new Gson().toJson(sensorType);
		} else {
			ret = toCsv(Arrays.asList(sensorType));
		}
		return ok(ret);
	}
	
	public static Result getAllSensorTypes(String format) {
		if(!checkDao()){
			System.out.println("Sensor type not found, database conf file not found");
			return internalServerError("Sensor type not found, database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		List<SensorType> sensorTypes = sensorTypeDao.getAllSensorTypes();
		
		if(sensorTypes == null || sensorTypes.isEmpty()){
			System.out.println("No sensor type is found");
			return notFound("No sensor type is found");
		}
		
		String ret = new String();
		if(format.equals("json")){
			ret = new Gson().toJson(sensorTypes);
		} else {
			ret = toCsv(sensorTypes);
		}
		return ok(ret);
	}
	
	public static Result deleteSensorType(String sensorTypeName){
		if(sensorTypeName == null || sensorTypeName.length() == 0){
			System.out.println("Sensor type not deleted, null sensorTypeName");
			return ok("Sensor type not deleted, null sensorTypeName");
		}
		if(!checkDao()){
			System.out.println("Sensor type not deleted, database conf file not found");
			return internalServerError("Sensor type not deleted, database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		if(sensorTypeDao.getSensorType(sensorTypeName) == null){
			System.out.println("Sensor type not deleted, sensor type does not exist: " + sensorTypeName);
			return ok("Sensor type not deleted, sensor type does not exist: " + sensorTypeName);
		}
		
		if(sensorTypeDao.deleteSensorType(sensorTypeName)){
			System.out.println("Sensor type deleted: " + sensorTypeName);
			return ok("Sensor type deleted: " + sensorTypeName);
		}else{
			System.out.println("Sensor type not deleted: " + sensorTypeName);
			return ok("Sensor type not deleted: " + sensorTypeName);
		}
	}
	
	private static String toCsv(List<SensorType> types) {
		StringWriter sw = new StringWriter();
		CellProcessor[] processors = new CellProcessor[] {
				new Optional(),	new Optional(), new Optional(),	new Optional(),
				new Optional(),	new Optional(), new Optional(),	new Optional(),
				new Optional()};
		ICsvBeanWriter writer = new CsvBeanWriter(sw, CsvPreference.STANDARD_PREFERENCE);
		
		try {
			final String[] header = new String[] {"sensorTypeName", "manufacturer"
					, "version", "maximumValue", "minimumValue", "unit"
					, "interpreter", "sensorTypeUserDefinedFields", "sensorCategoryName"};
			writer.writeHeader(header);
			for(SensorType type : types){
				writer.write(type, header, processors);
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
