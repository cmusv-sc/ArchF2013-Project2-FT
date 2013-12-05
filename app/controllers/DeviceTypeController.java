package controllers;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import models.DeviceType;
import models.dao.DeviceTypeDao;

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

public class DeviceTypeController extends Controller {
		
	private static ApplicationContext context;
	private static DeviceTypeDao deviceTypeDao;
	
	private static void checkDao(){
		if (context == null) {
			context = new ClassPathXmlApplicationContext("application-context.xml");
		}
		if (deviceTypeDao == null) {
			deviceTypeDao = (DeviceTypeDao) context.getBean("deviceTypeDaoImplementation");
		}
	}

	public static Result addDeviceType() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} 
		checkDao();

		// Parse JSON FIle 
		String deviceTypeName = json.findPath("device_type_name").getTextValue();
		String manufacturer = json.findPath("manufacturer").getTextValue();
		String version = json.findPath("version").getTextValue();
		String userDefinedFields = json.findPath("user_defined_fields").getTextValue();
		Iterator<JsonNode> sensorTypeIte = json.findPath("sensor_type_names").getElements();
		List<String> sensorTypes = new LinkedList<String>();
		while(sensorTypeIte.hasNext()) {
			sensorTypes.add(sensorTypeIte.next().getTextValue());
		}
		
		ArrayList<String> error = new ArrayList<String>();
		
		boolean result = deviceTypeDao.addDeviceType(deviceTypeName, manufacturer, version, userDefinedFields, sensorTypes);

		if(!result){
			error.add(deviceTypeName);
		}
		// Can this error have more than one name in it? I don't understand why error needs to be a list.
		if(error.size() == 0){
			System.out.println("device type saved");
			return ok("device type saved");
		}
		else{
			System.out.println("some device types not saved: " + error.toString());
			return ok("some device types not saved: " + error.toString());
		}
	}
	
	public static Result getDeviceType(String deviceTypeName, String format) {
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		DeviceType deviceType = deviceTypeDao.getDeviceType(deviceTypeName);
		if(deviceType == null){
			return notFound("no devices found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(deviceType);		
		} else {			
				ret = toCsv(Arrays.asList(deviceType));
		}
		return ok(ret);
	}
	
	public static Result updateDeviceType(String deviceTypeName, String format) {
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		
		Gson gson = new Gson();
		
		DeviceType wrapper = gson.fromJson(request().body().asJson().toString(), DeviceType.class);
		
		ArrayList<String> error = new ArrayList<String>();

		
		DeviceType deviceType = deviceTypeDao.updateDeviceType(deviceTypeName, wrapper);
		if(deviceType == null){
			return notFound("no devices found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(deviceType);		
		} else {			
				ret = toCsv(Arrays.asList(deviceType));
		}
		return ok(ret);
	}
	
	public static Result getAllDeviceTypes(String format) {
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		List<DeviceType> deviceTypes = deviceTypeDao.getAllDeviceTypes();
		
		if(deviceTypes == null || deviceTypes.isEmpty()){
			return notFound("no device type found");
		} 
		
		String ret = null;
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(deviceTypes);
		} 
		else {			
			ret = toCsv(deviceTypes);
		}
		return ok(ret);
	}
	
	private static String toCsv(List<DeviceType> deviceTypes) {
		StringWriter sw = new StringWriter();
		CellProcessor[] processors = new CellProcessor[] {
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				};
		ICsvBeanWriter writer = new CsvBeanWriter(sw, CsvPreference.STANDARD_PREFERENCE);
		try {
			final String[] header = new String[] { "deviceTypeName",  "userDefinedFields",  "manufacturer", "version", "sensorTypes"};
			writer.writeHeader(header);
			for (DeviceType sensor : deviceTypes) {
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
