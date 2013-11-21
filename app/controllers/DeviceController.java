package controllers;

import java.util.ArrayList;
import java.util.List;


//import models.DBHandler;
import models.Device;
import models.dao.DeviceDao;

import org.codehaus.jackson.JsonNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.defaultpages.error;

public class DeviceController extends Controller {
//	private static DBHandler dbHandler = null;
	private static ApplicationContext context;
	private static DeviceDao deviceDao;
	
	private static boolean checkDao(){
		if (context == null) {
			context = new ClassPathXmlApplicationContext("application-context.xml");
		}
		if (deviceDao == null) {
			deviceDao = (DeviceDao) context.getBean("deviceDaoImplementation");
		}
		return true;
	}
	
//	private static boolean testDBHandler(){
//		if(dbHandler == null){
//			dbHandler = new DBHandler("conf/database.properties");
//		}
//		return true;
//	}
	
	public static Result add() {
		JsonNode json = request().body().asJson();
		 if(json == null) {
			    return badRequest("Expecting Json data");
		 } 
		 
		 if (!checkDao()){
			 return internalServerError("database conf file not found"); 
		 }

		 
		 // Parse JSON FIle 
//		 String deviceId = json.findPath("device_id").getTextValue();
		 String deviceTypeId = json.findPath("device_type_id").getTextValue();
		 String uri= json.findPath("uri").getTextValue();
		 String userDefinedFields = json.findPath("user_defined_field").getTextValue();
//		 models.Device device = new models.Device(deviceTypeId, uri, userDefinedFields);
		 
		 boolean result = deviceDao.addDevice(deviceTypeName, uri, userDefinedFields, longitude, latitude, altitude, representation);
		 if(!device.save()){
			System.err.println("device is not saved: " + error.toString());
			return ok("not saved");
		 } 
         return ok("saved");		 
	}

	public static Result getDevice(String format) {
		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		List<models.OldDevice> devices = deviceDao.getAllDevices();
		if(devices == null || devices.isEmpty()){
			return notFound("no devices found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			for (models.OldDevice device : devices) {
				if (ret.isEmpty())
					ret += "[";
				else				
					ret += ',';			
				ret += device.toJSONString();
			}
			ret += "]";			
		} else {			
			for (models.OldDevice device : devices) {
				if (!ret.isEmpty())
					ret += '\n';
				else
					ret += device.getCSVHeader();
				ret += device.toCSVString();
			}
		}
		return ok(ret);
	}

	public static Result getSensorType(String deviceType, String format) {
		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
		// case insensitive search. device types in the database are in lower case
		deviceType = deviceType.toLowerCase();
		ArrayList<String> sensorTypes = dbHandler.getSensorType(deviceType);
		if(sensorTypes == null || sensorTypes.isEmpty()){
			return notFound("No sensor type found for " + deviceType);
		}
		String ret = new String();
		if (format.equals("json"))
		{
			String sensorTypesStr = "";
			for (String sensorType : sensorTypes) {
				if (!sensorTypesStr.isEmpty())
					sensorTypesStr += ',';
				sensorTypesStr += sensorType;
			}
			ret = "{\"device_type\":\"" + deviceType + "\", \"sensor_type\":\"" + sensorTypesStr + "\"}";
		} else {
			for (String sensorType : sensorTypes) {
				if (!ret.isEmpty())
					ret += '\n';
				else
					ret += "sensor_types\n";
				ret += sensorType;
			}
		}
		return ok(ret);
	}	
	
}
