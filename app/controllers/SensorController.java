package controllers;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import models.Sensor;
import models.dao.SensorDao;

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
//import models.cmu.sv.sensor.SensorReading;

import com.google.gson.Gson;

public class SensorController extends Controller {
		
	private static ApplicationContext context;
	private static SensorDao sensorDao;
	
	private static boolean checkDao(){
		try{
			if (context == null) {
				context = new ClassPathXmlApplicationContext("application-context.xml");
			}
			if (sensorDao == null) {
				sensorDao = (SensorDao) context.getBean("sensorDaoImplementation");
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Result addSensor() {
		JsonNode json = request().body().asJson();
		if (json == null) {
			System.out.println("Sensor not saved, expecting Json data");
			return badRequest("Sensor not saved, expecting Json data");
		}
		if(!checkDao()){
			System.out.println("Sensor not saved, database conf file not found");
			return internalServerError("Sensor not saved, database conf file not found");
		}

		// Parse JSON FIle 
		String sensorTypeName = json.findPath("sensorTypeName").getTextValue();
		String deviceUri = json.findPath("deviceUri").getTextValue();
		String sensorName = json.findPath("sensorName").getTextValue();
		String userDefinedFields = json.findPath("sensorUserDefinedFields").getTextValue();
		String userName = json.findPath("userName").getTextValue();

		
		ArrayList<String> error = new ArrayList<String>();
		
		boolean result;
		
		if (userName != null && !userName.equals("")) {
			result = sensorDao.addSensor(sensorTypeName, deviceUri, sensorName, userDefinedFields, userName);
		} else {
			result = sensorDao.addSensor(sensorTypeName, deviceUri, sensorName, userDefinedFields);
		}
		
		if (!result) {
			error.add(sensorTypeName);
    }	
		if(sensorName == null || sensorName.length() == 0){
			System.out.println("Sensor not saved, null sensorName");
			return ok("Sensor not saved, null sensorName");
		}
		
		if(sensorTypeName == null || sensorTypeName.length() == 0){
			System.out.println("Sensor not saved, null sensorTypeName: " + sensorName);
			return ok("Sensor not saved, null sensorTypeName: " + sensorName);
		}
		
		if(deviceUri == null || deviceUri.length() == 0){
			System.out.println("Sensor not saved, null deviceUri: " + deviceUri);
			return ok("Sensor not saved, null deviceUri: " + deviceUri);
		}
		
		boolean result = sensorDao.addSensor(sensorTypeName, deviceUri, sensorName, userDefinedFields);
		
		if (result) {
			System.out.println("Sensor saved: " + sensorTypeName);
			return ok("Sensor saved: " + sensorTypeName);
		} else {
			System.out.println("Sensor not saved: " + sensorTypeName);
			return ok("Sensor not saved: " + sensorTypeName);
		}
	}
	
	public static Result updateSensor() {
		JsonNode json = request().body().asJson();
		if (json == null) {
			System.out.println("Sensor not updated, expecting Json data");
			return badRequest("Sensor not updated, expecting Json data");
		}
		if(!checkDao()){
			System.out.println("Sensor not updated, database conf file not found");
			return internalServerError("Sensor not updated, database conf file not found");
		}

//		Parse JSON File 
		String sensorName = json.findPath("sensorName").getTextValue();
		String userDefinedFields = json.findPath("sensorUserDefinedFields").getTextValue();
		
		if(sensorName == null || sensorName.length() == 0){
			System.out.println("Sensor not saved, null sensorName");
			return ok("Sensor not saved, null sensorName");
		}
		
		if(userDefinedFields == null || userDefinedFields.length() == 0){
			System.out.println("Sensor not updated, null userDefinedFields: " + sensorName);
			return ok("Sensor not updated, null userDefinedFields: " + sensorName);
		}
		
		boolean result = sensorDao.updateSensor(sensorName, userDefinedFields);

		if (result) {
			System.out.println("Sensor updated: " + sensorName);
			return ok("Sensor updated: " + sensorName);
		} else {
			System.out.println("Sensor not updated: " + sensorName);
			return ok("Sensor not updated: " + sensorName);
		}
	}
	
	public static Result getSensor(String sensorName, String format) {
		String[] userNames = request().headers().get("Authorization");
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		
		Sensor sensor = null;
		if (userNames.length > 0) {
			sensor = sensorDao.getSensor(sensorName, userNames[0]);
		} else {
			sensor = sensorDao.getSensor(sensorName);
		}
		
		if(!checkDao()){
			System.out.println("Sensor not found, database conf file not found");
			return internalServerError("Sensor not found, database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		if(sensorName == null || sensorName.length() == 0){
			System.out.println("Sensor not found, null sensorName");
			return ok("Sensor not found, null sensorName");
		}
		
		Sensor sensor = sensorDao.getSensor(sensorName);
		
		if(sensor == null){
			System.out.println("Sensor not found: " + sensorName);
			return notFound("Sensor not found: " + sensorName);
		}
		String ret = new String();
		if (format.equals("json")) {			
			ret = new Gson().toJson(sensor);
		} else {			
			ret = toCsv(Arrays.asList(sensor));
		}
		return ok(ret);
	}
	
	public static Result getAllSensors(String format) {
		if(!checkDao()){
			System.out.println("Sensor not found, database conf file not found");
			return internalServerError("Sensor not found, database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		List<Sensor> sensors = sensorDao.getAllSensors();
		
		if(sensors == null || sensors.isEmpty()){
			System.out.println("No sensor found");
			return notFound("No sensor found");
		} 
		
		String ret = null;
		if (format.equals("json")) {			
			ret = new Gson().toJson(sensors);
		} else {			
			ret = toCsv(sensors);
		}
		return ok(ret);
	}
	
	public static Result deleteSensor(String sensorName){
		if(!checkDao()){
			System.out.println("Sensor not deleted, database conf file not found");
			return internalServerError("Sensor not deleted, database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		if(sensorName == null || sensorName.length() == 0){
			System.out.println("Sensor not deleted, null sensorName");
			return ok("Sensor not deleted, null sensorName");
		}
		
		boolean result = sensorDao.deleteSensor(sensorName); 
		
		if(result){
			System.out.println("Sensor deleted: " + sensorName);
			return ok("Sensor deleted: " + sensorName);
		}else{
			System.out.println("Sensor not deleted: " + sensorName);
			return ok("Sensor not deleted: " + sensorName);
		}
	}
	
	private static String toCsv(List<Sensor> sensors) {
		StringWriter sw = new StringWriter();
		CellProcessor[] processors = new CellProcessor[] {
				new Optional(),	new Optional(),	new Optional(),	new Optional(),
				new Optional(),	new Optional(),	new Optional(),	new Optional(),
				new Optional(),	new Optional(),	new Optional(), new Optional()};
		ICsvBeanWriter writer = new CsvBeanWriter(sw, CsvPreference.STANDARD_PREFERENCE);
		try {
			final String[] header = new String[] {"sensorName", "sensorUserDefinedFields"
					, "deviceUri" , "sensorTypeName", "manufacturer", "version"
					, "maximumValue", "minimumValue", "unit", "interpreter"
					, "sensorTypeUserDefinedFields", "sensorCategoryName"};
			writer.writeHeader(header);
			for (Sensor sensor : sensors) {
				writer.write(sensor, header, processors);
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
