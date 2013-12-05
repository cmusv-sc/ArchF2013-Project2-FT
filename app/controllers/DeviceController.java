package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//import models.DBHandler;
import models.Device;
import models.DeviceType;
import models.dao.DeviceDao;

import org.codehaus.jackson.JsonNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;

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
		 String deviceTypeName = json.findPath("device_type_name").getTextValue();
		 String uri= json.findPath("uri").getTextValue();
//		 String deviceTypeId = json.findPath("device_type_id").getTextValue();
		 String userDefinedFields = json.findPath("user_defined_field").getTextValue();
		 double longitude = json.findPath("longitude").getDoubleValue();
		 double latitude = json.findPath("latitude").getDoubleValue();
		 double altitude = json.findPath("altitude").getDoubleValue();
		 String representation = json.findPath("representation").getTextValue();
//		 models.Device device = new models.Device(deviceTypeId, uri, userDefinedFields);
		 
		 boolean result = deviceDao.addDevice(deviceTypeName, uri, userDefinedFields, longitude, latitude, altitude, representation);
		 
		 if(!result){
			System.err.println(deviceTypeName + " is not saved: " + error.toString());
			return ok("device not saved");
		 } 
         return ok("device saved");		 
	}
	
	public static Result updateDevice(String deviceUri, String format) {
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		
		Gson gson = new Gson();
		
		Device wrapper = gson.fromJson(request().body().asJson().toString(), Device.class);
		
		ArrayList<String> error = new ArrayList<String>();

		
		Device device = deviceDao.updateDevice(deviceUri, wrapper);
		if(device == null){
			return notFound("no devices found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(device);		
		} else {			
				ret = toCsv(Arrays.asList(device));
		}
		return ok(ret);
	}

	public static Result getAllDevices(String format) {
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
	    // case insensitive search. device types in the database are in lower case
		
		List<models.Device> devices = deviceDao.getAllDevices();
		if(devices == null || devices.isEmpty()){
			return notFound("no devices found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			for (models.Device device : devices) {
				if (ret.isEmpty())
					ret += "[";
				else				
					ret += ',';			
				ret += device.toJSONString();
			}
			ret += "]";			
		} else {			
			for (models.Device device : devices) {
				if (!ret.isEmpty())
					ret += '\n';
				else
					ret += device.getCSVHeader();
				ret += device.toCSVString();
			}
		}
		return ok(ret);
	}
	
	public static Result getDevice(String uri, String format) {
		if(!checkDao()){
			return internalServerError("database conf file not found");
		}
		response().setHeader("Access-Control-Allow-Origin", "*");
	    // case insensitive search. device types in the database are in lower case
		
		Device device = deviceDao.getDevice(uri);
		if(device == null){
			return notFound("no device found");
		}
		String ret = new String();
		if (format.equals("json"))
		{							
				ret = device.toJSONString();
							
		} else {						
				ret = device.toCSVString();			
		}
		return ok(ret);
	}

//	public static Result getSensorType(String deviceType, String format) {
//		if(!checkDao()){
//			return internalServerError("database conf file not found");
//		}
//		response().setHeader("Access-Control-Allow-Origin", "*");
//		// case insensitive search. device types in the database are in lower case
//		deviceType = deviceType.toLowerCase();
//		ArrayList<String> sensorTypes = deviceDao.getSensorType(deviceType);
//		if(sensorTypes == null || sensorTypes.isEmpty()){
//			return notFound("No sensor type found for " + deviceType);
//		}
//		String ret = new String();
//		if (format.equals("json"))
//		{
//			String sensorTypesStr = "";
//			for (String sensorType : sensorTypes) {
//				if (!sensorTypesStr.isEmpty())
//					sensorTypesStr += ',';
//				sensorTypesStr += sensorType;
//			}
//			ret = "{\"device_type\":\"" + deviceType + "\", \"sensor_type\":\"" + sensorTypesStr + "\"}";
//		} else {
//			for (String sensorType : sensorTypes) {
//				if (!ret.isEmpty())
//					ret += '\n';
//				else
//					ret += "sensor_types\n";
//				ret += sensorType;
//			}
//		}
//		return ok(ret);
//	}	
	
}
