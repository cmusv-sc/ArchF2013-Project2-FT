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
		if (context == null) {
			context = new ClassPathXmlApplicationContext("application-context.xml");
		}
		if (sensorCategoryDao == null) {
			sensorCategoryDao = (SensorCategoryDao) context.getBean("sensorCategoryDaoImplementation");
		}
		
		return true;
	}
	
	public static Result addSensorCategory() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} 
		checkDao();

		// Parse JSON FIle 
		String sensorCategoryName = json.findPath("sensorCategoryName").getTextValue();
		String purpose = json.findPath("purpose").getTextValue();
		
		if(sensorCategoryName == null || sensorCategoryName.length() == 0){
			System.out.println("sensor category not saved: null name");
			return ok("sensor category not saved: null name");
		}
		
		boolean result = sensorCategoryDao.addSensorCategory(sensorCategoryName, purpose);

//		TODO API document says it should return a HTTP 201 here. However, Play does not have a class for it
//		Is HTTP 200 (implemented by Result.Ok) fine?
		if(result){
			System.out.println("sensor category saved");
			return ok("sensor category saved");
		}
		else{
			System.out.println("sensor category not saved: " + sensorCategoryName);
			return ok("sensor category not saved: " + sensorCategoryName);
		}
	}
	
	public static Result updateSensorCategory() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} 
		checkDao();

//		Parse JSON FIle 
		String sensorCategoryName = json.findPath("sensorCategoryName").getTextValue();
		String purpose = json.findPath("purpose").getTextValue();
		
//		Return error message if the SensorCategory does not exist 
		if(sensorCategoryDao.getSensorCategory(sensorCategoryName) == null){
			return ok("sensor category not updated: " + sensorCategoryName); 
		}
		
		boolean result = sensorCategoryDao.updateSensorCategory(sensorCategoryName, purpose);

		if(result){
			System.out.println("sensor category updated");
			return ok("sensor category updated");
		}else{
			System.out.println("sensor category not updated: " + sensorCategoryName);
			return ok("sensor category not updated: " + sensorCategoryName);
		}
	}
	
	public static Result getSensorCategory(String SensorCategoryName, String format) {
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		SensorCategory sensorCategory = sensorCategoryDao.getSensorCategory(SensorCategoryName);
		if(sensorCategory == null){
			return notFound("no sensor categories found");
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
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		List<SensorCategory> categories = sensorCategoryDao.getAllSensorCategories();
		String ret = new String();
		if (format.equals("json")){
			ret = new Gson().toJson(categories);
		} else {
			ret = toCsv(categories);
		}
		return ok(ret);
	}
	
	public static Result deleteSensorCategory(String sensorCategoryName){
		checkDao();
		response().setHeader("Access-Control-Allow-Origin", "*");
		if(sensorCategoryDao.deleteSensorCategory(sensorCategoryName)){
			System.out.println("sensor deleted");
			return ok("sensor deleted");
		}else{
			System.out.println("sensor is not deleted");
			return ok("sensor is not deleted");
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
