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
		 int deviceId = json.findPath("id").getIntValue();
		 int timeStamp = json.findPath("timestamp").getIntValue();
		 Iterator<String> it = json.getFieldNames();
		 ArrayList<String> error = new ArrayList();
		 while(it.hasNext()){
			 String sensorType = it.next();
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
	public static Result search(int deviceId, int timeStamp){
		if(!testDBHandler()){
			return internalServerError("database conf file not found");
		}
		models.cmu.sv.sensor.SensorReading reading = dbHandler.searchReading(deviceId, timeStamp);
		if(reading == null){
			return notFound("the record not exists");
		}
		
		return ok(reading.getSensorType()+ ":" + String.valueOf( reading.getValue()));
	}
	/*
	public static void updateUser(Long id, User user) {
	    User dbUser = User.findById(id);
	    dbUser.updateDetails(user); // some model logic you would write to do a safe merge
	    dbUser.save();
	    user(id);
	}

	public static void deleteUser(Long id) {
	    User.findById(id).delete();
	    renderText("success");
	}

	public static void user(Long id)  {
	    User user = User.findById(id)
	    render(user);
	}
	*/
}
