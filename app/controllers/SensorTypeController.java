package controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.SensorCategory;
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

import com.google.gson.Gson;

import play.mvc.Controller;
import play.mvc.Result;

public class SensorTypeController extends Controller {
	private static ApplicationContext context;
	private static SensorTypeDao sensorTypeDao;
	
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

		// Parse JSON File
		String sensorTypeName = json.findPath("sensorTypeName").getTextValue();
		String manufacturer = json.findPath("manufacturer").getTextValue();
		String version = json.findPath("version").getTextValue();
		Double maxValue = json.findPath("maximumValue").getDoubleValue();
		Double minValue = json.findPath("minimumValue").getDoubleValue();
		String unit = json.findPath("unit").getTextValue();
		String interpreter = json.findPath("interpreter").getTextValue();
		String userDefinedFields = json.findPath("userDefinedFields").getTextValue();
		String sensorCategoryName = json.findPath("sensorCategoryName").getTextValue();
		ArrayList<String> error = new ArrayList<String>();
		
		if(sensorTypeName == null || sensorTypeName.length() == 0){
			System.out.println("sensor type not saved: null name");
			return ok("sensor type not saved: null name");
		}
		
		if(sensorCategoryName == null || sensorCategoryName.length() == 0){
			System.out.println("sensor type not saved: null sensor category name");
			return ok("sensor type not saved: null sensor category name");
		}

		boolean result = sensorTypeDao.addSensorType(sensorTypeName, manufacturer, version, maxValue, minValue, unit, interpreter, userDefinedFields, sensorCategoryName);

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
	
	public static Result updateSensorType() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} 
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}

		// Parse JSON File
		String sensorTypeName = json.findPath("sensorTypeName").getTextValue();
		String manufacturer = json.findPath("manufacturer").getTextValue();
		String version = json.findPath("version").getTextValue();
		Double maxValue = json.findPath("maximumValue").getDoubleValue();
		Double minValue = json.findPath("minimumValue").getDoubleValue();
		String unit = json.findPath("unit").getTextValue();
		String interpreter = json.findPath("interpreter").getTextValue();
		String userDefinedFields = json.findPath("userDefinedFields").getTextValue();
		String sensorCategoryName = json.findPath("sensorCategoryName").getTextValue();
		ArrayList<String> error = new ArrayList<String>();
		
		if(sensorTypeDao.getSensorType(sensorTypeName) == null){
			error.add(sensorTypeName);
		}

		boolean result = sensorTypeDao.updateSensorType(sensorTypeName, manufacturer, version, maxValue, minValue, unit, interpreter, userDefinedFields, sensorCategoryName);

		if(!result){
			error.add(sensorTypeName);
		}

		if(error.size() == 0){
			System.out.println("sensor type updated");
			return ok("sensor type updated");
		}else{
			System.out.println("sensor type not updated: " + error.toString());
			return ok("sensor type not updated: " + error.toString());
		}
	}
	
	public static Result getSensorType(String sensorTypeName, String format) {
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		// case insensitive search. device types in the database are in lower case
//		sensorTypeName = sensorTypeName.toLowerCase();
		SensorType sensorType = sensorTypeDao.getSensorType(sensorTypeName);
		if(sensorType == null){
			return notFound("No sensor type found for: " + sensorTypeName);
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
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		List<SensorType> sensorTypes = sensorTypeDao.getAllSensorTypes();
		if(sensorTypes == null || sensorTypes.isEmpty()){
			return notFound("No sensor type found");
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
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		if(sensorTypeDao.deleteSensorType(sensorTypeName)){
			System.out.println("sensor type deleted");
			return ok("sensor type deleted");
		}else{
			System.out.println("sensor type is not deleted");
			return ok("sensor type is not deleted");
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
