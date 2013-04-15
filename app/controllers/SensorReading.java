package controllers;

import java.util.ArrayList;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import play.mvc.Controller;
import play.mvc.Result;
import models.cmu.sv.sensor.*;


public class SensorReading extends Controller {
	private static DBHandler dbHandler = null;
	private  static boolean testDBHandler(){
		if(dbHandler == null){
			dbHandler = new DBHandler("conf/database.properties");
		}
		return true;
	}
	
	public static Result add() {
		JsonNode json = request().body().asJson();
		 if(json == null) {
			    return badRequest("Expecting Json data");
		 } 
		 if(!testDBHandler()){
			 return internalServerError("database conf file not found");
		 }
		 
		// Parse JSON FIle 
		 String deviceId = json.findPath("id").getTextValue();
		 Long timeStamp = json.findPath("timestamp").getLongValue();
		 Iterator<String> it = json.getFieldNames();
		 ArrayList<String> error = new ArrayList<String>();
		 while(it.hasNext()){
			 String sensorType = it.next();
			 if(sensorType == "id" || sensorType == "timestamp") continue;
			 double value = json.findPath(sensorType).getDoubleValue();
			 if(!dbHandler.addReading(deviceId, timeStamp, sensorType, value)){
				 error.add(sensorType);
			 }
		 }
		 if(error.size() == 0)
			 return ok("saved");
		 else{
			 return ok("some not saved: " + error.toString());
		 }
	}
	public static Result search(String deviceId, Long timeStamp, String sensorType){
		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}
		models.cmu.sv.sensor.SensorReading reading = dbHandler.searchReading(deviceId, timeStamp, sensorType);
		if(reading == null){
			return notFound("the record not exists");
		}
		
		return ok(reading.getSensorType()+ ":" + String.valueOf( reading.getValue()));
	}

}
