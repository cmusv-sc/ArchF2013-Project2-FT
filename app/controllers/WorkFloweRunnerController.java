package controllers;

import play.*;
import play.mvc.*;

import views.html.*;


public class WorkFloweRunnerController  extends Controller  {
	public static Result index() {
		   
		 return ok(runner.render("Your new application is ready."));
	  }
}
