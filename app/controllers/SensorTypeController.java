package controllers;

import java.util.ArrayList;
import java.util.List;

import models.SensorType;
import models.dao.SensorTypeDao;

import org.codehaus.jackson.JsonNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import play.mvc.Controller;
import play.mvc.Result;

public class SensorTypeController extends Controller {
	private static SensorTypeDao sensorTypeDao = null;
	private static ApplicationContext context = null;
	
	private static boolean checkDao(){
		if (context == null) {
			context = new ClassPathXmlApplicationContext("application-context.xml");
		}
		if (sensorTypeDao == null) {
			sensorTypeDao = (SensorTypeDao) context.getBean("sensorTypeDaoImplementation");
		}
		
		return true;
	}
	
	
	
	public static Result addSensorType() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} 
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}

		// Parse JSON FIle
		int sensorCategoryId = json.findPath("sensor_category_id").getIntValue();
		String sensorTypeName = json.findPath("sensor_type").getTextValue();
		String manufacturer = json.findPath("manufacturer").getTextValue();
		String version = json.findPath("version").getTextValue();
		Double maxValue = json.findPath("max_value").getDoubleValue();
		Double minValue = json.findPath("min_value").getDoubleValue();
		String unit = json.findPath("unit").getTextValue();
		String interpreter = json.findPath("interpreter").getTextValue();
		String userDefinedFields = json.findPath("user_defined_fields").getTextValue();
		ArrayList<String> error = new ArrayList<String>();

		boolean result = sensorTypeDao.addSensorType(sensorCategoryId, sensorTypeName, manufacturer, version, maxValue, minValue, unit, interpreter, userDefinedFields);

		if(!result){
			error.add(sensorTypeName);
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
	
	public static Result getSensorType(String sensorTypeName, String format) {
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		// case insensitive search. device types in the database are in lower case
		sensorTypeName = sensorTypeName.toLowerCase();
		SensorType sensorType = sensorTypeDao.getSensorType(sensorTypeName);
		if(sensorType == null){
			return notFound("No sensor type found for " + sensorTypeName);
		}
		String ret = new String();
		String sensorTypesStr = sensorType.getSensorTypeName();
		if (format.equals("json")) {
			ret = "{\"sensor_type\":\"" + sensorTypesStr + "\"}";
		} else {
			ret += "sensor_types\n" + sensorTypesStr;
		}
		return ok(ret);
	}
	
	public static Result getAllSensorTypes(String format) {
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		// case insensitive search. device types in the database are in lower case
		List<SensorType> sensorTypes = sensorTypeDao.getAllSensorTypes();
		if(sensorTypes == null || sensorTypes.isEmpty()){
			return notFound("No sensor type found");
		}
		String ret = new String();
		if (format.equals("json"))
		{
			String sensorTypesStr = "";
			for (SensorType sensorType : sensorTypes) {
				if (!sensorTypesStr.isEmpty())
					sensorTypesStr += ',';
				sensorTypesStr += sensorType.getSensorTypeName();
			}
			ret = "{\"sensor_type\":\"" + sensorTypesStr + "\"}";
		} else {
			for (SensorType sensorType : sensorTypes) {
				if (!ret.isEmpty())
					ret += '\n';
				else
					ret += "sensor_types\n";
				ret += sensorType.getSensorTypeName();
			}
		}
		return ok(ret);
	}
}
