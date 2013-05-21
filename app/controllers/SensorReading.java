package controllers;

import java.util.ArrayList;
import java.util.Iterator;

import models.cmu.sv.sensor.DBHandler;
import models.cmu.sv.sensor.MessageBusHandler;

import org.codehaus.jackson.JsonNode;

import play.mvc.Controller;
import play.mvc.Result;


public class SensorReading extends Controller {
	private static DBHandler dbHandler = null;
	private  static boolean testDBHandler(){
		if(dbHandler == null){
			dbHandler = new DBHandler("conf/database.properties");
		}
		return true;
	}
	
	public static Result add(Boolean publish) {
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
			 models.cmu.sv.sensor.SensorReading reading = new models.cmu.sv.sensor.SensorReading(deviceId, timeStamp, sensorType, value); 
			 if(!reading.save()){
				 error.add(sensorType);
			 }
			 
			 if(publish){
				 MessageBusHandler mb = new MessageBusHandler();
				 if(!mb.publish(reading)){
					 error.add("publish failed");
				 }
			 }
		 }
		 if(error.size() == 0){
			 System.out.println("saved");
             return ok("saved");
         }
		 else{
			 System.out.println("some not saved: " + error.toString());
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
