package controllers;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.Sensor;
import models.dao.SensorDao;

import org.codehaus.jackson.JsonNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
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
	
	private static void checkDao(){
		if (context == null) {
			context = new ClassPathXmlApplicationContext("application-context.xml");
		}
		if (sensorDao == null) {
			sensorDao = (SensorDao) context.getBean("sensorDaoImplementation");
		}
	}

	public static Result addSensor() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} 
		checkDao();

		// Parse JSON FIle 
		String sensorTypeName = json.findPath("sensor_type_name").getTextValue();
		String deviceUri = json.findPath("device_uri").getTextValue();
		String sensorName = json.findPath("sensor_name").getTextValue();
		String userDefinedFields = json.findPath("user_defined_fields").getTextValue();
		ArrayList<String> error = new ArrayList<String>();
		
		boolean result = sensorDao.addSensor(sensorTypeName, deviceUri, sensorName, userDefinedFields);

		if(!result){
			error.add(sensorTypeName);
		}
		// Can this error have more than one name in it? I don't understand why error needs to be a list.
		if(error.size() == 0){
			System.out.println("sensor saved");
			return ok("sensor saved");
		}
		else{
			System.out.println("sensor not saved: " + error.toString());
			return ok("sensor not saved: " + error.toString());
		}
	}
	
	public static Result updateSensor() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} 
		checkDao();

		// Parse JSON FIle 
		String sensorTypeName = json.findPath("sensor_type_name").getTextValue();
		String deviceUri = json.findPath("device_uri").getTextValue();
		String sensorName = json.findPath("sensor_name").getTextValue();
		String userDefinedFields = json.findPath("user_defined_fields").getTextValue();
		ArrayList<String> error = new ArrayList<String>();
		
		if(sensorDao.getSensor(sensorName) == null){
			System.out.println("sensor not updated: " + error.toString());
			return ok("sensor not updated: " + error.toString());
		}
		
		boolean result = sensorDao.updateSensor(sensorTypeName, deviceUri, sensorName, userDefinedFields);

		if(!result){
			error.add(sensorTypeName);
		}
		
		if(error.size() == 0){
			System.out.println("sensor updated");
			return ok("sensor updated");
		}
		else{
			System.out.println("sensor not updated: " + error.toString());
			return ok("sensor not updated: " + error.toString());
		}
	}
	
	public static Result getSensor(String sensorName, String format) {
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		Sensor sensor = sensorDao.getSensor(sensorName);
		if(sensor == null){
			return notFound("no sensor found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(sensor);
		} 
		else {			
			ret = toCsv(Arrays.asList(sensor));
		}
		return ok(ret);
	}
	
	public static Result getAllSensors(String format) {
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		List<Sensor> sensors = sensorDao.getAllSensors();
		
		if(sensors == null || sensors.isEmpty()){
			return notFound("no sensor found");
		} 
		
		String ret = null;
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(sensors);
		} 
		else {			
			ret = toCsv(sensors);
		}
		return ok(ret);
	}
	
	private static String toCsv(List<Sensor> sensors) {
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
			final String[] header = new String[] { "sensorName", "sensorUserDefinedFields", "sensorTypeName", "sensorTypeUserDefinedFields", "sensorCategoryName", "purpose", "manufacturer", "interpreter", "version", "maxValue", "minValue", "unit"};
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
