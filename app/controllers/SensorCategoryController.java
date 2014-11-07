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

import models.SensorCategory;
import models.dao.SensorCategoryDao;

import org.codehaus.jackson.JsonNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;


//import app.models.dao.String;
import play.mvc.Controller;
import play.mvc.Result;

import com.google.gson.Gson;

public class SensorCategoryController extends Controller {
	private static ApplicationContext context;
	private static SensorCategoryDao sensorCategoryDao;
	
	private static boolean checkDao(){
		try{
			if (context == null) {
				context = new ClassPathXmlApplicationContext("application-context.xml");
			}
			if (sensorCategoryDao == null) {
				sensorCategoryDao = (SensorCategoryDao) context.getBean("sensorCategoryDaoImplementation");
			}
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static Result addSensorCategory() {
		response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		JsonNode json = request().body().asJson();
		if(json == null) {
			System.out.println("Sensor category not saved, expecting Json data");
			return badRequest("Sensor category not saved, expecting Json data");
		}
		
		if(!checkDao()){
			System.out.println("Sensor category not saved, database conf file not found");
			return internalServerError("Sensor category not saved, database conf file not found");
		}
		
		// Parse JSON FIle 
		String sensorCategoryName = json.findPath("sensorCategoryName").getTextValue();
		String purpose = json.findPath("purpose").getTextValue();
		
		if(sensorCategoryName == null || sensorCategoryName.length() == 0){
			System.out.println("Sensor category not saved, null name");
			return ok("Sensor category not saved, null name");
		}
		
		boolean result = sensorCategoryDao.addSensorCategory(sensorCategoryName, purpose);

//		TODO API document says it should return a HTTP 201 here. However, Play does not have a class for it
//		Is HTTP 200 (implemented by Result.Ok) fine?
		if(result){
			System.out.println("Sensor category saved: " + sensorCategoryName);
			return created("Sensor category saved: " + sensorCategoryName);
		}else{
			System.out.println("Sensor category not saved: " + sensorCategoryName);
			return badRequest("Sensor category not saved: " + sensorCategoryName);
		}
	}
	
	public static Result updateSensorCategory() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			System.out.println("Sensor category not saved, expecting Json data");
			return badRequest("Sensor category not saved, expecting Json data");
		}
		
		if(!checkDao()){
			System.out.println("Sensor category not updated, database conf file not found");
			return internalServerError("Sensor category not updated, database conf file not found");
		}

//		Parse JSON FIle 
		String sensorCategoryName = json.findPath("sensorCategoryName").getTextValue();
		String purpose = json.findPath("purpose").getTextValue();
		
		if(sensorCategoryName == null || sensorCategoryName.length() == 0){
			System.out.println("Sensor category not updated, null sensorCategoryName");
			return ok("Sensor category not updated: null sensorCategoryName");
		}else if(purpose == null || purpose.length() == 0){
			System.out.println("Sensor category not updated, null purpose: " + sensorCategoryName);
			return ok("Sensor category not updated: null purpose: " + sensorCategoryName);
		}
		
//		Return error message if the SensorCategory is not found 
		if(sensorCategoryDao.getSensorCategory(sensorCategoryName) == null){
			System.out.println("Sensor category not updated, sensor category not found: " + sensorCategoryName);
			return ok("Sensor category not updated, sensor category not found: " + sensorCategoryName); 
		}
		
		boolean result = sensorCategoryDao.updateSensorCategory(sensorCategoryName, purpose);

		if(result){
			System.out.println("Sensor category updated: " + sensorCategoryName);
			return ok("Sensor category updated: " + sensorCategoryName);
		}else{
			System.out.println("Sensor category not updated: " + sensorCategoryName);
			return ok("Sensor category not updated: " + sensorCategoryName);
		}
	}
	
	public static Result getSensorCategory(String sensorCategoryName, String format) {
		if(sensorCategoryName == null || sensorCategoryName.length() == 0){
			System.out.println("Sensor category not found, null sensorCategoryName");
			return ok("Sensor category not found, null sensorCategoryName");
		}
		
		if(!checkDao()){
			System.out.println("Sensor category not found, database conf file not found");
			return internalServerError("Sensor category not found, database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		SensorCategory sensorCategory = sensorCategoryDao.getSensorCategory(sensorCategoryName);
		if(sensorCategory == null){
			System.out.println("Sensor category not found: " + sensorCategoryName);
			return notFound("Sensor category not found: " + sensorCategoryName);
		}
		String ret = new String();
		if (format.equals("json")){
			ret = new Gson().toJson(sensorCategory);
		} else {
			ret = toCsv(Arrays.asList(sensorCategory));
		}
		return ok(ret);
	}
	
	public static Result getAllSensorCategories(String format) {
		response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");

		if(!checkDao()){
			System.out.println("Sensor category not found, database conf file not found");
			return internalServerError("Sensor category not found, database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		List<SensorCategory> categories = sensorCategoryDao.getAllSensorCategories();
		
		if(categories == null || categories.isEmpty()){
			System.out.println("No sensor category found");
			return notFound("No sensor category found");
		}
		
		String ret = new String();
		if (format.equals("json")){
			ret = new Gson().toJson(categories);
		} else {
			ret = toCsv(categories);
		}
		return ok(ret);
	}
	
	public static Result deleteSensorCategory(String sensorCategoryName){
		if(sensorCategoryName == null || sensorCategoryName.length() == 0){
			System.out.println("Sensor category not deleted, null sensorCategoryName");
			return ok("Sensor category not deleted, null sensorCategoryName");
		}
		
		if(!checkDao()){
			System.out.println("Sensor category not deleted, database conf file not found");
			return internalServerError("Sensor category not deleted, database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		
//		Return error message if the SensorCategory is not found 
		if(sensorCategoryDao.getSensorCategory(sensorCategoryName) == null){
			System.out.println("Sensor category not deleted, sensor category not found: " + sensorCategoryName);
			return ok("Sensor category not deleted, sensor category not found: " + sensorCategoryName); 
		}
		
		boolean result = sensorCategoryDao.deleteSensorCategory(sensorCategoryName);
		
		if(result){
			System.out.println("Sensor category is deleted: " + sensorCategoryName);
			return ok("Sensor category is deleted: " + sensorCategoryName);
		}else{
			System.out.println("Sensor category is not deleted: " + sensorCategoryName);
			return ok("Sensor category is not deleted: " + sensorCategoryName);
		}
	}
	
	private static String toCsv(List<SensorCategory> categories) {
		StringWriter sw = new StringWriter();
		CellProcessor[] processors = new CellProcessor[] {
				new Optional(),	new Optional()};
		ICsvBeanWriter writer = new CsvBeanWriter(sw, CsvPreference.STANDARD_PREFERENCE);
		
		try {
			final String[] header = new String[] {"sensorCategoryName", "purpose"};
			writer.writeHeader(header);
			for(SensorCategory category : categories){
				writer.write(category, header, processors);
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
