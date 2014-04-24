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
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.ContestUser;
import models.User;
import models.dao.ContestUserDao;

import org.codehaus.jackson.JsonNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

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
	
	public static Result updateUser() {
		JsonNode json = request().body().asJson();
		if(json == null) {
			return badRequest("Expecting Json data");
		} 
		checkDao();

		Gson gson = new Gson();
		ContestUser user = gson.fromJson(request().body().asJson().toString(), ContestUser.class);

		
		ArrayList<String> error = new ArrayList<String>();
		
		boolean result = contestUserDao.updateUser(user);

		if(!result){
			error.add(user.getUserName());
		}
		// Can this error have more than one name in it? I don't understand why error needs to be a list.
		if(error.size() == 0){
			System.out.println("user updated");
			return ok("user updated");
		}
		else{
			System.out.println("user not updated: " + error.toString());
			return ok("user not updated: " + error.toString());
		}
	}
	
	public static Result deleteUser(String userName, String pwd) {
		
		checkDao();

	
		ArrayList<String> error = new ArrayList<String>();
		
		boolean result = contestUserDao.deleteUser(userName, pwd);

		if(!result){
			error.add(userName);
		}
		// Can this error have more than one name in it? I don't understand why error needs to be a list.
		if(error.size() == 0){
			System.out.println("user deleted");
			return ok("user deleted");
		}
		else{
			System.out.println("user not deleted: " + error.toString());
			return ok("user not deleted: " + error.toString());
		}
	}

	public static Result getUser(String userName, String pwd, String format) {
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		ContestUser user = contestUserDao.getUser(userName, pwd);
		if(user == null){
			return notFound("no contest users found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(user);		
		} else {			
				ret = toCsv(Arrays.asList(user));
		}
		return ok(ret);
	}
	
	public static Result getAllUsers(String format) {
		response().setHeader("Access-Control-Allow-Origin", "*");
		checkDao();
		List<ContestUser> users = contestUserDao.getAllUsers();
		if(users == null){
			return notFound("no contest users found");
		}
		String ret = new String();
		if (format.equals("json"))
		{			
			ret = new Gson().toJson(users);		
		} else {			
				ret = toCsv(users);
		}
		return ok(ret);
	}

	
	private static String toCsv(List<ContestUser> users) {
		StringWriter sw = new StringWriter();
		CellProcessor[] processors = new CellProcessor[] {
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional()
				};
		ICsvBeanWriter writer = new CsvBeanWriter(sw, CsvPreference.STANDARD_PREFERENCE);
		try {
			final String[] header = new String[] { "userName",  "password", "firstName", "lastName", "middleName", "affiliation", "email", "researchArea", "goal"};
			writer.writeHeader(header);
			for (ContestUser user : users) {
				writer.write(user, header, processors);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sw.getBuffer().toString();
	}

}
