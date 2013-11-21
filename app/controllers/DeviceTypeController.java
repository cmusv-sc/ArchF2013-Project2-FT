package controllers;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import models.DeviceType;
import models.dao.DeviceTypeDao;

import org.codehaus.jackson.JsonNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import play.mvc.Controller;
import play.mvc.Result;
//import models.cmu.sv.sensor.SensorReading;

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
				if (ret.isEmpty())
					ret += "[";
				else				
					ret += ',';			
				ret += deviceType.toJSONString();
			ret += "]";			
		} else {			
				if (!ret.isEmpty())
					ret += '\n';
				else
					ret += deviceType.getCSVHeader();
				ret += deviceType.toCSVString();
		}
		return ok(ret);
	}

}
