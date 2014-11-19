/*******************************************************************************
 * Copyright (c) 2013 Carnegie Mellon University Silicon Valley. 
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available
 * under the terms of dual licensing(GPL V2 for Research/Education
 * purposes). GNU Public License v2.0 which accompanies this distribution
 * is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * 
 * Please contact http://www.cmu.edu/silicon-valley/ if you have any 
 * questions.
 ******************************************************************************/
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
	public static Result changeInterval() throws Exception {
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
