package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.runner;


public class WorkFloweRunnerController  extends Controller  {
	public static Result index() {
		   
		 return ok(runner.render("test",5));
	  }
}
