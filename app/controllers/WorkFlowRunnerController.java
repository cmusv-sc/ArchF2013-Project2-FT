package controllers;

import org.codehaus.jackson.JsonNode;

import models.WorkFlowRunner;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.runner;


public class WorkFlowRunnerController  extends Controller  {
	
	public static Result index(String format) {
		
		WorkFlowRunner WFRunner = new WorkFlowRunner("WFRun", "virtual_device", "Interval");
		if(format == "json"){
			return ok(String.valueOf(WFRunner.computeThreshold()));
		}
		return ok(runner.render("Virtual_Device",WFRunner.computeThreshold(), 1));
	  }
	public static Result changeInterval(){
		WorkFlowRunner WFRunner = new WorkFlowRunner("WFRun", "virtual_device", "Interval");
		JsonNode json = request().body().asJson();
		 if(json == null) {
			    return badRequest("Expecting Json data");
		 } 
		 int newInterval = json.findPath("interval").asInt();
		 boolean result = WFRunner.notifyVirtualDevice(newInterval);
		 return ok(String.valueOf(result));
	}
}
