package controllers;

import models.SensorCategory;
import models.dao.SensorCategoryDao;

import org.codehaus.jackson.JsonNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



//import app.models.dao.String;
import play.mvc.Controller;
import play.mvc.Result;

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
		String sensorCategoryName = json.findPath("sensor_category_name").getTextValue();
		String purpose = json.findPath("purpose").getTextValue();
		
		boolean result = sensorCategoryDao.addSensorCategory(sensorCategoryName, purpose);

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
		String sensorCategoryName = json.findPath("sensor_category_name").getTextValue();
		String purpose = json.findPath("purpose").getTextValue();
		
//		Return error message if the SensorCategory does not exist 
		if(sensorCategoryDao.getSensorCategory(sensorCategoryName) == null){
			return ok("sensor category not updated: " + sensorCategoryName); 
		}
		
		boolean result = sensorCategoryDao.updateSensorCategory(sensorCategoryName, purpose);

		if(result){
			System.out.println("sensor category updated");
			return ok("sensor category updated");
		}
		else{
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
		if (format.equals("json"))
		{			
				if (ret.isEmpty())
					ret += "[";
				else				
					ret += ',';			
				ret += sensorCategory.toJSONString();
			ret += "]";			
		} else {			
				if (!ret.isEmpty())
					ret += '\n';
				else
					ret += sensorCategory.getCSVHeader();
//				ret += sensorCategory.toCSVString();
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
}
