package controllers;

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

import com.google.gson.Gson;

public class DeviceController extends Controller {
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
		
		Device device = deviceDao.updateDevice(deviceUri, wrapper);
		if(device == null){
			return notFound("no devices found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(device);		
		} else {
			//TODO
//				ret = toCsv(Arrays.asList(device));
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
			ret = new Gson().toJson(devices);		
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
		
		Device device = deviceDao.getDevice(uri);
		if(device == null){
			return notFound("no device found");
		}
		String ret = new String();
		if (format.equals("json"))
		{							
			ret = new Gson().toJson(device);
							
		} else {						
				ret = device.toCSVString();			
		}
		return ok(ret);
	}

	
}
