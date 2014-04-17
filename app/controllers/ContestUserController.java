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
import java.util.ArrayList;

import models.ContestUser;
import models.dao.ContestUserDao;

import org.codehaus.jackson.JsonNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import play.mvc.Controller;
import play.mvc.Result;
//import models.cmu.sv.sensor.SensorReading;

import com.google.gson.Gson;

public class ContestUserController extends Controller {
		
	private static ApplicationContext context;
	private static ContestUserDao contestUserDao;
	
	private static void checkDao(){
		if (context == null) {
			context = new ClassPathXmlApplicationContext("application-context.xml");
		}
		if (contestUserDao == null) {
			contestUserDao = (ContestUserDao) context.getBean("contestUserDaoImplementation");
		}
	}

	public static Result addUser() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} 
		checkDao();

		Gson gson = new Gson();
		ContestUser user = gson.fromJson(request().body().asJson().toString(), ContestUser.class);

		
		ArrayList<String> error = new ArrayList<String>();
		
		boolean result = contestUserDao.addUser(user);

		if(!result){
			error.add(user.getUserName());
		}
		// Can this error have more than one name in it? I don't understand why error needs to be a list.
		if(error.size() == 0){
			System.out.println("user saved");
			return ok("user saved");
		}
		else{
			System.out.println("user not saved: " + error.toString());
			return ok("user not saved: " + error.toString());
		}
	}
	
	


}
