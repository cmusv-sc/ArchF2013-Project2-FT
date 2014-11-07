/*******************************************************************************
 * Copyright (c) 2014 Carnegie Mellon University Silicon Valley. 
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

import models.ClimateService;
import models.dao.ClimateServiceDao;

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

public class ClimateServiceController extends Controller {
	private static ApplicationContext context;
	private static ClimateServiceDao climateServiceDao;
	
	private static boolean checkDao(){
		try{
			if (context == null) {
				context = new ClassPathXmlApplicationContext("application-context.xml");
			}
			if (climateServiceDao == null) {
				climateServiceDao = (ClimateServiceDao) context.getBean("climateServiceDaoImplementation");
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static Result addClimateService() {	
		response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			System.out.println("Climate service not saved, expecting Json data");
			return badRequest("Climate service not saved, expecting Json data");
		}
		
		if(!checkDao()) {
			System.out.println("Climate service not saved, database conf file not found");
			return internalServerError("Climate service not saved, database conf file not found");
		}	
		
		// Parse JSON File 
		String climateServiceName = json.findPath("climateServiceName").getTextValue();
		String purpose = json.findPath("purpose").getTextValue();
		String url = json.findPath("url").getTextValue();
		
		if(climateServiceName == null || climateServiceName.length() == 0) {
			System.out.println("Climate service not saved, null name");
			return ok("Climate service not saved, null name");
		}
		
		boolean result = climateServiceDao.addClimateService(climateServiceName, purpose, url);

//		TODO API document says it should return a HTTP 201 here. However, Play does not have a class for it
//		Is HTTP 200 (implemented by Result.Ok) fine?
		if(result) {
			System.out.println("Climate service saved: " + climateServiceName);
			return created("Climate service saved: " + climateServiceName);
		} else {
			System.out.println("Climate service not saved: " + climateServiceName);
			return badRequest("Climate service not saved: " + climateServiceName);
		}
	}
	
	public static Result updateClimateService() {
		response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			System.out.println("Climate service not saved, expecting Json data");
			return badRequest("Climate service not saved, expecting Json data");
		}
		
		if(!checkDao()) {
			System.out.println("Climate service not updated, database conf file not found");
			return internalServerError("Climate service not updated, database conf file not found");
		}

		//Parse JSON File 
		String climateServiceName = json.findPath("climateServiceName").getTextValue();
		String purpose = json.findPath("purpose").getTextValue();
		String url = json.findPath("url").getTextValue();
		
		if(climateServiceName == null || climateServiceName.length() == 0) {
			System.out.println("Climate service not updated, null climateServiceName");
			return ok("Climate service not updated: null climateServiceName");
		} else if(purpose == null || purpose.length() == 0) {
			System.out.println("Climate service not updated, null purpose: " + climateServiceName);
			return ok("Climate service not updated: null purpose: " + climateServiceName);
		} else if(url == null || url.length() == 0) {
			System.out.println("Climate service not updated, null url: " + climateServiceName);
			return ok("Climate service not updated: null url: " + climateServiceName);
		}
		
		//Return error message if the ClimateService is not found 
		if(climateServiceDao.getClimateService(climateServiceName) == null){
			System.out.println("Climate service not updated, climate service not found: " + climateServiceName);
			return ok("Climate service not updated, Climate service not found: " + climateServiceName); 
		}
		
		boolean result = climateServiceDao.updateClimateService(climateServiceName, purpose, url);

		if(result) {
			System.out.println("Climate service updated: " + climateServiceName);
			return ok("Climate service updated: " + climateServiceName);
		} else {
			System.out.println("Climate service not updated: " + climateServiceName);
			return ok("Climate service not updated: " + climateServiceName);
		}
	}
	
	public static Result getClimateService(String climateServiceName, String format) {
		response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		
		if(climateServiceName == null || climateServiceName.length() == 0) {
			System.out.println("Climate service not found, null climateServiceName");
			return ok("Climate service not found, null climateServiceName");
		}
		
		if(!checkDao()) {
			System.out.println("Climate service not found, database conf file not found");
			return internalServerError("Climate service not found, database conf file not found");
		}
		
		
		ClimateService ClimateService = climateServiceDao.getClimateService(climateServiceName);
		if(ClimateService == null){
			System.out.println("Climate service not found: " + climateServiceName);
			return notFound("Climate service not found: " + climateServiceName);
		}
		String ret = new String();
		if (format.equals("json")){
			ret = new Gson().toJson(ClimateService);
		} else {
			ret = toCsv(Arrays.asList(ClimateService));
		}
		return ok(ret);
	}
	
	public static Result getAllClimateServices(String format) {
		response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");

		if(!checkDao()) {
			System.out.println("Climate service not found, database conf file not found");
			return internalServerError("Climate service not found, database conf file not found");
		}	
		
		List<ClimateService> categories = climateServiceDao.getAllClimateServices();
		
		if(categories == null || categories.isEmpty()){
			System.out.println("No Climate service found");
			return notFound("No Climate service found");
		}
		
		String ret = new String();
		if (format.equals("json")){
			ret = new Gson().toJson(categories);
		} else {
			ret = toCsv(categories);
		}
		return ok(ret);
	}
	
	public static Result deleteClimateService(String climateServiceName){
		response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		
		if(climateServiceName == null || climateServiceName.length() == 0){
			System.out.println("Climate service not deleted, null climateServiceName");
			return ok("Climate service not deleted, null climateServiceName");
		}
		
		if(!checkDao()){
			System.out.println("Climate service not deleted, database conf file not found");
			return internalServerError("Climate service not deleted, database conf file not found");
		}
		
		//Return error message if the ClimateService is not found 
		if(climateServiceDao.getClimateService(climateServiceName) == null) {
			System.out.println("Climate service not deleted, Climate service not found: " + climateServiceName);
			return ok("Climate service not deleted, Climate service not found: " + climateServiceName); 
		}
		
		boolean result = climateServiceDao.deleteClimateService(climateServiceName);
		
		if(result) {
			System.out.println("Climate service is deleted: " + climateServiceName);
			return ok("Climate service is deleted: " + climateServiceName);
		}else {
			System.out.println("Climate service is not deleted: " + climateServiceName);
			return ok("Climate service is not deleted: " + climateServiceName);
		}
	}
	
	private static String toCsv(List<ClimateService> categories) {
		StringWriter sw = new StringWriter();
		CellProcessor[] processors = new CellProcessor[] {
				new Optional(),	new Optional()};
		ICsvBeanWriter writer = new CsvBeanWriter(sw, CsvPreference.STANDARD_PREFERENCE);
		
		try {
			final String[] header = new String[] {"climateServiceName", "purpose", "url"};
			writer.writeHeader(header);
			for(ClimateService category : categories){
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
